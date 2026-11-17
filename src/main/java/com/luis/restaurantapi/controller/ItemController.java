package com.luis.restaurantapi.controller;

import com.luis.restaurantapi.dto.ItemRequestDTO;
import com.luis.restaurantapi.dto.ItemResponseDTO;
import com.luis.restaurantapi.mapper.ItemMapper;
import com.luis.restaurantapi.model.Item;
import com.luis.restaurantapi.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Tag(name = "Itens do cardápio", description = "Operações de gerenciamento dos itens do restaurante")
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @Operation(summary = "Criar item", description = "Cria um item para o cardápio")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Item criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos para criação do item"
            )
    })
    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemRequestDTO requestDto) {
        Item item = service.create(requestDto);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id")
                .buildAndExpand(item.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDto);
    }

    @Operation(summary = "Listar itens", description = "Retorna todos os itens cadastrados no cardápio")
    @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "Itens encontrados com sucesso"
            )
    })
    @GetMapping
    public ResponseEntity<PagedModel<ItemResponseDTO>> getAll(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ItemResponseDTO> response = service.getAll(pageable)
                .map(ItemMapper::toResponseDto);

        return ResponseEntity.ok(new PagedModel<>(response));
    }

    @Operation(summary = "Procurar item", description = "Retorna via ID um item cadastrado no cardápio")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item encontrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable Long id) {
        Item item = service.getById(id);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Atualizar item", description = "Atualiza via ID um item cadastrado no cardápio")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos para atualização"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        Item item = service.update(id, itemRequestDTO);

        ItemResponseDTO responseDto = ItemMapper.toResponseDto(item);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Deletar item", description = "Deleta via ID um item cadastrado no cardápio")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Item excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}