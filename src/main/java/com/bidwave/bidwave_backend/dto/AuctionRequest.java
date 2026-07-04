package com.bidwave.bidwave_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO — carries only the fields client sends when creating an auction
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRequest {

    private Long itemId;
    private Long sellerId;
    private BigDecimal startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}