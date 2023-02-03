package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.models.PersonRepository;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    PersonRepository personRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/person/all")
    Iterable<Person> allTrackedPeople() {
        return personRepo.findAll(Sort.by(Sort.Direction.ASC,"locationUpdated"));
    }

    @GetMapping("/person/{id}")
    Optional<Person> personById(@PathVariable String id) {
        return personRepo.getById(id);
    }

    /// USERS

    @GetMapping("/user/all")
    Iterable<User> allUsers() {
        return userRepo.findAll(Sort.by(Sort.Direction.ASC,"name"));
    }

    @GetMapping("/user/{id}")
    Optional<User> userById(@PathVariable String id) {
        return userRepo.getById(id);
    }
}