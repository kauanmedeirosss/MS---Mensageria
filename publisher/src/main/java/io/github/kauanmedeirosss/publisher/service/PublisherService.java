package io.github.kauanmedeirosss.publisher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publicaMensagemTexto(String mensagem, String fila){
        System.out.println(mensagem);
        rabbitTemplate.convertAndSend(fila, mensagem);
    }

}
