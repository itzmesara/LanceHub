package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.Notification;
import com.launcehub.Model.Users;
import com.launcehub.repository.NotificationRepo;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepo.findById(id);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepo.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

    public List<Notification> getNotificationsByUser(Users user) {
        return notificationRepo.findByUser(user);
    }

    public List<Notification> getUnreadNotificationsByUser(Users user) {
        return notificationRepo.findByUserAndIsReadFalse(user);
    }

    public long getUnreadNotificationCount(Users user) {
        return notificationRepo.findByUserAndIsReadFalse(user).size();
    }
}
