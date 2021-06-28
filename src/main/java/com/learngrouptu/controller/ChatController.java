package com.learngrouptu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ChatController {


    @Autowired ChatroomRepository chatroomRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @GetMapping("/meineChats")
    public String showMyChats(Map<String, Object> model){
        User user = userService.getCurrentUser();
        Set<Chatroom> erstellteChats = chatroomRepository.findAllBySender(user.getUsername());
        Set<Chatroom> empfangeneChats = chatroomRepository.findAllByRecipient(user.getUsername());
        model.put("erstellteChats", erstellteChats);
        model.put("empfangeneChats", empfangeneChats);
        return "meineChats";}

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chatnew";}

    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model) {

        User user = userRepository.findByUserAnnoncen_AnnonceId(id);
        System.out.println(user);

        Chatroom chatroom = new Chatroom();
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        model.addAttribute("chatroom", chatroom);
        chatroomRepository.save(chatroom);

        return showChat();
    }

    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/channel/{roomId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat/{roomId}/addUser")
    @SendTo("/channel/{roomId}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
