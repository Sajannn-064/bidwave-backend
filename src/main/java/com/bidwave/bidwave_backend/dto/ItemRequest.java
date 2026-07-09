package com.bidwave.bidwave_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO — carries only the fields client sends when creating an item
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    private String name;
    private String description;
    private String imageUrl;
    private Long sellerId;
}