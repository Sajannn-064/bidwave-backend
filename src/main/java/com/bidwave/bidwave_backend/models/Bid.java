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
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many bids can belong to one auction
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    // Many bids can be placed by one user
    @ManyToOne
    @JoinColumn(name = "bidder_id", nullable = false)
    private User bidder;

    // Exact bid amount — BigDecimal for precision
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // Timestamp of when the bid was placed
    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime;
}