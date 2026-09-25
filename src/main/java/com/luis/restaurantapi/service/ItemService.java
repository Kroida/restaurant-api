package com.luis.restaurantapi.service;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
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

    // Alterar depois listItem e update para aceitar exceptions
    public Item findItem(Long id) {
        Optional<Item> item = repository.findById(id);
        return item.orElse(null);
    }

    public void updateItem(Long id, ItemRequestDTO itemRequestDTO) {
        Optional<Item> item = repository.findById(id);

        if (item.isPresent()) {
            Item foundItem = item.get();

            foundItem.setName(itemRequestDTO.name());
            foundItem.setDescription(itemRequestDTO.description());
            foundItem.setQuantity(itemRequestDTO.quantity());
            foundItem.setPrice(itemRequestDTO.price());
            foundItem.setImageUrl(itemRequestDTO.imageUrl());

            foundItem.setUpdatedAt(LocalDateTime.now());

            repository.save(foundItem);
        }
    }

    // Alterar depois para lançar exception caso item não exista
    public void deleteItem(Long id) {
        repository.deleteById(id);
    }
}
