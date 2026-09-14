package com.luis.restaurantapi.repository;

import com.luis.restaurantapi.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}