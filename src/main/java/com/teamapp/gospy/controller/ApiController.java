package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.models.PersonRepository;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    PersonRepository personRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/person/getall")
    Iterable<Person> allTrackedPeople() {

        //return personRepo.findAll(Sort.by(Sort.Direction.ASC, "locationUpdated"));
        return personRepo.findAll();
    }

    @GetMapping("/person/getById/{id}")
    Optional<Person> personById(@PathVariable("id") String id) {
        return personRepo.findById(id);
    }

    @GetMapping("/person/create/{name}")
    Optional<Person> createPerson(@PathVariable("name") String name) {
        Person temp = Person.of(name);
        Person savedPerson = personRepo.save(temp);
        return Optional.of(savedPerson);
    }

    /// USERS

    @GetMapping("/user/getall")
    Iterable<User> allUsers() {

        //return userRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return userRepo.findAll();
    }

    @GetMapping("/user/getById/{id}")
    Optional<User> userById(@PathVariable("id") String id) {
        return userRepo.findById(id);
    }
}