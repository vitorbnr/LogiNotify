package com.vitorbnr.LogiNotify.controller;

import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import com.vitorbnr.LogiNotify.service.EntregaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping
    public ResponseEntity<String> criarEntrega(@RequestBody @Valid EntregaDTO entregaDTO) {
        entregaService.enviarNotificacao(entregaDTO);

        return ResponseEntity.ok("Entrega recebida e enviada para processamento!");
    }
}