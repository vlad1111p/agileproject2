package com.learngrouptu.models;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Chatroom {

    @Id
    @GeneratedValue
    private Integer chatroomId;
    private String sender;
    private String recipient;

    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ChatMessage> chatroomMessages;

    public Chatroom(Integer chatroomId, String sender, String recipient) {
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.recipient = recipient;
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
}
