package com.learngrouptu.controller;

import com.learngrouptu.DTO.RegisterDTO;
import com.learngrouptu.models.Annonce;
import com.learngrouptu.models.User;
import com.learngrouptu.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {this.userRepository = userRepository;}

    @PostMapping("/register")
    public String showRegister(User user, Model model){return "register";}

    @PostMapping("/perform_register")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "redirect:register";
        }

        try {
           if (userRepository.findByUsername(user.getUsername()) != null) {
               throw new UserDuplicateException();
           }
           else if (userRepository.findByEmail(user.getEmail()) != null) {
               throw new EmailDuplicateException();
           }
           else {
               userRepository.save(user);
               return "redirect:login.html";
           }
        }
        catch (UserDuplicateException e) {
                model.addAttribute("usererror", new Object());
                return showRegister(user, model);
        }
        catch (EmailDuplicateException e) {
            model.addAttribute("emailerror", new Object());
            return showRegister(user, model);
        }
    }

    private class UserDuplicateException extends Throwable {
    }

    private class EmailDuplicateException extends Throwable {
    }
}
