package com.learngrouptu.controller;

import com.learngrouptu.models.*;
import com.learngrouptu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ChatController {


    @Autowired ChatroomRepository chatroomRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired ChatMessageRepository chatMessageRepository;
    @Autowired AnnonceRepository annonceRepository;

    @GetMapping("/meineChats")
    @Transactional
    public String showMyChats(Map<String, Object> model){
        User user = userService.getCurrentUser();
        Set<Chatroom> alleChats = chatroomRepository.findAllBySenderOrRecipient(user.getUsername(), user.getUsername());

        HashMap map = new HashMap();

        for (Chatroom chatroom : alleChats){
            if (chatroom.chatroomTooOld()) { // checks if Chatroom is too old
                chatroomRepository.deleteChatroomByChatroomId(chatroom.getChatroomId());
                alleChats.remove(chatroom);
            }
            else {
                // TODO is this necessary?
                map.put(new Integer(chatroom.getChatroomId()), chatroom.sortChatroomMessages(chatroom.getChatroomMessages()));
            }
        }
        model.put("alleChats", alleChats);
        model.put("map", map);
        System.out.println(map);

        model.put("userName", user.getUsername());


        return "meineChats";}


    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chatnew";}


    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model) {

        User user = userRepository.findByUserAnnoncen_AnnonceId(id);
        Annonce annonce = annonceRepository.findOneByAnnonceId(id);
        System.out.println(user.getUsername());

        Chatroom chatroom = new Chatroom();
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        chatroom.setTitle(annonce.getVorlName());
        chatroom.setChatroomMessages(new HashSet<ChatMessage>());
        model.addAttribute("chatroom", chatroom);
        System.out.println(chatroom.getChatroomId());
        System.out.println(chatroom.getSender());
        System.out.println(chatroom.getTitle());
        chatroom = chatroomRepository.save(chatroom);
        chatroom.init();
        chatroomRepository.save(chatroom);
        return "redirect:meineChats";
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


    /*@MessageMapping("/chat/{roomId}/addUser")
    @SendTo("/channel/{roomId}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }*/
}
