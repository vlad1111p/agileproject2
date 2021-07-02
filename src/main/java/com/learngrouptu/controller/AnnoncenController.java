package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.DTO.AnnonceDTO;
import com.learngrouptu.models.AnnonceRepository;
import com.learngrouptu.models.User;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.net.http.HttpClient;
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
    public ModelAndView showAnnonceEinsehen(Model model, @RequestParam(name = "annonceCreated", required = false) Boolean created){
        // TODO Refactoring und so, dass es so funktioniert wie ich will!!!
        model.addAttribute("annoncen", annonceRepository.findAll());
        if (created) {
            model.addAttribute("annonceCreated", true);
        }
        ModelAndView mav = new ModelAndView();
        mav.addAllObjects(model.asMap());
        mav.setViewName("annonceEinsehen");
        return mav;
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
    public ModelAndView addAnnonce(@Valid Annonce annonce, BindingResult result, Model model, ModelAndView mav) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:annonceErstellen");
        }

        insertAnnonceIntoRepo(annonce);

        model.addAttribute("annonceCreated", true);

        return new ModelAndView("redirect:annonceEinsehen", model.asMap());
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
