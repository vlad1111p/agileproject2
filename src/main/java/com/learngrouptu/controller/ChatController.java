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
import org.sqlite.SQLiteException;

import java.util.*;
import java.util.stream.Collectors;

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
        String currUsername = user.getUsername();
        Set<Chatroom> alleChats = chatroomRepository.findAllBySenderOrRecipient(user.getUsername(), user.getUsername());
        List<Chatroom> alleChatsModel = alleChats.stream().collect(Collectors.toList());
        HashMap map = new HashMap();
        Chatroom.Status chatroomStatus;

        for (Chatroom chatroom : alleChats){
            chatroomStatus = chatroom.getStatus();
            if (chatroom.chatroomTooOld()) { // checks if Chatroom is too old
                chatroomRepository.deleteChatroomByChatroomId(chatroom.getChatroomId());
                alleChatsModel.remove(chatroom);
            }
            else if (wasChatroomDeletedByUser(currUsername, chatroomStatus, chatroom)) {
                alleChatsModel.remove(chatroom);
            }
            else {
                // TODO is this necessary?
                map.put(new Integer(chatroom.getChatroomId()), chatroom.sortChatroomMessages(chatroom.getChatroomMessages()));
            }
        }
        model.put("alleChats", alleChatsModel);
        model.put("map", map);
        System.out.println(map);

        model.put("userName", user.getUsername());


        return "meineChats";}

    private boolean wasChatroomDeletedByUser(String currUsername, Chatroom.Status chatroomStatus, Chatroom chatroom) {
        return chatroomStatus.equals(Chatroom.Status.DEAD_FOR_ALL) ||
                 chatroomStatus.equals(Chatroom.Status.DEAD_FOR_SENDER)
                         && chatroom.getSender().equals(currUsername) ||
                 chatroomStatus.equals(Chatroom.Status.DEAD_FOR_RECIPIENT)
                         && chatroom.getRecipient().equals(currUsername);
    }


    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String showChat(){return "chatnew";}


    @PostMapping("/startChat")
    public String startChat(@RequestParam Integer id, Model model) {

        User recipient = userRepository.findByUserAnnoncen_AnnonceId(id);
        User sender = userService.getCurrentUser();
        Annonce annonce = annonceRepository.findOneByAnnonceId(id);
        String title = annonce.getVorlName();
        System.out.println(recipient.getUsername());

        List<Chatroom> existingChatroom = chatroomRepository
                .findChatroomsBySenderAndRecipientAndAndTitle(sender.getUsername(), recipient.getUsername(), title);

        if (existingChatroom.isEmpty()) {
            Chatroom chatroom = new Chatroom();
            configureChatroom(recipient, annonce, chatroom);
            model.addAttribute("chatroom", chatroom);
            System.out.println(chatroom.getChatroomId());
            System.out.println(chatroom.getSender());
            System.out.println(chatroom.getTitle());
            chatroom = chatroomRepository.save(chatroom);
            ChatMessage systemMessage = new ChatMessage();
            String content = "Dieser Chat wird nach 30 Tagen Inaktivität gelöscht!";
            configureSystemMessage(content, systemMessage);
            sendMessage(chatroom.getChatroomId().toString(), systemMessage);
        }

        return "redirect:meineChats";
    }

    private void configureChatroom(User user, Annonce annonce, Chatroom chatroom) {
        chatroom.setRecipient(user.getUsername());
        chatroom.setSender(userService.getCurrentUser().getUsername());
        chatroom.setTitle(annonce.getVorlName());
        chatroom.setChatroomMessages(new HashSet<ChatMessage>());
        chatroom.setStatus(Chatroom.Status.ALIVE);
    }

    @PostMapping("/deleteChat")
    public String deleteChat(@RequestParam Integer chatroomId, Model model) {
        Chatroom chatroom = chatroomRepository.findChatroomByChatroomId(chatroomId);
        String currUsername = userService.getCurrentUser().getUsername();
        setNewStatusForChatroom(chatroom, currUsername);
        chatroom = chatroomRepository.save(chatroom);
        ChatMessage systemMessage = new ChatMessage();
        String content = "Dein Gegenüber hat diesen Chat gelöscht, weitere Nachrichten können nicht mehr gelesen werden";
        configureSystemMessage(content, systemMessage);
        sendMessage(chatroom.getChatroomId().toString(), systemMessage);
        return "redirect:meineChats";
    }

    private void configureSystemMessage(String content, ChatMessage systemMessage) {
        systemMessage.setContent(content);
        systemMessage.setSender("System");
        systemMessage.setType(ChatMessage.MessageType.CHAT);
    }

    private void setNewStatusForChatroom(Chatroom chatroom, String currUsername) {
        if (!chatroom.getStatus().equals(Chatroom.Status.ALIVE)) { // if already deleted by one party
            chatroom.setStatus(Chatroom.Status.DEAD_FOR_ALL);
        }
        else if (currUsername.equals(chatroom.getSender())) { // if deleter is Sender
            chatroom.setStatus(Chatroom.Status.DEAD_FOR_SENDER);
        }
        else { // if deleter is recipient
            chatroom.setStatus(Chatroom.Status.DEAD_FOR_RECIPIENT);
        }
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
