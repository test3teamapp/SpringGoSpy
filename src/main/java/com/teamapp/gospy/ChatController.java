package com.teamapp.gospy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "myName", defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "chat";
    }
}
