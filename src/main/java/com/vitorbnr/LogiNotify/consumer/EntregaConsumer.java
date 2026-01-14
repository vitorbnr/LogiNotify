package com.vitorbnr.LogiNotify.consumer;

import com.vitorbnr.LogiNotify.domain.StatusEntrega;
import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import com.vitorbnr.LogiNotify.repository.EntregaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EntregaConsumer {

    @Autowired
    private EntregaRepository entregaRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receberNotificacao(@Payload EntregaDTO entregaDTO) {
        System.out.println("--- Recebendo mensagem da fila ---");

        if (entregaDTO.id() != null) {
            entregaRepository.findById(entregaDTO.id()).ifPresentOrElse(
                    entrega -> {
                        simularProcessamento();

                        entrega.setStatus(StatusEntrega.ENTREGUE);
                        entregaRepository.save(entrega);

                        System.out.println("Status atualizado para ENTREGUE no banco de dados (ID: " + entrega.getId() + ")");
                    },
                    () -> System.err.println("Erro: Entrega com ID " + entregaDTO.id() + " não encontrada no banco!")
            );
        } else {
            System.err.println("Erro: Mensagem recebida sem ID da entrega!");
        }
    }

    private void simularProcessamento() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}