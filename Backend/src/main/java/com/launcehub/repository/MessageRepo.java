package com.launcehub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.Message;
import com.launcehub.Model.Users;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findBySender(Users sender);
    List<Message> findByProjectId(Long projectId);
    
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(@Param("user1") Users user1, @Param("user2") Users user2);
}
