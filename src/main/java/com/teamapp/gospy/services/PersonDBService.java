package com.teamapp.gospy.services;

import com.teamapp.gospy.helperobjects.CommandReplyObj;
import com.teamapp.gospy.models.Person;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.redis.om.spring.search.stream.EntityStream;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class PersonDBService {

    private final String apiHost = "https://gospy.rheotome.eu:8084";
    private static Logger logger = LoggerFactory.getLogger(PersonDBService.class);


    private String trustStorePath;
    private String trustStorePass;
    private String keyStorePath;
    private String keyStorePass;
    private String keyStoreType;
    private String keyAlias;

    @Autowired
    EntityStream entityStream; // not working for us because of indexing mismatch in redis
    // as the records have already been created with the nodejs redis-om library
    // and even we have e.g. @Keyspace(Person) annotation on the Person class
    // the record index is PersonIdx, while the nodejs default was Person:index

    @Autowired
    private Environment env;

    WebClient client;

    public PersonDBService() {
        /*
        server.ssl.key-store=classpath:gospy_spring_keystore.pkcs12
server.ssl.key-store-password=rheotome_eu
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=rheotome_eu
server.ssl.key-password=...
server.ssl.port=8443
server.ssl.enabled=true

#trust store location
trust.store=classpath:gospy_spring_truststore
#trust store password
trust.store.password=rheotome_eu
         */
        /// Environment is null at this point .... :(
        /// so ...
        String thisDir = System.getProperty("user.dir");
        logger.info("Working Directory = " + thisDir);
        this.trustStorePath = thisDir + "/gospy_spring_truststore";
        this.trustStorePass = "rheotome_eu";
        this.keyStorePath = thisDir + "/gospy_spring_keystore.pkcs12";
        this.keyStorePass = "rheotome_eu";
        this.keyStoreType = "PKCS12";
        this.keyAlias = "rheotome_eu";

        this.client = buildSSLWebClient();
    }

    private WebClient buildSSLWebClient() {
        SslContext sslContext;
        final PrivateKey privateKey;
        final X509Certificate[] certificates;

        try {
            final KeyStore trustStore;
            final KeyStore keyStore;
            trustStore = KeyStore.getInstance(keyStoreType);
            trustStore.load(new FileInputStream(ResourceUtils.getFile(trustStorePath)), trustStorePass.toCharArray());
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(new FileInputStream(ResourceUtils.getFile(keyStorePath)), keyStorePass.toCharArray());
            List<Certificate> certificateList = Collections.list(trustStore.aliases())
                    .stream()
                    .filter(t -> {
                        try {
                            return trustStore.isCertificateEntry(t);
                        } catch (KeyStoreException e1) {
                            throw new RuntimeException("Error reading truststore", e1);
                        }
                    })
                    .map(t -> {
                        try {
                            return trustStore.getCertificate(t);
                        } catch (KeyStoreException e2) {
                            throw new RuntimeException("Error reading truststore", e2);
                        }
                    })
                    .collect(Collectors.toList());
            logger.info("Found " + certificateList.size() + " certificates in trust store");
            certificates = certificateList.toArray(new X509Certificate[certificateList.size()]);
            privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyStorePass.toCharArray());
            Certificate[] certChain = keyStore.getCertificateChain(keyAlias);
            logger.info("Found " + certChain.length + " certificates in key store");
            X509Certificate[] x509CertificateChain = Arrays.stream(certChain)
                    .map(certificate -> (X509Certificate) certificate)
                    .collect(Collectors.toList())
                    .toArray(new X509Certificate[certChain.length]);
            sslContext = SslContextBuilder.forClient()
                    .keyManager(privateKey, keyStorePass, x509CertificateChain)
                    .trustManager(certificates)
                    .build();

            reactor.netty.http.client.HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
            ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

            return WebClient.builder()
                    .baseUrl(apiHost)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .clientConnector(connector)
                    .build();

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                 UnrecoverableKeyException e) {
            logger.info("Exception while preparing webclient for ssl connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Find all people
    public Iterable<Person> findAllPeople() {
//        return entityStream //
//                .of(Person.class) //
//                .collect(Collectors.toList());
        ResponseSpec responseSpec = client.get()
                .uri(apiHost + "/persons/all")
                .retrieve();

        Flux<Person> responseFluxOfPerson = responseSpec.bodyToFlux(Person.class);

//        AtomicInteger count = new AtomicInteger();
//        responseFluxOfPerson.subscribe(i -> count.getAndIncrement(),
//                error -> System.err.println("Error during request 'findAllPeople'"),
//                () -> System.out.println("all done 'findAllPeople' : " + count));

        return responseFluxOfPerson.toIterable();
    }


    public void printAllPeople() {
//        return entityStream //
//                .of(Person.class) //
//                .collect(Collectors.toList());

        ResponseSpec responseSpec = client.get()
                .uri(apiHost + "/persons/all")
                .retrieve();

        Flux<Person> responseFluxOfPerson = responseSpec.bodyToFlux(Person.class);

        responseFluxOfPerson.subscribe(i -> System.out.println(i.toString()));
    }


    public Mono<CommandReplyObj> sendCommandToDevice(String devicename, String command) {
        ResponseSpec responseSpec = client.get()
                .uri(apiHost + "/sendCommand/spring/byName/" + devicename + "/command/" + command)
                .retrieve();

        Mono<CommandReplyObj> responseMonoCommandReplyObj = responseSpec.bodyToMono(CommandReplyObj.class);


        return responseMonoCommandReplyObj;
    }


    public Mono<CommandReplyObj> waitForLUForDevice(String devicename) {
        ResponseSpec responseSpec = client.get()
                .uri(apiHost + "/waitForLU/spring/byName/" + devicename)
                .retrieve();

        Mono<CommandReplyObj> responseMonoCommandReplyObj = responseSpec.bodyToMono(CommandReplyObj.class);

        return responseMonoCommandReplyObj;
    }

}
