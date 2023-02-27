package com.teamapp.gospy.services;

import com.teamapp.gospy.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.redis.om.spring.search.stream.EntityStream;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PersonDBService {

    @Autowired
    EntityStream entityStream; // not working for us because of indexing mismatch in redis
    // as the records have already been created with the nodejs redis-om library
    // and even we have e.g. @Keyspace(Person) annotation on the Person class
    // the record index is PersonIdx, while the nodejs default was Person:index

    WebClient client;
    public PersonDBService() {
       this.client = WebClient.builder()
               .baseUrl("http://rheotome.eu:8084")
               .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
               .build();
    }

    // Find all people
    public Iterable<Person> findAllPeople() {
//        return entityStream //
//                .of(Person.class) //
//                .collect(Collectors.toList());
        ResponseSpec responseSpec = client.get()
                .uri("http://rheotome.eu:8084/persons/all")
                .retrieve();

        Flux<Person> responseFluxOfPerson = responseSpec.bodyToFlux(Person.class);

        AtomicInteger count = new AtomicInteger();
        responseFluxOfPerson.subscribe(i -> count.getAndIncrement(),
                error -> System.err.println("Error during request 'findAllPeople'"),
                () -> System.out.println("all done 'findAllPeople' : " + count));

        return responseFluxOfPerson.toIterable();
    }

    public void printAllPeople() {
//        return entityStream //
//                .of(Person.class) //
//                .collect(Collectors.toList());

        ResponseSpec responseSpec = client.get()
                .uri("http://rheotome.eu:8084/persons/all")
                .retrieve();

        Flux<Person> responseFluxOfPerson = responseSpec.bodyToFlux(Person.class);

        responseFluxOfPerson.subscribe(i -> System.out.println(i.toString()));
    }

    public void sendCommandToDevice(String devicename, String command){
        ResponseSpec responseSpec = client.get()
                .uri("http://rheotome.eu:8084/sendCommand/byName/"+ devicename + "/command/" + command)
                .retrieve();

        Mono<String> responseMonoVoid = responseSpec.bodyToMono(String.class);

        responseMonoVoid.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error during request 'sendCommandToDevice'"),
                () -> System.out.println("all done 'sendCommandToDevice' : "));

    }

}
