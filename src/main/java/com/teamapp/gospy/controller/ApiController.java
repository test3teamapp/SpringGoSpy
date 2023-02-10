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
public class ApiController {
    @Autowired
    PersonRepository personRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/api/person/getall")
    Iterable<Person> allTrackedPeople() {
        System.out.println("allTrackedPeople() called");
        //return personRepo.findAll(Sort.by(Sort.Direction.ASC, "locationUpdated"));
        return personRepo.findAll();
    }

    @GetMapping("/api/person/getById/{id}")
    Optional<Person> personById(@PathVariable("id") String id) {
        return personRepo.findById(id);
    }

    @PostMapping(path = "/api/person/create/" , consumes = "application/json")
    Optional<Person> createPerson(@RequestBody Person newPerson) {
        System.out.println(newPerson.toString());
        Person savedPerson = personRepo.save(newPerson);
        return Optional.of(savedPerson);
    }

    @GetMapping("/api/person/updateLocation/byName/{name}/lat/{lat}/lng/{lng}")
    Optional<Person> updateLocation(@PathVariable String name,  @PathVariable String lat, @PathVariable String lng) {
        return Optional.of(null);
    }

    /// USERS com.teamapp.gospy.controller.GoSpyErrorController#handleError(HttpServletRequest)

    @GetMapping("/api/user/getall")
    Iterable<User> allUsers() {

        //return userRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return userRepo.findAll();
    }

    @GetMapping("/api/user/getById/{id}")
    Optional<User> userById(@PathVariable("id") String id) {
        return userRepo.findById(id);
    }
    @PostMapping("/api/user/create")
    Optional<User> createUser(@RequestBody User newUser) {
        User savedUser = userRepo.save(newUser);
        return Optional.of(savedUser);
    }

}