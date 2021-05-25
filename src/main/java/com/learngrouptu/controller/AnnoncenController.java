package com.learngrouptu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AnnoncenController {

    @RequestMapping(value = "/annonceErstellen", method = RequestMethod.GET)
    public String showAnnonceErstellen(){return "annonceErstellen";}

    @RequestMapping(value = "/annonceEinsehen", method = RequestMethod.GET)
    public String showAnnonceEinsehen(){return "annonceEinsehen";}
}
