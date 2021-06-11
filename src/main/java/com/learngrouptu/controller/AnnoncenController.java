package com.learngrouptu.controller;

import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.AnnonceDTO;
import com.learngrouptu.models.AnnonceRepository;
import com.learngrouptu.models.User;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
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
    public String showAnnonceErstellen(Annonce annonce){
        return "annonceErstellen";
    }

    @PostMapping("/addannonce")
    public String addAnnonce(@Valid Annonce annonce, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:annonceErstellen";
        }

        User user = userService.getCurrentUser();
        user.addAnnonce(annonce);
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

    /*@RequestMapping("/searchAnnonce")
    @ResponseBody
    public String getUserInput(@RequestParam(name="vorlName") String vorlName, @RequestParam String choice){
        System.out.println(vorlName + choice);
        return "annonceErstellen?vorlName=vorlName&choice=choice";
        //return "vorlName:" + vorlName + "choice" + choice;
    }*/

    @GetMapping("/searchAnnonce")
    public String searchAnnonce(Model model, @RequestParam(name="vorlName",required = false) String vorlName,
                                @RequestParam(name="choice",required = false) String choice) {
        ArrayList<String> searchArray = new ArrayList<>();
        searchArray.add(vorlName);
        searchArray.add(choice);
        model.addAttribute("search", searchArray);

        List<Annonce> annonceList = annonceRepository.findAll();
        annonceList = annonceList.stream()
                        .filter(annonce -> annonce.getVorlName().toLowerCase().contains(vorlName.toLowerCase()))
                        .collect(Collectors.toList());

        if (annonceList.isEmpty()) {
            AnnonceDTO annonceDTO = new AnnonceDTO();
            annonceDTO.setVorlName(vorlName);
            annonceDTO.setChoice(choice);
            model.addAttribute("annonceDTO", annonceDTO);
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


}
