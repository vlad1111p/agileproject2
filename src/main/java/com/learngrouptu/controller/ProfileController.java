package com.learngrouptu.controller;

import com.learngrouptu.DTO.UserDTO;
import com.learngrouptu.Exceptions.AbschlussNotAllowedException;
import com.learngrouptu.models.User;
import com.learngrouptu.models.UserRepository;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    private final UserRepository userRepository;

    @Autowired
    UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @GetMapping("/passwordChange")
    public String changePassword(Model model, User user) {
        model.addAttribute("user", userService.getCurrentUser());
        return "passwordChange";
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
    @PostMapping("/change_password")
    public String editPassword(Model model, UserDTO user,@RequestParam(name="altesPassword",required = false) String altesPassword,
                               @RequestParam(name="altesPassword1",required = false) String altesPassword1,@RequestParam(name="neuesPassword",required = false) String neuesPassword,@RequestParam(name="neuesPassword1",required = false) String neuesPassword1)  {

        System.out.println(altesPassword);
        User currUser = userService.getCurrentUser();
        String userCurrentPassword = currUser.getPassword();

        String userInputPasswordEncrypted=bCryptPasswordEncoder.encode(altesPassword);
        if(altesPassword.equals(altesPassword1) ) {


            if(!neuesPassword.equals(altesPassword)){

                if( neuesPassword.equals(neuesPassword1)){

                    if( userCurrentPassword.equals(userInputPasswordEncrypted)) {


                        String encryptedNeuesPassword = bCryptPasswordEncoder.encode(neuesPassword);
                        currUser.setPassword(encryptedNeuesPassword);
                        userRepository.save(currUser);
                        return "redirect:profilEinstellen";

                    }
                }


            }







        }
        return "passwordChange";

    }
}
