package com.learngrouptu.controller;

import com.learngrouptu.DTO.VorlesungDTO;
import com.learngrouptu.models.*;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VorlesungController {

    private final VorlesungRepository vorlesungRepository;
    private final AnnonceRepository annonceRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserService userService;

    @Autowired
    public VorlesungController(VorlesungRepository vorlesungRepository, AnnonceRepository annonceRepository) {
        this.vorlesungRepository = vorlesungRepository;
        this.annonceRepository = annonceRepository;
    }



    @GetMapping("/vorlesungEinsehen")
    public ModelAndView showVorlesungEinsehen(Model model,
                                              @RequestParam(value = "vorlesungCreated", required = false) Boolean created,
                                              @RequestParam(value = "vorlesungRequested", required = false) Boolean requested,
                                              VorlesungDTO vorlesungDTO) {
        ModelAndView mav = new ModelAndView("vorlesungEinsehen");

        if (created != null && created) {
            mav.addObject("vorlesungCreated", true);
        }

        if (requested != null && requested) {
            mav.addObject("vorlesungRequested", true);
        }

        mav.addObject("vorlesungen", vorlesungRepository.findAll());
        return mav;
    }


    @GetMapping("/admin/vorlesungErstellen")
    public String showVorlesungErstellen(Vorlesung vorlesung, Model model,
                                         @RequestParam(name = "cpKeineZahl", required = false) Boolean cpKeinZahl,
                                         @RequestParam(name="vorlesungDoppelt",required = false) Boolean duplVorl) {
        if (cpKeinZahl != null && cpKeinZahl) {
            model.addAttribute("cpKeineZahl", true);
        }

        if (duplVorl != null && duplVorl) {
            model.addAttribute("vorlesungDoppelt", true);
        }

        return "vorlesungErstellen";
    }

    @GetMapping("/vorlesungsgruppeerstellen")
    public String createAnnonceFromVorlesung(Model model, Annonce annonce, @RequestParam("id") Integer id) {
        model.addAttribute("titelFromVorlesung", vorlesungRepository.getOne(id).getTitel());
        return "annonceErstellen";
    }

    @GetMapping("/vorlesungsgruppesuchen")
    public String searchAnnoncenFromVorlesung(Model model, @RequestParam("titel") String titel) {
        List<Annonce> filteredAnnoncen = annonceRepository.findAllByVorlName(titel);
        model.addAttribute("annoncen", filteredAnnoncen);
        return "annonceEinsehen";
    }

    @GetMapping("/searchLecture")
    public String searchLecture(Model model,
                                @RequestParam(name="titel",required = false) String vorlName,
                                @RequestParam(name="kursnummer",required = false) String kursNr,
                                @RequestParam(name="studiengang",required = false) String studiengang,
                                VorlesungDTO vorlesungDTO, BindingResult result) {

        List<Vorlesung> vorlList = vorlesungRepository.findAll();

        if(vorlName != null && !vorlName.equals("")) {
            vorlList = filterByTitelIgnoringCases(vorlName, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }
        }
        if(kursNr != null && !kursNr.equals("")){
            vorlList = filterByKursNr(kursNr, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }}
        if(studiengang != null && !studiengang.equals("")){
            vorlList = filterByStudiengang(studiengang, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }}

            model.addAttribute("vorlesungen", vorlList);
            model.addAttribute("vorlesungDTO", vorlesungDTO);
        return "vorlesungEinsehen";
    }

    @PostMapping(value = "/admin/vorlesungErstellen")
    public ModelAndView addVorlesung(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            if (result.getFieldError().getField().equals("cp")) {
                mav.setViewName("vorlesungErstellen");
                mav.addObject("cpKeineZahl", true);
                return mav;
            }
            else {
                mav.setViewName("vorlesungErstellen");
                mav.addObject("error", true);
                return mav;
            }
        }
        else {
            try {
                checkForDuplicateKursNrAndStudiengang(vorlesung); // can throw VorlesungDuplicateException
                vorlesungRepository.save(vorlesung);
                mav.setViewName("redirect:vorlesungEinsehen");
                mav.addObject("vorlesungCreated", true);
                return mav;
            }
            catch (VorlesungDuplicateException e) {
                mav.setViewName("vorlesungErstellen");
                mav.addObject("vorlesungDoppelt", true);
                return mav;
            }

        }
    }

    @PostMapping(value = "/vorlesungBeantragen")
    public ModelAndView requestVorlesung(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            if (result.getFieldError().getField().equals("cp")) {
                mav.setViewName("vorlesungBeantragen");
                mav.addObject("cpKeineZahl", true);
                return mav;
            }
            else {
                mav.setViewName("vorlesungBeantragen");
                mav.addObject("error", true);
                return mav;
            }
        }
        else {
            try {
                checkForDuplicateKursNrAndStudiengang(vorlesung); // can throw VorlesungDuplicateException
                sendRequestEmail(vorlesung);
                mav.setViewName("redirect:vorlesungEinsehen");
                mav.addObject("vorlesungRequested", true);
                return mav;
            }
            catch (VorlesungDuplicateException e) {
                mav.setViewName("vorlesungBeantragen");
                mav.addObject("vorlesungDoppelt", true);
                return mav;
            }

        }
    }

    @GetMapping("/vorlesungBeantragen")
    public String showVorlesungBeantragen(Vorlesung vorlesung, Model model) {
        return "vorlesungBeantragen";
    }

    private List<Vorlesung> filterByStudiengang(String studiengang, List<Vorlesung> vorlList) {
        vorlList = vorlList.stream()
                .filter(vorl -> vorl.getStudiengang().contains(studiengang))
                .collect(Collectors.toList());
        return vorlList;
    }

    private void checkForDuplicateKursNrAndStudiengang(@Valid Vorlesung vorlesung) throws VorlesungDuplicateException {
        List<Vorlesung> vorlesungenWithSameKursNr = vorlesungRepository.findVorlesungsByKursnr(vorlesung.getKursnr());
        List<String> vorl_studiengaenge = vorlesungenWithSameKursNr.stream()
                .map(vorl -> vorl.getStudiengang())
                .collect(Collectors.toList());
        if (!vorlesungenWithSameKursNr.isEmpty() && vorl_studiengaenge.contains(vorlesung.getStudiengang())) {
            throw new VorlesungDuplicateException();
        }
    }

    private List<Vorlesung> filterByKursNr(String kursNr, List<Vorlesung> vorlList) {
        vorlList = vorlList.stream()
                .filter(vorl -> vorl.getKursnr().contains(kursNr))
                .collect(Collectors.toList());
        return vorlList;
    }
    private List<Vorlesung> filterByTitelIgnoringCases(String vorlName, List<Vorlesung> vorlList) {
        vorlList = vorlList.stream()
                .filter(vorl -> vorl.getTitel().toLowerCase().contains(vorlName.toLowerCase()))
                .collect(Collectors.toList());
        return vorlList;
    }

    private void sendRequestEmail(Vorlesung vorl) {
        String from = "vladmihalea1111p@gmail.com";
        String to = "l_cezanne19@cs.uni-kl.de";
        User currUser = userService.getCurrentUser();
        String username = currUser.getUsername();
        //String to = "pam2-2021-LearngroupTU@cs.uni-kl.de"; TODO für Livebetrieb sollte diese Mailadresse genutzt werden
        String messageTextHead = "Es wurde eine neue Vorlesung von " + username + " beantragt: \n";
        String messageTextBody = "Vorlesungsname: " + vorl.getTitel() + "\n" +
                "Vorlesungsnummer: " + vorl.getKursnr() + "\n" +
                "Studiengang: " + vorl.getStudiengang() + "\n" +
                "CP: " + vorl.getCp();
        String messageText = messageTextHead + messageTextBody;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Vorlesungs Request von " + username + " zu " + vorl.getTitel());
        message.setText(messageText);

        mailSender.send(message);
    }

    private class VorlesungDuplicateException extends Throwable {

    }

}

