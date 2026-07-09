package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.dto.ItemRequest;
import com.bidwave.bidwave_backend.models.Item;
import com.bidwave.bidwave_backend.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController — handles HTTP requests and converts responses to JSON
// @RequestMapping — all endpoints start with /api/items
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // handles POST /api/items
    // creates a new item — requires authentication (falls under anyRequest().authenticated())
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemRequest request) {
        try {
            Item item = itemService.createItem(
                    request.getName(),
                    request.getDescription(),
                    request.getImageUrl(),
                    request.getSellerId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(item);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // handles GET /api/items/seller/{sellerId}
    // returns all items listed by a specific seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Item>> getItemsBySeller(@PathVariable Long sellerId) {
        List<Item> items = itemService.getItemsBySeller(sellerId);
        return ResponseEntity.ok(items);
    }
}