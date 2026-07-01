package com.bidwave.bidwave_backend.repositories;

import com.bidwave.bidwave_backend.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// @Repository marks this as a Spring Bean managed by Spring
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Spring auto-generates: SELECT * FROM items WHERE seller_id = ?
    List<Item> findBySeller_Id(Long sellerId);
}