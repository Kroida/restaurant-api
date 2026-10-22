package com.luis.restaurantapi.mapper;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
import com.luis.restaurantapi.model.Item;

public class ItemMapper {
    public static Item toEntity(ItemRequestDTO dto) {
        Item item = new Item();

        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setQuantity(dto.quantity());
        item.setPrice(dto.price());
        item.setImageUrl(dto.imageUrl());

        return item;
    }

    public static ItemResponseDTO toResponseDto(Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getPrice(),
                item.getImageUrl(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
