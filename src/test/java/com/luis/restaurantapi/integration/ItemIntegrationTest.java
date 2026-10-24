package com.luis.restaurantapi.integration;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.repository.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveCriarItemEGravarNoBanco() throws Exception {

        ItemRequestDTO dto = new ItemRequestDTO(
                "Hambúrguer",
                "Hambúrguer artesanal",
                10,
                new BigDecimal("25.90"),
                "https://imagem.com/hamburguer.jpg"
        );

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hambúrguer"))
                .andExpect(jsonPath("$.price").value(25.90));

        List<Item> itens = repository.findAll();

        assertEquals(1, itens.size());
        assertEquals("Hambúrguer", itens.getFirst().getName());
    }

    @Test
    void deveBuscarItemPorId() throws Exception {

        Item item = new Item();

        item.setName("Pizza");
        item.setDescription("Pizza de calabresa");
        item.setQuantity(5);
        item.setPrice(new BigDecimal("45.00"));
        item.setImageUrl("https://imagem.com/pizza.jpg");

        Item itemSalvo = repository.save(item);

        mockMvc.perform(get("/items/{id}", itemSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemSalvo.getId()))
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andExpect(jsonPath("$.price").value(45.00));
    }

    @Test
    void deveListarItens() throws Exception {

        Item item1 = new Item();
        item1.setName("Hambúrguer");
        item1.setDescription("Hambúrguer artesanal");
        item1.setQuantity(10);
        item1.setPrice(new BigDecimal("25.90"));
        item1.setImageUrl("https://imagem.com/hamburguer.jpg");

        Item item2 = new Item();
        item2.setName("Pizza");
        item2.setDescription("Pizza de calabresa");
        item2.setQuantity(5);
        item2.setPrice(new BigDecimal("45.00"));
        item2.setImageUrl("https://imagem.com/pizza.jpg");

        repository.save(item1);
        repository.save(item2);

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name",
                        hasItems("Hambúrguer", "Pizza")));
    }

    @Test
    void deveAtualizarItem() throws Exception {

        Item item = new Item();

        item.setName("Hambúrguer");
        item.setDescription("Hambúrguer simples");
        item.setQuantity(5);
        item.setPrice(new BigDecimal("20.00"));
        item.setImageUrl("https://imagem.com/hamburguer.jpg");

        Item itemSalvo = repository.save(item);

        ItemRequestDTO dto = new ItemRequestDTO(
                "Hambúrguer Especial",
                "Hambúrguer atualizado",
                15,
                new BigDecimal("35.90"),
                "https://imagem.com/hamburguer-especial.jpg"
        );

        mockMvc.perform(put("/items/{id}", itemSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hambúrguer Especial"))
                .andExpect(jsonPath("$.price").value(35.90));

        Item itemAtualizado = repository
                .findById(itemSalvo.getId())
                .orElseThrow();

        assertEquals("Hambúrguer Especial", itemAtualizado.getName());
        assertEquals(new BigDecimal("35.90"), itemAtualizado.getPrice());
    }

    @Test
    void deveDeletarItem() throws Exception {

        Item item = new Item();

        item.setName("Batata Frita");
        item.setDescription("Porção de batata");
        item.setQuantity(20);
        item.setPrice(new BigDecimal("18.00"));
        item.setImageUrl("https://imagem.com/batata.jpg");

        Item itemSalvo = repository.save(item);

        mockMvc.perform(delete("/items/{id}", itemSalvo.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(itemSalvo.getId()));
    }

    @Test
    void deveRetornar404QuandoItemNaoExistir() throws Exception {

        Long idInexistente = 999999999L;

        assertFalse(repository.existsById(idInexistente));

        mockMvc.perform(get("/items/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Item não encontrado"))
                .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"));
    }
}