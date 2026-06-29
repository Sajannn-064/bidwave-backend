package com.bidwave.bidwave_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {

    // Primary key — PostgreSQL auto-increments this
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many auctions can be created for one item
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // Many auctions can be run by one seller (User)
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    // BigDecimal used for exact precision — never use float or double for money
    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    // updated every time a new valid bid is placed
    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    // LocalDateTime stores both date and time together
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // holds values like PENDING, ACTIVE, CLOSED
    @Column(name = "status", nullable = false)
    private String status;
}