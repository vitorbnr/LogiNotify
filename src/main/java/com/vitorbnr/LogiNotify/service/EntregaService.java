package com.vitorbnr.LogiNotify.service;

import com.vitorbnr.LogiNotify.domain.Entrega;
import com.vitorbnr.LogiNotify.domain.StatusEntrega;
import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import com.vitorbnr.LogiNotify.repository.EntregaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EntregaRepository entregaRepository;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void enviarNotificacao(EntregaDTO entregaDTO) {
        Entrega entrega = new Entrega();
        entrega.setNomeDestinatario(entregaDTO.nomeCliente());
        entrega.setEndereco(entregaDTO.endereco());
        entrega.setEmailDestinatario(entregaDTO.email());
        entrega.setStatus(StatusEntrega.PENDENTE);

        entrega = entregaRepository.save(entrega);
        System.out.println("Pedido salvo no banco com ID: " + entrega.getId());

        EntregaDTO mensagemFila = new EntregaDTO(
                entrega.getId(),
                entregaDTO.idPedido(),
                entregaDTO.nomeCliente(),
                entregaDTO.email(),
                entregaDTO.endereco(),
                entrega.getStatus().toString()
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, mensagemFila);
        System.out.println("Mensagem enviada para a fila: " + mensagemFila);
    }
}