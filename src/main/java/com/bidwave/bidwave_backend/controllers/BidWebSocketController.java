package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.dto.BidRequest;
import com.bidwave.bidwave_backend.models.Bid;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import com.bidwave.bidwave_backend.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

// @Controller — plain controller, not @RestController, since nothing returns over HTTP
// @RequiredArgsConstructor — Lombok generates constructor for final fields
@Controller
@RequiredArgsConstructor
public class BidWebSocketController {

    // reuse existing service logic — same rules as the REST bid path
    private final BidService bidService;

    // needed to resolve the verified username into an actual User entity/ID
    private final UserRepository userRepository;

    // used to broadcast the update to everyone watching this auction
    private final SimpMessagingTemplate messagingTemplate;

    // fires when a client sends a STOMP message to /app/bid.place
    @MessageMapping("/bid.place")
    public void placeBid(@Payload BidRequest bidRequest, Principal principal) {

        // get the verified username from the authenticated STOMP session
        String username = principal.getName();

        // look up the real User entity — never trust a client-supplied bidderId
        User bidder = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // delegate to existing service — same validation rules as REST path
        Bid savedBid = bidService.placeBid(
                bidRequest.getAuctionId(),
                bidder.getId(),
                bidRequest.getAmount()
        );

        // broadcast the new bid to everyone subscribed to this auction's topic
        String destination = "/topic/auction." + savedBid.getAuction().getId();
        messagingTemplate.convertAndSend(destination, savedBid);
    }
}