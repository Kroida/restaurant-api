package com.luis.restaurantapi.service;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
import com.luis.restaurantapi.exception.ItemNotFoundException;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public Item create(ItemRequestDTO itemRequestDTO) {
        Item item = new Item(itemRequestDTO);
        repository.save(item);
        return item;
    }

    public List<ItemResponseDTO> getAll() {
        List<ItemResponseDTO> itemList = repository.findAll().stream().map(ItemResponseDTO::new).toList();
        return itemList;
    }

    public Item getById(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));
        return item;
    }

    public Item update(Long id, ItemRequestDTO itemRequestDTO) {
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));

        item.setName(itemRequestDTO.name());
        item.setDescription(itemRequestDTO.description());
        item.setQuantity(itemRequestDTO.quantity());
        item.setPrice(itemRequestDTO.price());
        item.setImageUrl(itemRequestDTO.imageUrl());

        item.setUpdatedAt(LocalDateTime.now());

        repository.save(item);

        return item;
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException("Item não encontrado");
        }
        repository.deleteById(id);
    }
}



