package com.bidwave.bidwave_backend.services;

public package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.Auction;
import com.bidwave.bidwave_backend.repositories.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;

    // runs every 60 seconds — checks for ACTIVE auctions whose end_time has passed
    @Scheduled(fixedRate = 60000)
    public void expireOldAuctions() {

        List<Auction> activeAuctions = auctionRepository.findByStatus("ACTIVE");
        LocalDateTime now = LocalDateTime.now();

        for (Auction auction : activeAuctions) {
            if (auction.getEndTime().isBefore(now)) {
                auction.setStatus("CLOSED");
                auctionRepository.save(auction);
            }
        }
    }
} {
    
}
