package io.github.kauanmedeirosss.subscriber.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MensagemFila {
    @JsonProperty("chave1")
    private String chave1;
    @JsonProperty("chave2")
    private String chave2;
}
