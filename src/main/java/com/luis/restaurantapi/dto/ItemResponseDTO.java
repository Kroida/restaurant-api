
package com.luis.restaurantapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados de um item cadastrado no cardápio")
public record ItemResponseDTO(
        @Schema(description = "Identificador do item", example = "1")
        Long id,

        @Schema(description = "Nome do item", example = "Hambúrguer artesanal")
        String name,

        @Schema(description = "Descrição do item", example = "Hambúrguer com queijo e bacon")
        String description,

        @Schema(description = "Quantidade disponível", example = "10")
        Integer quantity,

        @Schema(description = "Preço do item", example = "29.90")
        BigDecimal price,

        @Schema(description = "URL da imagem", example = "https://example.com/hamburguer.jpg")
        String imageUrl,

        @Schema(description = "Data de criação", example = "2026-11-16T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-11-16T15:45:00")
        LocalDateTime updatedAt
) {}
