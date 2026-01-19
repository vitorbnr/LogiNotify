package com.vitorbnr.LogiNotify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EntregaDTO(
        Long id,

        @NotBlank(message = "O ID do pedido é obrigatório")
        String idPedido,

        @NotBlank(message = "O nome do cliente é obrigatório")
        String nomeCliente,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato do email é inválido")
        String email,

        @NotBlank(message = "O endereço de entrega é obrigatório")
        String endereco,

        String status
) {}