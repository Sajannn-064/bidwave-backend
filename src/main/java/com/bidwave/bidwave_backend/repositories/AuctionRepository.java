package com.bidwave.bidwave_backend.repositories;

import com.bidwave.bidwave_backend.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// @Repository marks this as a Spring Bean managed by Spring
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // Spring auto-generates: SELECT * FROM auctions WHERE seller_id = ?
    List<Auction> findBySeller_Id(Long sellerId);

    // Spring auto-generates: SELECT * FROM auctions WHERE status = ?
    List<Auction> findByStatus(String status);

    // Spring auto-generates: SELECT * FROM auctions WHERE item_id = ?
    List<Auction> findByItem_Id(Long itemId);
}