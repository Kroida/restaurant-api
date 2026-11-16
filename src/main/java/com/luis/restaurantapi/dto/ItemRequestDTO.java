package com.luis.restaurantapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Schema(description = "Dados necessários para cadastrar ou atualizar um item")
public record ItemRequestDTO(
        @Schema(
                description = "Nome do item",
                example = "Hambúrguer artesanal"
        )
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "Tamanho máximo: 120 caracteres")
        String name,

        @Schema(
                description = "Descrição do item",
                example = "Hambúrguer com queijo e bacon"
        )
        @Size(max = 240, message = "Tamanho máximo: 240 caracteres")
        String description,

        @Schema(
                description = "Quantidade disponível",
                example = "10"
        )
        @PositiveOrZero(message = "A quantidade não pode ser negativa")
        Integer quantity,

        @Schema(
                description = "Preço do item em reais",
                example = "29.90"
        )
        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price,

        @Schema(
                description = "URL da imagem do item",
                example = "https://example.com/hamburguer.jpg"
        )
        @Size(max = 500)
        @URL
        String imageUrl
) {
}