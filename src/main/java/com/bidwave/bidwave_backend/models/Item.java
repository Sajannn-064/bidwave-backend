package com.bidwave.bidwave_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    // Primary key — PostgreSQL auto-increments this
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name of the item being auctioned
    @Column(name = "name", nullable = false)
    private String name;

    // TEXT type used instead of VARCHAR to allow long descriptions
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // optional URL pointing to item image
    @Column(name = "image_url")
    private String imageUrl;

    // Many items can be listed by one seller (User)
    // seller_id is the foreign key column in the items table
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
}