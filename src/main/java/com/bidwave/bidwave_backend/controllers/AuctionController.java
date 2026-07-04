package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.dto.AuctionRequest;
import com.bidwave.bidwave_backend.models.Auction;
import com.bidwave.bidwave_backend.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController — handles HTTP requests and converts responses to JSON
// @RequestMapping — all endpoints start with /api/auctions
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    // Spring injects AuctionService automatically via constructor injection
    private final AuctionService auctionService;

    // handles POST /api/auctions
    // creates a new auction
    @PostMapping
    public ResponseEntity<?> createAuction(@RequestBody AuctionRequest request) {
        try {
            Auction auction = auctionService.createAuction(
                    request.getItemId(),
                    request.getSellerId(),
                    request.getStartPrice(),
                    request.getStartTime(),
                    request.getEndTime()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(auction);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // handles GET /api/auctions/active
    // returns all active auctions — used for homepage
    @GetMapping("/active")
    public ResponseEntity<List<Auction>> getActiveAuctions() {
        List<Auction> auctions = auctionService.getActiveAuctions();
        return ResponseEntity.ok(auctions);
    }

    // handles GET /api/auctions/{id}
    // returns one auction by its id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionById(@PathVariable Long id) {
        try {
            Auction auction = auctionService.getAuctionById(id);
            return ResponseEntity.ok(auction);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // handles GET /api/auctions/seller/{sellerId}
    // returns all auctions created by a specific seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Auction>> getAuctionsBySeller(@PathVariable Long sellerId) {
        List<Auction> auctions = auctionService.getAuctionsBySeller(sellerId);
        return ResponseEntity.ok(auctions);
    }

    // handles PUT /api/auctions/{id}/status
    // updates auction status — PENDING → ACTIVE → CLOSED
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAuctionStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Auction auction = auctionService.updateAuctionStatus(id, status);
            return ResponseEntity.ok(auction);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}