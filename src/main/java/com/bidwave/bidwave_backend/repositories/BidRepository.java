package com.bidwave.bidwave_backend.repositories;

import com.bidwave.bidwave_backend.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// @Repository marks this as a Spring Bean managed by Spring
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    // Spring auto-generates: SELECT * FROM bids WHERE auction_id = ?
    List<Bid> findByAuction_Id(Long auctionId);

    // Spring auto-generates: SELECT * FROM bids WHERE bidder_id = ?
    List<Bid> findByBidder_Id(Long bidderId);

    // Spring auto-generates: SELECT * FROM bids WHERE auction_id = ?
    // ORDER BY amount DESC LIMIT 1
    Optional<Bid> findTopByAuction_IdOrderByAmountDesc(Long auctionId);
}