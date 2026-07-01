package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.Auction;
import com.bidwave.bidwave_backend.models.Bid;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.AuctionRepository;
import com.bidwave.bidwave_backend.repositories.BidRepository;
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
public class BidService {

    // Spring injects these automatically via constructor injection
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Bid placeBid(Long auctionId, Long bidderId, BigDecimal amount) {

        // fetch auction — throw exception if not found
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // fetch bidder — throw exception if not found
        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // business rule — auction must be ACTIVE to accept bids
        if (!auction.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Auction is not active");
        }

        // business rule — auction must not be expired
        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new RuntimeException("Auction has ended");
        }

        // business rule — bid must be higher than current price
        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("Bid must be higher than current price");
        }

        // business rule — seller cannot bid on their own auction
        if (auction.getSeller().getId().equals(bidderId)) {
            throw new RuntimeException("Seller cannot bid on their own auction");
        }

        // all rules passed — create the bid
        Bid bid = new Bid(null, auction, bidder, amount, LocalDateTime.now());

        // save the bid
        Bid savedBid = bidRepository.save(bid);

        // update auction current price to this new highest bid
        auction.setCurrentPrice(amount);
        auctionRepository.save(auction);

        return savedBid;
    }

    // fetch all bids for a specific auction
    public List<Bid> getBidsByAuction(Long auctionId) {
        return bidRepository.findByAuction_Id(auctionId);
    }

    // fetch all bids placed by a specific user
    public List<Bid> getBidsByBidder(Long bidderId) {
        return bidRepository.findByBidder_Id(bidderId);
    }

    // fetch the current highest bid for an auction
    public Bid getHighestBid(Long auctionId) {
        return bidRepository.findTopByAuction_IdOrderByAmountDesc(auctionId)
                .orElseThrow(() -> new RuntimeException("No bids found for this auction"));
    }
}