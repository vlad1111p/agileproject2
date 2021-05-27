package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.AnnonceRepository;
import com.learngrouptu.models.Vorlesung;
import com.learngrouptu.models.VorlesungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VorlesungController {

    private final VorlesungRepository vorlesungRepository;
    private final AnnonceRepository annonceRepository;

    @Autowired
    public VorlesungController(VorlesungRepository vorlesungRepository, AnnonceRepository annonceRepository) {
        this.vorlesungRepository = vorlesungRepository;
        this.annonceRepository = annonceRepository;
    }


    @GetMapping("/vorlesungsubersicht")
    public String showVorlesungEinsehen(Model model) {
        model.addAttribute("vorlesungsubersicht", vorlesungRepository.findAll());
        return "vorlesungsubersicht";
    }

    @GetMapping("/vorlesungsubersichterstellen")
    public String showVorlesungErstellen(Vorlesung vorlesung) {
        return "vorlesungsubersichterstellen";
    }

    @GetMapping("/vorlesungsgruppeerstellen")
    public String showVorlesungGruppe(Model model, Annonce annonce, @RequestParam("id") Integer id) {
        model.addAttribute("vorlesungsubersicht1", vorlesungRepository.getOne(id).getTitel());
        return "annonceErstellen";
    }

    @GetMapping("/vorlesungsgruppesuchen")
    public String showVorlesungGruppeErstellen(Model model, @RequestParam("id1") String id1) {
        List<Annonce> vorliterator = annonceRepository.findAll();
        List<Annonce> vorllist = vorliterator.stream()
                .filter(vorl -> vorl.getVorlName().matches(id1))
                .collect(Collectors.toList());
        model.addAttribute("annoncen", vorllist);
        return "annonceEinsehen";
    }

    @GetMapping("/searchLecture")
    public String searchLecture(Model model, @RequestParam(required = false) String id1, @RequestParam(required = false) String id2, @RequestParam(required = false) String id3) {
        List<Vorlesung> vorliterator = vorlesungRepository.findAll();
        if(!id1.equals(Null)){List<Vorlesung> vorllist = vorliterator.stream()
                .filter(vorl -> vorl.getTitel().matches(id1))
                .collect(Collectors.toList());}
        if(!id2.equals(Null)){List<Vorlesung> vorllist = vorliterator.stream()
                .filter(vorl -> vorl.getTitel().matches(id2))
                .collect(Collectors.toList());}
        if(!id3.equals(Null)){List<Vorlesung> vorllist = vorliterator.stream()
                .filter(vorl -> vorl.getTitel().matches(id3))
                .collect(Collectors.toList());}
        model.addAttribute("annoncen", vorllist);
        return "Vorlesungsubersicht";
    }

    @PostMapping(value = "/vorlesungadd")
    public String addUser(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:vorlesungsubersichterstellen";
        }

        vorlesungRepository.save(vorlesung);
        return "vorlesungsubersichterstellen";
    }
}
