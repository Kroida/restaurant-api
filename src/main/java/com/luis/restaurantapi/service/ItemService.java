package com.luis.restaurantapi.service;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.exception.ItemNotFoundException;
import com.luis.restaurantapi.mapper.ItemMapper;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.repository.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Item create(ItemRequestDTO dto) {
        Item item = ItemMapper.toEntity(dto);
        repository.save(item);
        return item;
    }

    @Transactional(readOnly = true)
    public List<Item> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Item getById(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));
        return item;
    }

    @Transactional
    public Item update(Long id, ItemRequestDTO dto) {
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado"));
        ItemMapper.updateEntity(item, dto);
        return item;
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException("Item não encontrado");
        }
        repository.deleteById(id);
    }
}