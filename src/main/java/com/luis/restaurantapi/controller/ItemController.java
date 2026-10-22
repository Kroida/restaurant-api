package com.luis.restaurantapi.controller;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
import com.luis.restaurantapi.mapper.ItemMapper;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemRequestDTO requestDto) {
        Item item = service.create(requestDto);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable Long id) {
        Item item = service.getById(id);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        Item item = service.update(id, itemRequestDTO);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}