package com.luis.restaurantapi.controller;

import tools.jackson.databind.ObjectMapper;
import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.exception.ItemNotFoundException;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService service;

    @Test
    void createDeveRetornar201() throws Exception {
        ItemRequestDTO dto = new ItemRequestDTO(
                "Hambúrguer",
                "Hambúrguer artesanal",
                10,
                new BigDecimal("25.90"),
                "https://imagem.com/hamburguer.jpg"
        );

        Item item = new Item();
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setQuantity(dto.quantity());
        item.setPrice(dto.price());
        item.setImageUrl(dto.imageUrl());

        when(service.create(any(ItemRequestDTO.class)))
                .thenReturn(item);

        mockMvc.perform(
                        post("/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.price").value(25.90));

        verify(service).create(any(ItemRequestDTO.class));
    }

    @Test
    void getAllDeveRetornar200() throws Exception {
        Item item1 = new Item();
        item1.setName("Pizza");

        Item item2 = new Item();
        item2.setName("Hambúrguer");

        // Simula uma página contendo dois itens cadastrados.
        Page<Item> pagina = new PageImpl<>(
                List.of(item1, item2),
                PageRequest.of(0, 10),
                2
        );

        // Configura o Service para retornar os itens paginados.
        when(service.getAll(any(Pageable.class)))
                .thenReturn(pagina);

        // Valida a resposta HTTP e a estrutura paginada.
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Pizza"))
                .andExpect(jsonPath("$.content[1].name").value("Hambúrguer"))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(1));

        // Verifica se o Service foi chamado com a paginação.
        verify(service).getAll(any(Pageable.class));
    }


    @Test
    void getByIdDeveRetornar200QuandoExistir() throws Exception {
        Item item = new Item();
        item.setName("Pizza");

        when(service.getById(1L))
                .thenReturn(item);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));

        verify(service).getById(1L);
    }

    @Test
    void getByIdDeveRetornar404QuandoNaoExistir() throws Exception {
        when(service.getById(99L))
                .thenThrow(new ItemNotFoundException("Item não encontrado"));

        mockMvc.perform(get("/items/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Item não encontrado"))
                .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"));

        verify(service).getById(99L);
    }

    @Test
    void updateDeveRetornar200() throws Exception {
        ItemRequestDTO dto = new ItemRequestDTO(
                "Pizza atualizada",
                "Descrição atualizada",
                5,
                new BigDecimal("49.90"),
                "https://imagem.com/pizza.jpg"
        );

        Item item = new Item();
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setQuantity(dto.quantity());
        item.setPrice(dto.price());
        item.setImageUrl(dto.imageUrl());

        when(service.update(eq(1L), any(ItemRequestDTO.class)))
                .thenReturn(item);

        mockMvc.perform(
                        put("/items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza atualizada"))
                .andExpect(jsonPath("$.price").value(49.90));

        verify(service).update(eq(1L), any(ItemRequestDTO.class));
    }

    @Test
    void deleteDeveRetornar204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void createDeveRetornar400QuandoDadosForemInvalidos() throws Exception {
        ItemRequestDTO dto = new ItemRequestDTO(
                "",
                "Teste",
                -1,
                new BigDecimal("-10"),
                "banana"
        );

        mockMvc.perform(
                        post("/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ITEM_NOT_VALID"));
    }
}