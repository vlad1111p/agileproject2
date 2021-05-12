package com.learngrouptu.homepage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Annonce {

    @GetMapping("/annonce_created")
    public String SetAnnonce (@RequestParam(value = "vorlName", required = true) String vorlName,
                         @RequestParam(value = "choice", required = true) String choice,
                         @RequestParam(value = "kontakt", required = true) String kontakt,
                         @RequestParam(value = "Nachricht", required = true) String nachricht) {
        return vorlName +" "+choice+ " " +kontakt+ " " +nachricht;
    }
}
