package com.luis.restaurantapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponseDTO(Long id, String name, String description, Integer quantity, BigDecimal price, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {}