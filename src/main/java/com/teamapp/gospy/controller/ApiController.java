package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.models.PersonRepository;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import com.teamapp.gospy.services.PersonDBService;
import com.teamapp.gospy.services.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class ApiController {
    @Autowired
    PersonRepository personRepo;

    @Autowired
    PersonDBService personDBService;

    @Autowired
    UserDBService userDBService;

    @Autowired
    UserRepository userRepo;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/person/getall")
    Iterable<Person> allTrackedPeople() {
        System.out.println("allTrackedPeople() called");
        //return personRepo.findAll(Sort.by(Sort.Direction.ASC, "locationUpdated"));
        return personDBService.findAllPeople();
        //return personRepo.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/person/getById/{id}")
    Optional<Person> personById(@PathVariable("id") String id) {
        return personRepo.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "/api/person/create/" , consumes = "application/json")
    Optional<Person> createPerson(@RequestBody Person newPerson) {
        System.out.println(newPerson.toString());
        Person savedPerson = personRepo.save(newPerson);
        return Optional.of(savedPerson);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/person/updateLocation/byName/{name}/lat/{lat}/lng/{lng}")
    Optional<Person> updateLocation(@PathVariable String name,  @PathVariable String lat, @PathVariable String lng) {

        return Optional.of(null);
    }

    /// USERS com.teamapp.gospy.controller.GoSpyErrorController#handleError(HttpServletRequest)

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/getall")
    Iterable<User> allUsers() {

        //return userRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return userRepo.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/getById/{id}")
    Optional<User> userById(@PathVariable("id") String id) {
        return userRepo.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/getByName/{name}")
    Optional<User> userByName(@PathVariable("name") String name) {

        return userRepo.findOneByUsername(name);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/getByToken/{token}")
    Optional<User> userByToken(@PathVariable("token") String token) {

        return userRepo.findOneByToken(token);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/api/user/create")
    Optional<User> createUser(@RequestBody User newUser) {
        User savedUser = userRepo.save(newUser);
        return Optional.of(savedUser);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/updatelogin/byName/{name}")
    void updatelogin(@PathVariable("name") String name) {
        userDBService.updateUserLoginDate(name);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/updatetoken/byName/{name}/withtoken/{token}")
    void updatetoken(@PathVariable("name") String name, @PathVariable("token") String token) {
        userDBService.updateUserToken(name, token);
    }

    // create guest user with 5 minutes expiration period

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping("/newguest")
    Optional<User> newGuestUser() {
        User newUser = new User(true);
        User savedUser = userRepo.save(newUser);
        return Optional.of(savedUser);
    }

}