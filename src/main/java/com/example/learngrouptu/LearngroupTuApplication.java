package com.example.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LearngroupTuApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearngroupTuApplication.class, args);
    }



    @GetMapping("/annance_created")
    public String index (@RequestParam(value = "myVorlesung", required = true) String name,
                         @RequestParam(value = "choice", required = true) String choice,
                         @RequestParam(value = "kontakt", required = true) String kontakt,
                         @RequestParam(value = "Nachricht", required = true) String nachricht) {
        return name+" "+choice+ " " +kontakt+ " " +nachricht;
    }
}



