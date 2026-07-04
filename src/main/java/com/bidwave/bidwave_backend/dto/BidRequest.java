package com.bidwave.bidwave_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// DTO — carries only the fields client sends when placing a bid
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {

    private Long auctionId;
    private Long bidderId;
    private BigDecimal amount;
}