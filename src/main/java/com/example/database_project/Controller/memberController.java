package com.example.database_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class memberController {
    @PostMapping("/signUp")
    public String signUpPage() {

        return "/";
    }

    @GetMapping("/signUp")
    public String signUp() { return "/signUp"; }
}