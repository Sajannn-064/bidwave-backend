package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.dto.BidRequest;
import com.bidwave.bidwave_backend.models.Bid;
import com.bidwave.bidwave_backend.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController — handles HTTP requests and converts responses to JSON
// @RequestMapping — all endpoints start with /api/bids
@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    // Spring injects BidService automatically via constructor injection
    private final BidService bidService;

    // handles POST /api/bids
    // places a new bid on an auction
    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody BidRequest request) {
        try {
            Bid bid = bidService.placeBid(
                    request.getAuctionId(),
                    request.getBidderId(),
                    request.getAmount()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(bid);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // handles GET /api/bids/auction/{auctionId}
    // returns all bids for a specific auction
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsByAuction(@PathVariable Long auctionId) {
        List<Bid> bids = bidService.getBidsByAuction(auctionId);
        return ResponseEntity.ok(bids);
    }

    // handles GET /api/bids/bidder/{bidderId}
    // returns all bids placed by a specific user
    @GetMapping("/bidder/{bidderId}")
    public ResponseEntity<List<Bid>> getBidsByBidder(@PathVariable Long bidderId) {
        List<Bid> bids = bidService.getBidsByBidder(bidderId);
        return ResponseEntity.ok(bids);
    }

    // handles GET /api/bids/auction/{auctionId}/highest
    // returns the current highest bid for an auction
    @GetMapping("/auction/{auctionId}/highest")
    public ResponseEntity<?> getHighestBid(@PathVariable Long auctionId) {
        try {
            Bid bid = bidService.getHighestBid(auctionId);
            return ResponseEntity.ok(bid);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}