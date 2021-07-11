package com.learngrouptu.controller;

import com.learngrouptu.Exceptions.AnnonceDoesNotExistException;
import com.learngrouptu.Exceptions.UsernameDoesNotExistException;
import com.learngrouptu.models.*;
import com.learngrouptu.DTO.AnnonceDTO;
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
    private final VorlesungRepository vorlesungRepository;

    @Autowired
    UserService userService;

    @Autowired
    public AnnoncenController(AnnonceRepository annonceRepository, VorlesungRepository vorlesungRepository) {
        this.annonceRepository = annonceRepository;
        this.vorlesungRepository = vorlesungRepository;
    }


    @GetMapping("/annonceEinsehen")
    public ModelAndView showAnnonceEinsehen(Model model,
                                            @RequestParam(name = "annonceCreated", required = false) Boolean created,
                                            @RequestParam(name = "searchVorl", required = false) String searchVorl,
                                            AnnonceDTO annonceDTO) {
        annonceDTO.setChoice("Beides");

        if (searchVorl != null && searchVorl != "") {
            annonceDTO.setVorlName(searchVorl);
            // suche nach Annonce mit gesuchtem Vorlesungsname
            return searchAnnonce(model, searchVorl, "Beides", annonceDTO, null);
        }
        List<Annonce> annonceList = annonceRepository.findAll();
        model.addAttribute("annoncen", annonceList);

        if (created != null && created) {
            model.addAttribute("annonceCreated", true);
        }

        model.addAttribute("annonceDTO", annonceDTO);
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
    public String showAnnonceErstellen(Model model, Annonce annonce,
                                       @RequestParam(name="titelFromVorlesung", required = false) String vorl,
                                       @RequestParam(name="vorlName", required = false) String titelFromSearch) {
        model.addAttribute("vorlesungen", vorlesungRepository.findAll());
        Boolean isTitelFromSearchInVorlesungen = vorlesungRepository.findAll().stream()
                .map(vorlesung -> vorlesung.getTitel())
                .collect(Collectors.toList())
                .contains(titelFromSearch);
        if ((vorl != null && vorl != "") ||
                (titelFromSearch != null && titelFromSearch != "" && isTitelFromSearchInVorlesungen)) {
            model.addAttribute("titelFromVorlesung", vorl);
        }
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
    public ModelAndView searchAnnonce(Model model, @RequestParam(name = "vorlName", required = false) String vorlName,
                                @RequestParam(name = "choice", required = false) String choice,
                                AnnonceDTO annonceDTO, BindingResult result) {

        ModelAndView mav = new ModelAndView("annonceEinsehen");
        List<Annonce> annonceList = getAnnoncesWithMatchingName(vorlName);

        if (annonceList.isEmpty()) {
            addSearchAttributesToModel(model, vorlName, choice);
            mav.addAllObjects(model.asMap());
            return mav;
        } else if (choice.equals("Beides")) {
            model.addAttribute("annoncen", annonceList);
            mav.addAllObjects(model.asMap());
            return mav;
        } else {
            annonceList = annonceList.stream()
                    .filter(annonce -> annonce.getChoice().equals(choice))
                    .collect(Collectors.toList());
            model.addAttribute("annoncen", annonceList);
            mav.addAllObjects(model.asMap());
            return mav;
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
    public ModelAndView annonceAendern(Model model, @RequestParam(name = "annonceId", required = true) Integer annonceId) throws AnnonceDoesNotExistException {
        ModelAndView mav = new ModelAndView("annonceAendern");
        Annonce annonce = annonceRepository.findAnnonceByAnnonceId(annonceId);
        if (annonce == null) {
            throw new AnnonceDoesNotExistException();
        } else {
            List<Vorlesung> vorlesungen = vorlesungRepository.findAll();
            // Hier muss gefiltert werden, da sonst die aktuelle Vorlesung nicht als Default angezeigt wird
            vorlesungen = vorlesungen.stream()
                    .filter(vorlesung -> !vorlesung.getTitel().equals(annonce.getVorlName()))
                    .collect(Collectors.toList());
            mav.addObject("vorlesungen", vorlesungen);
            mav.addObject("annonce", annonce);
            return mav;
        }
    }

    /*@GetMapping("annonceAendern")
    public ModelAndView showAnnonceAendern(Model model, @RequestParam(name = "annonceId", required = true) Integer annonceId) throws AnnonceDoesNotExistException {
        ModelAndView mav = new ModelAndView("annonceAendern");
        if (annonceRepository.findAnnonceByAnnonceId(annonceId) == null) {
            throw new AnnonceDoesNotExistException();
        } else {
            mav.addObject("annonce", annonceRepository.findAnnonceByAnnonceId(annonceId));
            return mav;
        }
    }*/

    private void configureCurrentAnnonce(Annonce annonce, Annonce currentAnnonce) {
        currentAnnonce.setVorlName(annonce.getVorlName());
        currentAnnonce.setChoice(annonce.getChoice());
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
