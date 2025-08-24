package com.launcehub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.AuditLog;
import com.launcehub.Model.Users;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUser(Users user);
    List<AuditLog> findByAction(String action);
    List<AuditLog> findByEntityType(String entityType);
    
    @Query("SELECT a FROM AuditLog a WHERE a.user = :user ORDER BY a.timestamp DESC")
    List<AuditLog> findByUserOrderByTimestampDesc(@Param("user") Users user);
    
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.timestamp DESC")
    List<AuditLog> findByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Long entityId);
}
