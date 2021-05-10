package com.example.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@Controller
public class LearngroupTuApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearngroupTuApplication.class, args);
    }



    @RequestMapping("/annance_created")
    public ModelAndView index () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("homepage");
        return modelAndView;

    }
}



