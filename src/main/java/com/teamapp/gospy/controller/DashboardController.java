package com.teamapp.gospy.controller;

import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.models.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {
    @Autowired
    PersonRepository personRepo;

    @RequestMapping({"/","/dashboard"})
    public String home(@RequestParam(value = "myName", defaultValue = "World") String name, Model model) {
        return initController(name, model);
    }

    private String initController(String name, Model model){
        model.addAttribute("nameInParam", name);
        model.addAttribute("allPeopleList", this.fetchAllPeopleFromDB());
        return "dashboard";
    }

    private Iterable<Person> fetchAllPeopleFromDB(){
        return personRepo.findAll();
    }
}
