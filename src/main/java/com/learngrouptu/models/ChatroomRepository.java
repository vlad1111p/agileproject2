package com.learngrouptu.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {

    Chatroom findChatroomByChatroomId(Integer id);
    Set<Chatroom> findAllBySender(String sender);
    Set<Chatroom> findAllByRecipient(String recipient);
    Set<Chatroom> findAllBySenderOrRecipient(String Sender, String Recipient);
    void deleteChatroomByChatroomId(Integer chatroomId);
    List<Chatroom> findChatroomsBySenderAndRecipientAndAndTitle(String sender, String recipient, String title);

}