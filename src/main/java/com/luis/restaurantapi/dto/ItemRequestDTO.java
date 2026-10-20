package com.luis.restaurantapi.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record ItemRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "Tamanho máximo: 120 caracteres")
        String name,

        @Size(max = 240, message = "Tamanho máximo: 240 caracteres")
        String description,

        @PositiveOrZero(message = "A quantidade não pode ser negativa")
        Integer quantity,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price,

        @Size(max = 500)
        @URL
        String imageUrl
) {
}