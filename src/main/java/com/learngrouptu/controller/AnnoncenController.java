package com.learngrouptu.controller;

import com.learngrouptu.model.AnnonceRepository;
import com.learngrouptu.model.Annonce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnnoncenController {

    private final AnnonceRepository annonceRepository;

    @Autowired
    public AnnoncenController(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    @RequestMapping(value = "/annonceErstellen", method = RequestMethod.GET)
    public String showAnnonceErstellen(){return "annonceErstellen";}

    @RequestMapping(value = "/annonceEinsehen", method = RequestMethod.GET)
    public String showAnnonceEinsehen(Model model){
        model.addAttribute("annoncen", annonceRepository.findAll());
        return "annonceEinsehen";}

    /*
    @PostMapping("/annonce_created")
    public String setAnnonce (@RequestParam(value = "vorlName", required = true) String vorlName,
                              @RequestParam(value = "choice", required = true) String choice,
                              @RequestParam(value = "kontakt", required = true) String kontakt,
                              @RequestParam(value = "Nachricht", required = true) String nachricht) {
        annonceRepository.save(annonce);
        return "annonceEinsehen";
    }
    */
    @PostMapping("/annonce_created")
    public String setAnnonce(@ModelAttribute("annonce") Annonce annonce, Model model){
        annonceRepository.save(annonce);
        return "annonceEinsehen";
    }
}
