package com.bidwave.bidwave_backend.repositories;

import com.bidwave.bidwave_backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// @Repository marks this as a Spring Bean managed by Spring
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Spring auto-generates: SELECT * FROM notifications WHERE user_id = ?
    List<Notification> findByUser_Id(Long userId);

    // Spring auto-generates: SELECT * FROM notifications WHERE user_id = ? AND is_read = ?
    List<Notification> findByUser_IdAndIsRead(Long userId, Boolean isRead);
}