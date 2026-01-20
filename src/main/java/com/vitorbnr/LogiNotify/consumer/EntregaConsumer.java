package com.vitorbnr.LogiNotify.consumer;

import com.vitorbnr.LogiNotify.domain.StatusEntrega;
import com.vitorbnr.LogiNotify.dto.EntregaDTO;
import com.vitorbnr.LogiNotify.repository.EntregaRepository;
import com.vitorbnr.LogiNotify.service.EnvioEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EntregaConsumer {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private EnvioEmailService envioEmailService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receberNotificacao(@Payload EntregaDTO entregaDTO) {
        if (entregaDTO.id() != null) {
            entregaRepository.findById(entregaDTO.id()).ifPresentOrElse(
                    entrega -> {
                        simularProcessamento();

                        entrega.setStatus(StatusEntrega.ENTREGUE);
                        entregaRepository.save(entrega);

                        System.out.println("Status atualizado para ENTREGUE (ID: " + entrega.getId() + ")");

                        String mensagem = String.format(
                                "Olá %s,\n\nSeu pedido %s foi entregue com sucesso no endereço:\n%s\n\nObrigado por usar o LogiNotify!",
                                entrega.getNomeDestinatario(),
                                entregaDTO.idPedido(),
                                entrega.getEndereco()
                        );

                        envioEmailService.enviarEmail(
                                entrega.getEmailDestinatario(),
                                "LogiNotify - Entrega Concluída: " + entregaDTO.idPedido(),
                                mensagem
                        );
                    },
                    () -> System.err.println("Erro: Entrega não encontrada no banco!")
            );
        } else {
            System.err.println("Erro: Mensagem sem ID!");
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