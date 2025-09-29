package io.github.kauanmedeirosss.publisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kauanmedeirosss.publisher.model.MensagemFila;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    public void publicaMensagemTexto(String mensagem, String fila){
        System.out.println(mensagem);
        rabbitTemplate.convertAndSend(fila, mensagem);
    }

    public void publicaMensagemJson(String mensagem, String fila){
        MensagemFila msg = (MensagemFila) jsonToObject(mensagem, MensagemFila.class);
        rabbitTemplate.convertAndSend(fila, objectToJNode(msg));
    }

    private JsonNode objectToJNode(Object jsonObject){
        return objectMapper.valueToTree(jsonObject);
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
