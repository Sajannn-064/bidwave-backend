package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.Item;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.ItemRepository;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// @Service marks this as a Spring Bean in the Service layer
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Item createItem(String name, String description, String imageUrl, Long sellerId) {

        // fetch seller — throw exception if not found
        // prevents creating an item pointing to a non-existent user
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // build the item — id null, JPA generates it on save
        Item item = new Item(null, name, description, imageUrl, seller);

        return itemRepository.save(item);
    }

    // fetch all items listed by a specific seller
    public List<Item> getItemsBySeller(Long sellerId) {
        return itemRepository.findBySeller_Id(sellerId);
    }
}