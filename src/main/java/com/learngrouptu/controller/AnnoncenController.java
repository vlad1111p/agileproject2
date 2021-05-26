package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class AnnoncenController {

    private final AnnonceRepository annonceRepository;

    @Autowired
    public AnnoncenController(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    @RequestMapping(value = "/annonceErstellen", method = RequestMethod.GET)
    public String showAnnonceErstellen() {
        return "annonceErstellen";
    }

    @GetMapping("/annonceEinsehen")
    public String showAnnonceEinsehen(Model model) {
        model.addAttribute("annoncen", annonceRepository.findAll());
        return "annonceEinsehen";
    }

    @GetMapping("/annonceErstellenNeu")
    public String showAnnonceErstellenNeu(Annonce annonce) {
        return "annonceErstellenNeu";
    }

    @PostMapping("/addannonce")
    public String addUser(@Valid Annonce annonce, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:annonceErstellenNeu";
        }

        annonceRepository.save(annonce);
        return "annonceEinsehen";
    }

}
