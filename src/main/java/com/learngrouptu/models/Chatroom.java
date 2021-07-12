package com.learngrouptu.models;

import javax.persistence.*;
import java.util.*;

@Entity
public class Chatroom {

    @Id
    @GeneratedValue
    private Integer chatroomId;
    private String sender;
    private String recipient;
    private String title;

    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)

    private Set<ChatMessage> chatroomMessages;


    public Chatroom(Integer chatroomId, String sender, String recipient) {
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.recipient = recipient;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
