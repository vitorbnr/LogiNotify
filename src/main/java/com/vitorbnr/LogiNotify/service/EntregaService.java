package com.vitorbnr.LogiNotify.service;

import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void enviarNotificacao(EntregaDTO entregaDTO) {
        rabbitTemplate.convertAndSend(exchange, routingKey, entregaDTO);

        System.out.println("Mensagem enviada para o RabbitMQ: " + entregaDTO);
    }
}