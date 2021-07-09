package com.learngrouptu.models;


import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

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
    //private SortedSet<ChatMessage>  ??


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
      //  Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC()) ? Sort.by

       /* List<ChatMessage> messagesSorted = chatroomMessages.stream().sorted().collect(Collectors.toList());
        Set<ChatMessage> sortedChatMessages = new HashSet<>();
            for( ChatMessage m: messagesSorted)
            {
                sortedChatMessages.add(m);
            }

        */
        /*
        chatroomMessages.stream().sorted

        Set<ChatMessage> chatMessages = new HashSet<ChatMessage>(chatroomMessages);
        return chatMessages;
*/
       // return sortedChatMessages;
        return chatroomMessages;
    }

    public void setChatroomMessages(Set<ChatMessage> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }
}
