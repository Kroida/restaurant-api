package com.luis.restaurantapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class itemController {
    @PostMapping
    public void assignItem() {
        return;
    }

    @GetMapping
    public void listItem() {
        return;
    }

    @GetMapping("/{id}")
    public void searchItem() {
        return;
    }

    @PutMapping("/{id}")
    public void updateItem() {
        return;
    }

    @DeleteMapping("/{id}")
    public void deleteitem() {
        return;
    }
}