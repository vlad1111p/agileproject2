package com.learngrouptu.controller;

import com.learngrouptu.models.Vorlesung;
import com.learngrouptu.models.VorlesungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class VorlesungController {

    private final VorlesungRepository vorlesungRepository;
    @Autowired
    public VorlesungController(VorlesungRepository vorlesungRepository){this.vorlesungRepository = vorlesungRepository;}


    @GetMapping("/vorlesungsubersicht")
    public String showVorlesungEinsehen(Model model){
        model.addAttribute("vorlesungsubersicht", vorlesungRepository.findAll());
        return "vorlesungsubersicht";
    }
    @GetMapping("/vorlesungsubersichterstellen")
    public String showVorlesungErstellen(Vorlesung vorlesung){
        return "vorlesungsubersichterstellen";
    }


    @PostMapping(value="/vorlesungadd")
    public String addUser(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:vorlesungsubersichterstellen";
        }

        vorlesungRepository.save(vorlesung);
        return "vorlesungsubersichterstellen";
    }
}
