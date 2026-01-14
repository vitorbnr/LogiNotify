package com.vitorbnr.LogiNotify.consumer;

import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EntregaConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receberNotificacao(@Payload EntregaDTO entregaDTO) {
        System.out.println("=================================================");
        System.out.println("NOVA NOTIFICAÇÃO RECEBIDA!");
        System.out.println("Pedido ID: " + entregaDTO.idPedido());
        System.out.println("Cliente: " + entregaDTO.nomeCliente());
        System.out.println("Status: " + entregaDTO.status());
        System.out.println("=================================================");

    }
}