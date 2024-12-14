package com.example.database_project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session) {

        session.setAttribute("option", (int)0);
        System.out.println("option result: " + session.getAttribute("option"));

        return "/home";
    }

    @GetMapping("/home")
    public String homepage(HttpSession session) {
        session.setAttribute("option", (int)0);
        System.out.println("option result: " + session.getAttribute("option"));

        return "/home";
    }

}
