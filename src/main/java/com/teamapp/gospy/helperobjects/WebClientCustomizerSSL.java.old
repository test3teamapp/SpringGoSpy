package com.teamapp.gospy.helperobjects;

import com.teamapp.gospy.configuration.WebSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebClientCustomizerSSL implements WebClientCustomizer {

    private static Logger logger = LoggerFactory.getLogger(WebClientCustomizerSSL.class);

    @Autowired
    private Environment env;
    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        SslContext sslContext;
        final PrivateKey privateKey;
        final X509Certificate[] certificates;

        String trustStorePath = env.getProperty("server.ssl.trust-store");
        String trustStorePass = env.getProperty("server.ssl.trust-store-password");
        String keyStorePath = env.getProperty("server.ssl.key-store");
        String keyStorePass = env.getProperty("server.ssl.key-store-password");
        String keyAlias = env.getProperty("server.ssl.key-alias");
        try {
            final KeyStore trustStore;
            final KeyStore keyStore;
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(ResourceUtils.getFile(trustStorePath)), trustStorePass.toCharArray());
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
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

            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
            ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
            webClientBuilder.clientConnector(connector);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                 UnrecoverableKeyException e) {
            logger.info("Exception while preparing webclient for ssl connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
