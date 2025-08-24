package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.Message;
import com.launcehub.Model.Users;
import com.launcehub.repository.MessageRepo;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepo.findById(id);
    }

    public Message saveMessage(Message message) {
        return messageRepo.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepo.deleteById(id);
    }

    public List<Message> getMessagesBySender(Users sender) {
        return messageRepo.findBySender(sender);
    }

    public List<Message> getMessagesByProjectId(Long projectId) {
        return messageRepo.findByProjectId(projectId);
    }

    public List<Message> getConversationBetweenUsers(Users user1, Users user2) {
        return messageRepo.findConversationBetweenUsers(user1, user2);
    }
}
