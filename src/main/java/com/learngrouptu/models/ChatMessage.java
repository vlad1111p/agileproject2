package com.learngrouptu.models;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue
    private Integer chatMessageId;
    private MessageType type;
    private String content;
    private String sender;
    //private Integer chatid;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false, insertable = true)
    private Chatroom chatroom;

    public Integer getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(Integer chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

}
