package com.example.database_project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session) {

        session.setAttribute("sortby", (int)0);
        System.out.println("sortby result: " + session.getAttribute("sortby"));

        return "/home";
    }

    @GetMapping("/home")
    public String homepage(HttpSession session) {
        session.setAttribute("sortby", (int)0);
        System.out.println("sortby result: " + session.getAttribute("sortby"));

        return "/home";
    }

}
