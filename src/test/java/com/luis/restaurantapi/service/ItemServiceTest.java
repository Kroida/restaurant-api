package com.luis.restaurantapi.service;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.exception.ItemNotFoundException;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.repository.ItemRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    @Test
    void getByIdDeveRetornarItemQuandoExistir() {
        Long id = 1L;
        Item itemFalso = new Item();

        when(repository.findById(id))
                .thenReturn(Optional.of(itemFalso));

        Item resultado = service.getById(id);

        assertEquals(itemFalso, resultado);

        verify(repository).findById(id);
    }

    @Test
    void getByIdDeveLancarItemNotFoundQuandoNaoExistir() {
        Long id = 99L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ItemNotFoundException.class,
                () -> service.getById(id)
        );

        verify(repository).findById(id);
    }

    @Test
    void createDeveSalvarERetornarItem() {
        ItemRequestDTO dto = new ItemRequestDTO(
                "Hambúrguer",
                "Hambúrguer artesanal",
                10,
                new BigDecimal("25.90"),
                "https://imagem.com/hamburguer.jpg"
        );

        Item resultado = service.create(dto);

        assertNotNull(resultado);
        assertEquals(dto.name(), resultado.getName());
        assertEquals(dto.description(), resultado.getDescription());
        assertEquals(dto.quantity(), resultado.getQuantity());
        assertEquals(dto.price(), resultado.getPrice());
        assertEquals(dto.imageUrl(), resultado.getImageUrl());

        verify(repository).save(resultado);
    }

    @Test
    void getAllDeveRetornarListaDeItens() {
        Item item1 = new Item();
        Item item2 = new Item();

        when(repository.findAll())
                .thenReturn(List.of(item1, item2));

        List<Item> resultado = service.getAll();

        assertEquals(2, resultado.size());
        assertEquals(item1, resultado.get(0));
        assertEquals(item2, resultado.get(1));

        verify(repository).findAll();
    }

    @Test
    void updateDeveAlterarItemQuandoExistir() {
        Long id = 1L;

        Item itemExistente = new Item();

        ItemRequestDTO dto = new ItemRequestDTO(
                "Pizza",
                "Pizza grande",
                5,
                new BigDecimal("49.90"),
                "https://imagem.com/pizza.jpg"
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(itemExistente));

        Item resultado = service.update(id, dto);

        assertEquals(dto.name(), resultado.getName());
        assertEquals(dto.description(), resultado.getDescription());
        assertEquals(dto.quantity(), resultado.getQuantity());
        assertEquals(dto.price(), resultado.getPrice());
        assertEquals(dto.imageUrl(), resultado.getImageUrl());

        verify(repository).findById(id);
    }

    @Test
    void updateDeveLancarItemNotFoundQuandoNaoExistir() {
        Long id = 99L;

        ItemRequestDTO dto = new ItemRequestDTO(
                "Pizza",
                "Pizza grande",
                5,
                new BigDecimal("49.90"),
                null
        );

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ItemNotFoundException.class,
                () -> service.update(id, dto)
        );

        verify(repository).findById(id);
    }

    @Test
    void deleteDeveRemoverItemQuandoExistir() {
        Long id = 1L;

        when(repository.existsById(id))
                .thenReturn(true);

        service.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteDeveLancarItemNotFoundQuandoNaoExistir() {
        Long id = 99L;

        when(repository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ItemNotFoundException.class,
                () -> service.delete(id)
        );

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(id);
    }
}