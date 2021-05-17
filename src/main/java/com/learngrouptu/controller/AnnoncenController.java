package com.learngrouptu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnnoncenController {

    @RequestMapping(value = "/annonceErstellen", method = RequestMethod.GET)
    public String showAnnonceErstellen(){return "annonceErstellen";}

    @RequestMapping(value = "/annonceEinsehen", method = RequestMethod.GET)
    public String showAnnonceEinsehen(Model model){model.addAttribute("annoncen", annonceRepository.findAll());
        return "annonceEinsehen";}

    @PostMapping("/annonce_created")
    public String SetAnnonce (@RequestParam(value = "vorlName", required = true) String vorlName,
                              @RequestParam(value = "choice", required = true) String choice,
                              @RequestParam(value = "kontakt", required = true) String kontakt,
                              @RequestParam(value = "Nachricht", required = true) String nachricht) {
        annonceRepository.save(annonce);
        return "annonceEinsehen";
    }
}
