package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.DTO.AnnonceDTO;
import com.learngrouptu.models.AnnonceRepository;
import com.learngrouptu.models.User;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AnnoncenController {

    private final AnnonceRepository annonceRepository;

    @Autowired
    UserService userService;

    @Autowired
    public AnnoncenController(AnnonceRepository annonceRepository){this.annonceRepository = annonceRepository;}



    @GetMapping("/annonceEinsehen")
    public String showAnnonceEinsehen(Model model){
        model.addAttribute("annoncen", annonceRepository.findAll());
        return "annonceEinsehen";
    }

    @GetMapping("/meineAnnoncen")
    public String showMeineAnnoncen(Map<String, Object> model){
        User user = userService.getCurrentUser();
        Set<Annonce> userAnnoncen = user.getUserAnnonces();
        model.put("userAnnoncen", userAnnoncen);
        return "meineAnnoncen";
    }

    @GetMapping("/annonceErstellen")
    public String showAnnonceErstellen(Model model, Annonce annonce){
        return "annonceErstellen";
    }

    @PostMapping("/addannonce")
    public String addAnnonce(@Valid Annonce annonce, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:annonceErstellen";
        }

        insertAnnonceIntoRepo(annonce);

        model.addAttribute("annonceCreated", true);

        return showAnnonceEinsehen(model);
    }

    private void insertAnnonceIntoRepo(Annonce annonce) {
        User user = userService.getCurrentUser();
        user.addAnnonce(annonce);
        annonceRepository.save(annonce);
    }

    @Transactional
    @PostMapping("/deleteannonce")
    public String deleteAnnonce(@RequestParam Integer id, Model model){
        annonceRepository.deleteByAnnonceId(id);
        return "redirect:meineAnnoncen";
    }



    @GetMapping("/searchAnnonce")
    public String searchAnnonce(Model model, @RequestParam(name="vorlName",required = false) String vorlName,
                                @RequestParam(name="choice",required = false) String choice) {

        List<Annonce> annonceList = getAnnoncesWithMatchingName(vorlName);

        if (annonceList.isEmpty()) {
            addSearchAttributesToModel(model, vorlName, choice);
            return "annonceEinsehen";
        }

        else if(choice.equals("Beides")) {
            model.addAttribute("annoncen", annonceList);
            return "annonceEinsehen";
        }
        else{
            annonceList = annonceList.stream()
                    .filter(annonce -> annonce.getChoice().equals(choice))
                    .collect(Collectors.toList());
            model.addAttribute("annoncen", annonceList);
            return "annonceEinsehen";
        }



    }

    private void addSearchAttributesToModel(Model model, String vorlName, String choice) {
        AnnonceDTO annonceDTO = new AnnonceDTO();
        annonceDTO.setVorlName(vorlName);
        annonceDTO.setChoice(choice);
        model.addAttribute("annonceDTO", annonceDTO);
    }

    private List<Annonce> getAnnoncesWithMatchingName(String vorlName) {
        List<Annonce> annonceList = annonceRepository.findAll();
        annonceList = annonceList.stream()
                        .filter(annonce -> annonce.getVorlName().toLowerCase().contains(vorlName.toLowerCase()))
                        .collect(Collectors.toList());
        return annonceList;
    }

}
