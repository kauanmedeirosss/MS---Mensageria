package io.github.kauanmedeirosss.subscriber.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kauanmedeirosss.subscriber.model.MensagemFila;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SubscriberService {

    ObjectMapper objectMapper;

    @RabbitListener(containerFactory = "listenerContainerFactory", queues = "${rabbitmq.queuename}")
    public void recebeMensagem(Message mensagem){
        String mensagemJson = rmqMessageToString(mensagem);
        MensagemFila msg = (MensagemFila) jsonToObject(mensagemJson, MensagemFila.class);
        System.out.println(msg.toString());
    }

    private String rmqMessageToString(Message mensagem){
        return new String(mensagem.getBody(), StandardCharsets.UTF_8);
    }

    private Object jsonToObject(String jsonString, Class<?> clazz){
        try{
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init(){
        objectMapper = new ObjectMapper();
    }

}
