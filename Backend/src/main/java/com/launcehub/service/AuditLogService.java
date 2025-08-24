package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.AuditLog;
import com.launcehub.Model.Users;
import com.launcehub.repository.AuditLogRepo;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepo auditLogRepo;

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepo.findAll();
    }

    public Optional<AuditLog> getAuditLogById(Long id) {
        return auditLogRepo.findById(id);
    }

    public AuditLog saveAuditLog(AuditLog auditLog) {
        return auditLogRepo.save(auditLog);
    }

    public void deleteAuditLog(Long id) {
        auditLogRepo.deleteById(id);
    }

    public List<AuditLog> getAuditLogsByUser(Users user) {
        return auditLogRepo.findByUser(user);
    }

    public List<AuditLog> getAuditLogsByActionType(String actionType) {
        return auditLogRepo.findByAction(actionType);
    }

    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        return auditLogRepo.findByEntityType(entityType);
    }

    public List<AuditLog> getAuditLogsByUserOrderByCreatedAtDesc(Users user) {
        return auditLogRepo.findByUserOrderByTimestampDesc(user);
    }

    public List<AuditLog> getAuditLogsByEntityTypeAndEntityId(String entityType, Long entityId) {
        return auditLogRepo.findByEntityTypeAndEntityId(entityType, entityId);
    }
}
