package com.learngrouptu.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;


@Entity
public class ChatMessage implements Comparable<ChatMessage>{

    @Id
    @GeneratedValue
    private Integer chatMessageId;
    private MessageType type;
    private String content;
    private String sender;
    private Integer chatid;
    @CreationTimestamp
    private Date timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chatid", nullable = false, insertable = false, updatable = false)
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

    public Integer getChatid() {
        return chatid;
    }

    public void setChatid(Integer chatid) {
        this.chatid = chatid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return (this.chatMessageId - o.chatMessageId);
    }
}
