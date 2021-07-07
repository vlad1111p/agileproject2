package com.learngrouptu.controller;

import com.learngrouptu.DTO.PasswordDTO;
import com.learngrouptu.DTO.PasswordResetDTO;
import com.learngrouptu.DTO.UserDTO;
import com.learngrouptu.Exceptions.AbschlussNotAllowedException;
import com.learngrouptu.Exceptions.UsernameDoesNotExistException;
import com.learngrouptu.models.User;
import com.learngrouptu.models.UserRepository;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private JavaMailSender mailSender;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profilEinstellen")
    public String showProfile(Model model, User user) {
        model.addAttribute("userProfile", userService.getCurrentUser());
        return "profilEinstellen";
    }

    @GetMapping("profilBesuchen")
    public String visitProfile(Model model, @RequestParam(name="username" ,required = true) String username) throws UsernameDoesNotExistException {
        if (userRepository.findUserByUsername(username) == null) {
            throw new UsernameDoesNotExistException();
        }
        else {
            model.addAttribute("userProfile", userRepository.findUserByUsername(username));
            return "profilBesuchen";
        }
    }

    @GetMapping("/profileChange")
    public String changeProfile(Model model, User user) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profilBearbeiten";
    }

    @GetMapping("/passwordChange")
    public String changePassword(Model model, User user, PasswordDTO password) {
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
    public String editPassword(Model model, PasswordDTO passwordDTO, UserDTO user) {


        User currUser = userService.getCurrentUser();
        String altesPassword = passwordDTO.getAltesPassword();
        String altesPassword1 = passwordDTO.getAltesPassword1();
        String neuesPassword = passwordDTO.getNeuesPassword();
        String neuesPassword1 = passwordDTO.getNeuesPassword1();
        String userCurrentPassword = currUser.getPassword();

        Boolean userInputPasswordEncrypted = bCryptPasswordEncoder.matches(altesPassword, userCurrentPassword);
        System.out.println(userCurrentPassword + " " + userInputPasswordEncrypted + " " + altesPassword + " " + altesPassword1 + " " + neuesPassword + " " + neuesPassword1);
        if (altesPassword.equals(altesPassword1)) {

            if (neuesPassword.equals(neuesPassword1)) {

                if (!neuesPassword.equals(altesPassword)) {

                    if (userInputPasswordEncrypted) {


                        String encryptedNeuesPassword = bCryptPasswordEncoder.encode(neuesPassword);
                        currUser.setPassword(encryptedNeuesPassword);
                        userRepository.save(currUser);

                        sendConfirmationEmail(currUser);


                        return "redirect:profilEinstellen";

                    }
                } else {
                    model.addAttribute("oldpassworddoesnotmatch", true);
                    return "passwordChange";
                }

            } else {
                model.addAttribute("oldpassworddoesnotmatch", true);
                return "passwordChange";
            }
        } else {
            model.addAttribute("oldpassworddoesnotmatch", true);
            return "passwordChange";
        }

        return "passwordChange";

    }

    private void sendConfirmationEmail(User currUser) {
        String from = "vladmihalea1111p@gmail.com";
        String to = currUser.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Password change from LearngroupTU");
        message.setText("You just have successfully changed your password.");

        mailSender.send(message);
    }

    @GetMapping("/passwortVergessen")
    public String resetPassword(Model model, User user, PasswordResetDTO password) {
        model.addAttribute("user", userService.getCurrentUser());
        return "passwortVergessen";
    }


    @PostMapping("/reset_password")
    public String resetPassword(Model model, PasswordResetDTO passwordResetDTO, UserDTO user) {

        String usermail = passwordResetDTO.getUsermail();

        String from = "vladmihalea1111p@gmail.com";
        String to = usermail;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Password reset from LearngroupTU");
        message.setText("Du hast eine Anfrage geschickt, um dein Passwort zurückzusetzen. Wenn du diese Anfrage nicht gestellt hast, ignoriere diese Mail." +
                "Um dein Passwort zurückzusetzen, klicke auf den folgenden Link:" +
                "http://localhost:8080/vergessenesPasswortErsetzen?mail=" + usermail);

        mailSender.send(message);

        return "login";


    }
}
