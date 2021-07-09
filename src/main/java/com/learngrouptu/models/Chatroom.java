package com.learngrouptu.models;


import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Chatroom {

    @Id
    @GeneratedValue
    private Integer chatroomId;
    private String sender;
    private String recipient;
    private String vorlesung;

    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)

    private Set<ChatMessage> chatroomMessages;


    public Chatroom(Integer chatroomId, String sender, String recipient) {
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.recipient = recipient;
        this.vorlesung = vorlesung;
    }

    public Chatroom() {
    }

    public Integer getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Integer chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Set<ChatMessage> getChatroomMessages() {
        return chatroomMessages;
    }

    public void setChatroomMessages(Set<ChatMessage> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }

    public Set<ChatMessage> sortChatroomMessages(Set<ChatMessage> chatroomMessages){
        Set chatroomMessagesUnordered = chatroomMessages;

        Set<ChatMessage> chatroomMessagesOrdered = new TreeSet<ChatMessage>(chatroomMessagesUnordered);
        chatroomMessages = chatroomMessagesOrdered;

        System.out.println(chatroomMessages);
        return chatroomMessages;
    }

    public String getVorlesung() {
        return vorlesung;
    }

    public void setVorlesung(String vorlesung) {
        this.vorlesung = vorlesung;
    }
}
