package com.vitorbnr.LogiNotify.controller;

import com.vitorbnr.LogiNotify.config.RabbitMQConfig;
import com.vitorbnr.LogiNotify.domain.Entrega;
import com.vitorbnr.LogiNotify.domain.StatusEntrega;
import com.vitorbnr.LogiNotify.repository.EntregaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<Entrega> criarPedido(@RequestBody Entrega entrega) {

        entrega.setStatus(StatusEntrega.PENDENTE);

        Entrega entregaSalva = repository.save(entrega);

        rabbitTemplate.convertAndSend(RabbitMQConfig.NOME_FILA, entregaSalva);

        return ResponseEntity.ok(entregaSalva);
    }
}