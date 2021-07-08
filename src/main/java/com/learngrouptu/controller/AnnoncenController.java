package com.learngrouptu.controller;

import com.learngrouptu.Exceptions.AnnonceDoesNotExistException;
import com.learngrouptu.Exceptions.UsernameDoesNotExistException;
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
    public AnnoncenController(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }


    @GetMapping("/annonceEinsehen")
    public ModelAndView showAnnonceEinsehen(Model model,
                                            @RequestParam(name = "annonceCreated", required = false) Boolean created) {
        model.addAttribute("annoncen", annonceRepository.findAll());
        if (created != null && created) {
            model.addAttribute("annonceCreated", true);
        }
        ModelAndView mav = new ModelAndView("annonceEinsehen");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    @GetMapping("/meineAnnoncen")
    public String showMeineAnnoncen(Model model, @RequestParam(name = "annonceEdited", required = false) Boolean edited) {
        if (edited != null && edited) {
            model.addAttribute("annonceEdited", true);
        }
        User user = userService.getCurrentUser();
        Set<Annonce> userAnnoncen = user.getUserAnnonces();
        model.addAttribute("userAnnoncen", userAnnoncen);
        return "meineAnnoncen";
    }

    @GetMapping("/annonceErstellen")
    public String showAnnonceErstellen(Model model, Annonce annonce) {
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

    @Transactional
    @PostMapping("/deleteannonce")
    public String deleteAnnonce(@RequestParam Integer id, Model model) {
        annonceRepository.deleteByAnnonceId(id);
        return "redirect:meineAnnoncen";
    }

    @GetMapping("/searchAnnonce")
    public String searchAnnonce(Model model, @RequestParam(name = "vorlName", required = false) String vorlName,
                                @RequestParam(name = "choice", required = false) String choice) {

        List<Annonce> annonceList = getAnnoncesWithMatchingName(vorlName);

        if (annonceList.isEmpty()) {
            addSearchAttributesToModel(model, vorlName, choice);
            return "annonceEinsehen";
        } else if (choice.equals("Beides")) {
            model.addAttribute("annoncen", annonceList);
            return "annonceEinsehen";
        } else {
            annonceList = annonceList.stream()
                    .filter(annonce -> annonce.getChoice().equals(choice))
                    .collect(Collectors.toList());
            model.addAttribute("annoncen", annonceList);
            return "annonceEinsehen";
        }


    }

    @PostMapping("/editannonce")
    public ModelAndView editAnnonce(@Valid Annonce annonce, BindingResult result, @RequestParam(name = "annonceId") Integer annonceId, Model model) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:annonceAendern");
        }
        Annonce currentAnnonce = annonceRepository.findAnnonceByAnnonceId(annonceId);
        configureCurrentAnnonce(annonce, currentAnnonce);

        annonceRepository.save(currentAnnonce);

        model.addAttribute("annonceEdited", true);

        return new ModelAndView("redirect:meineAnnoncen", model.asMap());
    }

    @PostMapping("annonceAendern")
    public String annonceAendern(Model model, @RequestParam(name = "annonceId", required = true) Integer annonceId) throws AnnonceDoesNotExistException {
        if (annonceRepository.findAnnonceByAnnonceId(annonceId) == null) {
            throw new AnnonceDoesNotExistException();
        } else {
            model.addAttribute("annonce", annonceRepository.findAnnonceByAnnonceId(annonceId));
            return "annonceAendern";
        }
    }

    private void configureCurrentAnnonce(Annonce annonce, Annonce currentAnnonce) {
        currentAnnonce.setVorlName(annonce.getVorlName());
        currentAnnonce.setChoice(annonce.getChoice());
        currentAnnonce.setKontakt(annonce.getKontakt());
        currentAnnonce.setNachricht(annonce.getNachricht());
    }

    private void insertAnnonceIntoRepo(Annonce annonce) {
        User user = userService.getCurrentUser();
        user.addAnnonce(annonce);
        annonceRepository.save(annonce);
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
