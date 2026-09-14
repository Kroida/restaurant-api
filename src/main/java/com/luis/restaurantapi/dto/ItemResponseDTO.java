package com.luis.restaurantapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponseDTO(Long id, String name, String description, Integer quantity, BigDecimal price, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public ItemResponseDTO(Item item) {
        this(item.getId(), item.getName(), item.getDescription(), item.getQuantity(), item.getPrice(), item.getImageUrl(), item.getCreatedAt(), item.getUpdatedAt());
    }
}