package com.example.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LearngroupTuApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearngroupTuApplication.class, args);
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myVorlesung", defaultValue =  "World", required = true) String name,
                           @RequestParam(value = "choice", required = true) String choice,
                           @RequestParam(value = "email", required = true) String email,
                           @RequestParam(value = "Nachricht", required = false) String nachricht) {
        return String.format("Hello %s! %s %s %s", name, choice,email,nachricht);
    }
}



