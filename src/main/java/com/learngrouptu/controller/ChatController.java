package com.learngrouptu.controller;

import com.learngrouptu.models.*;
import com.learngrouptu.services.AnnonceService;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ChatController {


    @Autowired ChatroomRepository chatroomRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chatnew";}

    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model){

        User user = userRepository.findByUserAnnoncen_AnnonceId(id);
        System.out.println(user);

        Chatroom chatroom = new Chatroom();
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        model.addAttribute("chatroom", chatroom);
        chatroomRepository.save(chatroom);
        //return ("chat"/*+ chatroom.getChatroomId()*/);
        return showChat();
    }

/*
    @PostMapping(value = "/chooseRecipient")
    public String openChatroom(@Valid Chatroom chatroom, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:singleChat";
        }

        User user = userService.getCurrentUser();
        chatroom.setSender(user.getUsername());
        chatroomRepository.save(chatroom);

        return showSingleChat();
    }
    }*/

    @MessageMapping("/chat.sendMessage")
    @SendTo("/channel")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/channel")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
