package com.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;


import java.sql.*;

@RestController
@SpringBootApplication//(exclude = { SecurityAutoConfiguration.class })
public class LearngroupTuApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearngroupTuApplication.class, args);
    }
}



