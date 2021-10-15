package com.learngrouptu.models;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Chatroom {

    @Id
    @GeneratedValue
    private Integer chatroomId;
    private String sender;
    private String recipient;
    private String title;

    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private Set<ChatMessage> chatroomMessages;

    public enum Status {
        ALIVE,
        DEAD_FOR_SENDER,
        DEAD_FOR_RECIPIENT,
        DEAD_FOR_ALL
    }

    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public List<ChatMessage> sortChatroomMessages() {
        List<ChatMessage> orderedMessages = new ArrayList<ChatMessage>();
        orderedMessages.addAll(this.getChatroomMessages());
        orderedMessages.sort(Comparator.comparing(ChatMessage::getTimestamp));
        return orderedMessages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.sql.Date getLastActivity() throws NoActivityYetException {
        if (this.getChatroomMessages().isEmpty()) {
            throw new NoActivityYetException();
        }
        List<ChatMessage> orderedMessages = sortChatroomMessages();
        ChatMessage lastMessage = orderedMessages.get(orderedMessages.size()-1);
        return lastMessage.getTimestamp();
    }

    public boolean chatroomTooOld() {
        long millis=System.currentTimeMillis();
        LocalDate now = (new java.sql.Date(millis)).toLocalDate();
        try {
            LocalDate then = getLastActivity().toLocalDate();
            System.out.println("Then:" + java.sql.Date.valueOf(then.minusDays(1)).getTime());
            System.out.println("now.minusDays:" + java.sql.Date.valueOf(now.minusDays(30)).getTime());
            return now.minusDays(30)
                    .isAfter(then);
        }
        catch (NoActivityYetException e) {
            return false;
        }
    }

    private class NoActivityYetException extends Throwable {
    }
}
