package com.learngrouptu.controller;

import com.learngrouptu.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public VorlesungController(VorlesungRepository vorlesungRepository, AnnonceRepository annonceRepository) {
        this.vorlesungRepository = vorlesungRepository;
        this.annonceRepository = annonceRepository;
    }


    @GetMapping("/vorlesungEinsehen")
    public String showVorlesungEinsehen(Model model) {
        model.addAttribute("vorlesungen", vorlesungRepository.findAll());
        return "vorlesungEinsehen";
    }


    @GetMapping("/admin/vorlesungErstellen")
    public String showVorlesungErstellen(Vorlesung vorlesung) {
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
                                @RequestParam(name="VorlName",required = false) String vorlName,
                                @RequestParam(name="kursNr",required = false) String kursNr,
                                @RequestParam(name="Studiengang",required = false) String studiengang) {

        List<Vorlesung> vorlList = vorlesungRepository.findAll();

        if(!vorlName.equals("")) {
            vorlList = filterByTitelIgnoringCases(vorlName, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }
        }
        if(!kursNr.equals("")){
            vorlList = filterByKursNr(kursNr, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }}
        if(!studiengang.equals("")){
            vorlList = filterByStudiengang(studiengang, vorlList);
            if (vorlList.isEmpty()){
                return "vorlesungEinsehen";
            }}

            model.addAttribute("vorlesungen", vorlList);
        return "vorlesungEinsehen";
    }

    private List<Vorlesung> filterByStudiengang(String studiengang, List<Vorlesung> vorlList) {
        vorlList = vorlList.stream()
                .filter(vorl -> vorl.getStudiengang().contains(studiengang))
                .collect(Collectors.toList());
        return vorlList;
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

    @PostMapping(value = "/vorlesungadd")
    public String addVorlesung(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (result.getFieldError().getField().equals("cp")) {
                model.addAttribute("cpKeineZahl", true);
                return "vorlesungErstellen";
            }
            else {
                return "redirect:vorlesungErstellen?error=true";
            }
        }
        else {
            try {
                if (vorlesungRepository.findVorlesungByKursnr(vorlesung.getKursnr()) != null) {
                    throw new VorlesungController.VorlesungDuplicateException();
                }
                vorlesungRepository.save(vorlesung);
                model.addAttribute("vorlesungCreated", true);
                return showVorlesungEinsehen(model);
            }
            catch (VorlesungDuplicateException e) {
                model.addAttribute("vorlesungDoppelt", true);
                return "vorlesungErstellen";
            }

        }
    }

    private class VorlesungDuplicateException extends Throwable {
    }

    @PostMapping(value = "/vorlesungrequest")
    public String requestVorlesung(@Valid Vorlesung vorlesung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (result.getFieldError().getField().equals("cp")) {
                model.addAttribute("cpKeineZahl", true);
                return "vorlesungErstellen";
            }
            else {
                return "redirect:vorlesungErstellen?error=true";
            }
        }
        else {
            try {
                if (vorlesungRepository.findVorlesungByKursnr(vorlesung.getKursnr()) != null) {
                    throw new VorlesungController.VorlesungDuplicateException();
                }
                sendRequestEmail(vorlesung);
                return "redirect:home";
            }
            catch (VorlesungDuplicateException e) {
                model.addAttribute("vorlesungDoppelt", true);
                return "vorlesungErstellen";
            }

        }
    }

    private void sendRequestEmail(Vorlesung vorl) {
        String from = "vladmihalea1111p@gmail.com";
        String to = "l_cezanne19@cs.uni-kl.de";
        //String to = "pam2-2021-LearngroupTU@cs.uni-kl.de";
        String messageText = "Vorlesungsname: " + vorl.getTitel() + "\n" +
                            "Vorlesungsnummer: " + vorl.getKursnr() + "\n" +
                            "Studiengang: " + vorl.getStudiengang() + "\n" +
                            "CP: " + vorl.getCp();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Vorlesungs Request");
        message.setText(messageText);

        mailSender.send(message);
    }
    @GetMapping("/vorlesungBeantragen")
    public String showVorlesungBeantragen(Vorlesung vorlesung) {
        return "vorlesungBeantragen";
    }
}
