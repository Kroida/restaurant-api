package com.luis.restaurantapi.dto;

import java.math.BigDecimal;

public record ItemRequestDTO(String name, String description, Integer quantity, BigDecimal price, String imageUrl) {
}