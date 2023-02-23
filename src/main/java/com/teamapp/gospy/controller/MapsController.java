package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.services.PersonDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class MapsController {

    @Autowired
    PersonDBService personDBService;

    @GetMapping("/maps")
    public String maps(@RequestParam(value = "myName", defaultValue = "World") String name, Model model) {
        return initController(name, model);
    }

    private String initController(String name, Model model){
        model.addAttribute("nameInParam", name);
        model.addAttribute("allPeopleList", this.fetchAllPeopleFromDB());
        return "maps";
    }

    private Iterable<Person> fetchAllPeopleFromDB(){

        //return personRepo.findAll();
        // personDBService.printAllPeople();
        return personDBService.findAllPeople();
    }
}
