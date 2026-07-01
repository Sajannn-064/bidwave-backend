package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.Notification;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.NotificationRepository;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// @Service marks this as a Spring Bean in the Service layer
@Service
@RequiredArgsConstructor
public class NotificationService {

    // Spring injects these automatically via constructor injection
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // Create and save a new notification for a user
    @Transactional
    public Notification createNotification(Long userId, String message, String type) {

        // fetch user — throw exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // create notification — isRead defaults to false
        Notification notification = new Notification(
                null,
                user,
                message,
                type,
                false,
                LocalDateTime.now()
        );

        return notificationRepository.save(notification);
    }

    // fetch all notifications for a user
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    // fetch only unread notifications for a user
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUser_IdAndIsRead(userId, false);
    }

    // mark a single notification as read
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // update isRead flag and save
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    // mark all notifications as read for a user
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository
                .findByUser_IdAndIsRead(userId, false);

        // loop through all unread and mark each as read
        unread.forEach(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }
}