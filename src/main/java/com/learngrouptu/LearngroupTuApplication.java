package com.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.sql.*;

@SpringBootApplication
@RestController
public class LearngroupTuApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearngroupTuApplication.class, args);
    }
}



