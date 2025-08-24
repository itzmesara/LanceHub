package com.launcehub.controller;

import com.launcehub.Model.Message;
import com.launcehub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> message = messageService.getMessageById(id);
        return message.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody Message messageDetails) {
        Optional<Message> message = messageService.getMessageById(id);
        if (message.isPresent()) {
            messageDetails.setId(id);
            return ResponseEntity.ok(messageService.saveMessage(messageDetails));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread-count")
    @PermitAll
    public ResponseEntity<Map<String, Integer>> getUnreadCount(@AuthenticationPrincipal UserDetails principal) {
        // Placeholder implementation: return 0 until read-status is implemented
        return ResponseEntity.ok(Map.of("count", 0));
    }

    @GetMapping("/conversations")
    @PermitAll
    public ResponseEntity<List<Map<String, Object>>> getConversations(@AuthenticationPrincipal UserDetails principal) {
        // Placeholder: return empty conversations list for now
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/project/{projectId}")
    @PermitAll
    public ResponseEntity<List<Message>> getMessagesByProject(@PathVariable Long projectId,
                                                              @AuthenticationPrincipal UserDetails principal) {
        // Placeholder: no filtering by principal for now
        return ResponseEntity.ok(messageService.getMessagesByProjectId(projectId));
    }
}
