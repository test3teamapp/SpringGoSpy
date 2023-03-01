package com.teamapp.gospy.controller;

import com.teamapp.gospy.helperobjects.CommandObj;
import com.teamapp.gospy.helperobjects.CommandReplyObj;
import com.teamapp.gospy.models.Person;
import com.teamapp.gospy.services.PersonDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MapsController {

    @Autowired
    PersonDBService personDBService;

    @ModelAttribute("allPeopleList")
    public Iterable<Person> messages() {
        return this.fetchAllPeopleFromDB();
    }

    @GetMapping("/maps")
    public void maps(@RequestParam(value = "myName", defaultValue = "World") String name, Model model) {
        //return
        initController(name, model);
    }

    private void initController(String name, Model model) {
        model.addAttribute("nameInParam", name);
        model.addAttribute("selectedDeviceId", null);
        model.addAttribute("commandObj", new CommandObj());
        //return "maps";
    }

    private Iterable<Person> fetchAllPeopleFromDB() {

        //return personRepo.findAll();
        // personDBService.printAllPeople();
        return personDBService.findAllPeople();
    }

    @PostMapping("/maps/sendCommandToDeviceLU")
    public String sendCommandToDeviceLU(@ModelAttribute CommandObj commandObj, BindingResult errors, Model model) {
        personDBService.sendCommandToDevice(commandObj.getDeviceId(), "TRIGGER_LU");
        return "redirect:/maps";
    }

    @PostMapping("/maps/sendCommandToDeviceSTOP")
    public String sendCommandToDeviceSTOP(@ModelAttribute CommandObj commandObj, BindingResult errors, Model model) {
        personDBService.sendCommandToDevice(commandObj.getDeviceId(), "STOP_TRACKING");
        return "redirect:/maps";
    }

    // trying to make the page interactive, without POST/GET but with websockets msgs
    @MessageMapping("/maps-sendcommand")
    @SendToUser("/page/maps-reply")
    public CommandReplyObj sendCommand(CommandObj commandObj) throws Exception {

        Mono<CommandReplyObj> crobj = personDBService.sendCommandToDevice(commandObj.getDeviceId(), commandObj.getCommand());
//        crobj.subscribe(i -> System.out.println(i.toString()),
//                error -> System.err.println("Error during request 'sendCommandToDevice' : " + error.toString()),
//                () -> System.out.println("all done 'sendCommandToDevice' "));

        CommandReplyObj returnObj = crobj.block();
        return  returnObj;
    }

    @MessageMapping("/maps-waitforlu")
    @SendToUser("/page/maps-reply")
    public CommandReplyObj waitForLU(String deviceid) throws Exception {

        Mono<CommandReplyObj> crobj = personDBService.waitForLUForDevice(deviceid);
//        crobj.subscribe(i -> System.out.println(i.toString()),
//                error -> System.err.println("Error during request 'waitForLUForDevice' : " + error.toString()),
//                () -> System.out.println("all done 'waitForLUForDevice' "));
        CommandReplyObj returnObj = crobj.block();
        return  returnObj;
    }
}
