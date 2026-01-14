package com.vitorbnr.LogiNotify.dto;

public record EntregaDTO(
        Long id,
        String idPedido,
        String nomeCliente,
        String email,
        String endereco,
        String status
) {}