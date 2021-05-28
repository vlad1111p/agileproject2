package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AnnoncenController {

    private final AnnonceRepository annonceRepository;

    @Autowired
    public AnnoncenController(AnnonceRepository annonceRepository){this.annonceRepository = annonceRepository;}

    @GetMapping("/annonceEinsehen")
    public String showAnnonceEinsehen(Model model){
        model.addAttribute("annoncen", annonceRepository.findAll());
        return "annonceEinsehen";
    }

    @GetMapping("/annonceErstellen")
    public String showAnnonceErstellen(Annonce annonce){
        return "annonceErstellen";
    }

    @PostMapping("/addannonce")
    public String addUser(@Valid Annonce annonce, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:annonceErstellen";
        }
        annonceRepository.save(annonce);
        return showAnnonceEinsehen(model);
    }

    @Transactional
    @PostMapping("/deleteannonce")
    public String deleteAnnonce(@RequestParam Integer id, Model model){

        annonceRepository.deleteByAnnonceId(id);
        return showAnnonceEinsehen(model);
        //return "redirect:annonceEinsehen";
    }

}
