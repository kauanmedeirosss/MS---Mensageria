package io.github.kauanmedeirosss.subscriber.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

    @RabbitListener(queues = "${rabbitmq.queuename}")
    public void recebeMensagem(Message mensagem){
        System.out.println(mensagem);
    }

}
