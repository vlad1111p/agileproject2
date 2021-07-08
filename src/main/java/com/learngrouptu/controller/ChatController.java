package com.learngrouptu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learngrouptu.models.*;
import com.learngrouptu.services.AnnonceService;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ChatController {


    @Autowired ChatroomRepository chatroomRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired ChatMessageRepository chatMessageRepository;

    @GetMapping("/meineChats")
    public String showMyChats(Map<String, Object> model){
        User user = userService.getCurrentUser();
        Set<Chatroom> alleChats = chatroomRepository.findAllBySenderOrRecipient(user.getUsername(), user.getUsername());
        model.put("alleChats", alleChats);

        HashMap map = new HashMap();

        for (Chatroom chatroom : alleChats){
            //Set<ChatMessage> chatroomMessages = chatroom.getChatroomMessages();
            map.put(new Integer(chatroom.getChatroomId()), chatroom.getChatroomMessages());
            //System.out.println(chatroomMessages);
            //model.put("chatroomMessages", chatroomMessages);
        }
        model.put("map", map);
        System.out.println(map);
        return "meineChats";}


    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chatnew";}


    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model) {

        User user = userRepository.findByUserAnnoncen_AnnonceId(id);
        System.out.println(user.getUsername());

        Chatroom chatroom = new Chatroom();
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        model.addAttribute("chatroom", chatroom);
        chatroomRepository.save(chatroom);

        return showChat();
    }


    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/channel/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {

        chatMessage.setChatid(Integer.valueOf(roomId));
        System.out.println("sender: " + chatMessage.getSender());
        System.out.println("content: " + chatMessage.getContent());
        System.out.println("chatroomid: " + chatMessage.getChatid());

        chatMessageRepository.save(chatMessage);

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
