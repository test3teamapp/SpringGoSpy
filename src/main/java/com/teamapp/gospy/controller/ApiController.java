package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.models.PersonRepository;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import com.teamapp.gospy.services.PersonDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ApiController {
    @Autowired
    PersonRepository personRepo;

    @Autowired
    PersonDBService personDBService;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/api/person/getall")
    Iterable<Person> allTrackedPeople() {
        System.out.println("allTrackedPeople() called");
        //return personRepo.findAll(Sort.by(Sort.Direction.ASC, "locationUpdated"));
        return personDBService.findAllPeople();
        //return personRepo.findAll();
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

    @GetMapping("/api/user/getByName/{name}")
    Optional<User> userByName(@PathVariable("name") String name) {

        return userRepo.findOneByUsername(name);
    }

    @GetMapping("/api/user/getByToken/{token}")
    Optional<User> userByToken(@PathVariable("token") String token) {

        return userRepo.findOneByToken(token);
    }

    @PostMapping("/api/user/create")
    Optional<User> createUser(@RequestBody User newUser) {
        User savedUser = userRepo.save(newUser);
        return Optional.of(savedUser);
    }



}