package com.learngrouptu.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue
    private Integer chatMessageId;
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

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
}
