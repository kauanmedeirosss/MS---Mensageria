## Observações:
* RabbitMQ rodando no docker
* Docker rodando no WSL

## Estrutura:
### Publisher
Aplicação que irá escrever na fila.
### Subscriber
Aplicação que irá consumir na fila.

## Passo a passo do que foi feito:
### 1-2 commit
#### Mudanças no código:
* A estrutura básica da aplicação foi construida
#### O que deve/pode ser executado externamente:
* No ambiente WSL onde há o docker, será rodado o container do RabbitMQ, com o comando:
  * docker run -d --name my-rmq-container -p 8080:15672 -p 5672:5672 rabbitmq:3-management
    OBS: Se rodar irá retornar um código no próprio terminal WSL
* Em seguida, no próprio navegador acessamos o http://localhost:8080/#/ e já nos deparamos com a interface RabbitNQ
* Navegado até a aba `Queues and Streams`, preenchemos:
  * Name: Nome que deixamos na nossa configuração (no application.properties)
* Após essa definição `Add queue` (no canto inferior esquerdo)

### 3 commit
#### Mudanças no código:
* Alteração da porta do publisher e subscriber, no application.properties de cada uma, para 8081 e 8082 respectivamente. RabbitMQ já estava rodando na 8080, isso gera conflito
#### O que deve/pode ser executado externamente:
* Agora já é possível rodar o projeto (com o container do RabbitMQ também rodando)
* Na aba do RabbitMQ, se dermos um click direito no nome da nossa fila e abrirmos ela em outra aba, podemos ver se a conexão foi estabelecida se conseguirmos ver `Consumers(1)`
* Mandando uma request `POST` para `http://localhost:8081` com body da mensagem desejada, podemos ver os logs da aplicação mostrando que o sistema está funcionando e na mesma aba que vemos os consumers podemos ver também a mensagem ser receptada nas estatísticas.