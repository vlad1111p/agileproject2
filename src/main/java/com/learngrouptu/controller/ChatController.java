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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ChatController {


    @Autowired ChatroomRepository chatroomRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chat";}

    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model){

        User user = userRepository.findByUserAnnoncen_AnnonceId(id);
        System.out.println(user);

        Chatroom chatroom = new Chatroom();
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        model.addAttribute("chatroom", chatroom);
        chatroomRepository.save(chatroom);
        return "chat";
    }
/*
    @RequestMapping(value = "/singleChat", method = RequestMethod.GET)
    public String showSingleChat(){return "singleChat";}

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

    @MessageMapping("singleChat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/singleChat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }*/

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
