package com.learngrouptu.controller;

import com.learngrouptu.DTO.UserDTO;
import com.learngrouptu.Exceptions.AbschlussNotAllowedException;
import com.learngrouptu.models.User;
import com.learngrouptu.models.UserRepository;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {
    private final UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    public ProfileController(UserRepository userRepository){this.userRepository = userRepository;}

    @GetMapping("/profilEinstellen")
    public String showProfile(Model model, User user) {
        model.addAttribute("userProfile", userService.getCurrentUser());
        return "profilEinstellen";
    }

    @GetMapping("/profileChange")
    public String changeProfile(Model model, User user) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profilBearbeiten";
    }

    @PostMapping("/change_profile")
    public String editProfile(Model model, UserDTO user) throws AbschlussNotAllowedException {
        User currUser = userService.getCurrentUser();
        String studiengang = user.getStudiengang();
        String abschluss = user.getAbschluss();
        String bio = user.getBio();


        currUser.setStudiengang(studiengang);

        currUser.setAbschluss(abschluss);

        currUser.setBio(bio);

        userRepository.save(currUser);

        return "redirect:profilEinstellen";
    }
}
