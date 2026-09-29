package com.luis.restaurantapi.service;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
import com.luis.restaurantapi.exception.ItemNotFoundException;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public void createItem(ItemRequestDTO itemRequestDTO) {
        Item item = new Item(itemRequestDTO);
        repository.save(item);
    }

    public List<ItemResponseDTO> listAllItems() {
        List<ItemResponseDTO> itemList = repository.findAll().stream().map(ItemResponseDTO::new).toList();
        return itemList;
    }

    public Item findItem(Long id) {
        Optional<Item> item = repository.findById(id);
        return item.orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));
    }

    public void updateItem(Long id, ItemRequestDTO itemRequestDTO) {
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));

        item.setName(itemRequestDTO.name());
        item.setDescription(itemRequestDTO.description());
        item.setQuantity(itemRequestDTO.quantity());
        item.setPrice(itemRequestDTO.price());
        item.setImageUrl(itemRequestDTO.imageUrl());

        item.setUpdatedAt(LocalDateTime.now());

        repository.save(item);

    }

    public void deleteItem(Long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException("Item não encontrado");
        }
        repository.deleteById(id);
    }
}



