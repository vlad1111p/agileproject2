package com.learngrouptu.controller;

import com.learngrouptu.models.User;
import com.learngrouptu.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserRepository userRepository) { this.userRepository = userRepository;}

    @RequestMapping("/register")
    public String showRegister(User user, Model model) {
        return "register";
    }

    @GetMapping("/register")
    public String showRegisterGet(User user, Model model) {
        return "register";
    }

    @PostMapping("/perform_register")
    public ModelAndView addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            System.out.println("Something went wrong");
            return new ModelAndView("redirect:register");
        }

        try {
            if (userRepository.findUserByUsername(user.getUsername()) != null) {
                throw new UserDuplicateException();
            }
           else if (userRepository.findUserByEmail(user.getEmail()) != null) {
                throw new EmailDuplicateException();
            }
           else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                //Test for turning off password encoding by commenting encryption function
                userRepository.save(user);
                return new ModelAndView("redirect:login.html");
            }
        } catch (UserDuplicateException e) {
            ModelAndView mav = new ModelAndView("redirect:register");
            mav.addObject("usererror", true);
            return mav;

            //return "redirect:/register?error=usererror";

        } catch (EmailDuplicateException e) {
            ModelAndView mav = new ModelAndView("redirect:register");
            mav.addObject("emailerror", true);
            return mav;
        }
    }

    private class UserDuplicateException extends Throwable {
    }

    private class EmailDuplicateException extends Throwable {
    }
}
