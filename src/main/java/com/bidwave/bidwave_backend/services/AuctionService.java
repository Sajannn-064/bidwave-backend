package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.Auction;
import com.bidwave.bidwave_backend.models.Item;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.AuctionRepository;
import com.bidwave.bidwave_backend.repositories.ItemRepository;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// @Service marks this as a Spring Bean in the Service layer
@Service
@RequiredArgsConstructor
public class AuctionService {

    // Spring injects these automatically via constructor injection
    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // Create a new auction
    @Transactional
    public Auction createAuction(Long itemId, Long sellerId,
                                  BigDecimal startPrice, LocalDateTime startTime,
                                  LocalDateTime endTime) {

        // fetch item — throw exception if not found
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // fetch seller — throw exception if not found
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // end time must be after start time — business rule
        if (endTime.isBefore(startTime)) {
            throw new RuntimeException("End time must be after start time");
        }

        // create auction — current price starts at start price
        Auction auction = new Auction(
                null,
                item,
                seller,
                startPrice,
                startPrice, // currentPrice starts same as startPrice
                startTime,
                endTime,
                "PENDING"   // all auctions start as PENDING
        );

        return auctionRepository.save(auction);
    }

    // Fetch all active auctions — used for homepage display
    public List<Auction> getActiveAuctions() {
        return auctionRepository.findByStatus("ACTIVE");
    }

    // Fetch one auction by id — used for auction detail page
    public Auction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    // Fetch all auctions by a specific seller
    public List<Auction> getAuctionsBySeller(Long sellerId) {
        return auctionRepository.findBySeller_Id(sellerId);
    }

    // Update auction status — e.g. PENDING → ACTIVE → CLOSED
    @Transactional
    public Auction updateAuctionStatus(Long auctionId, String status) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // update status and save
        auction.setStatus(status);
        return auctionRepository.save(auction);
    }
}