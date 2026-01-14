package com.vitorbnr.LogiNotify.dto;

public record EntregaDTO(
        String idPedido,
        String nomeCliente,
        String endereco,
        String status
) {}