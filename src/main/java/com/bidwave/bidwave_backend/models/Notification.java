package com.bidwave.bidwave_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who receives this notification
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Message content of the notification
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    // Type of notification e.g. BID_PLACED, AUCTION_WON, OUTBID
    @Column(name = "type", nullable = false)
    private String type;

    // Whether the user has read this notification
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // When the notification was created
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}