package io.github.kauanmedeirosss.publisher.controller;

import io.github.kauanmedeirosss.publisher.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Publisher {

    @Value("${rabbitmq.queuename}")
    private String nomeFila;

    private final PublisherService publisherService;

    @PostMapping("publish/texto")
    public void publicaTexto(@RequestBody String texto){
        System.out.println("Enviando mensagem: " + texto);
        publisherService.publicaMensagemTexto(texto, nomeFila);
    }

    @PostMapping("publish/json")
    public void publicaJson(@RequestBody String texto){
        System.out.println("Enviando mensagem: " + texto);
        publisherService.publicaMensagemJson(texto, nomeFila);
    }

}
