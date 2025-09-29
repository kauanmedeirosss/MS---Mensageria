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
#### O que deve ser executado externamente (WSL):
* No ambiente WSL onde há o docker, será rodado o container do RabbitMQ, com o comando:
  * docker run -d --name my-rmq-container -p 8080:15672 -p 5672:5672 rabbitmq:3-management
    OBS: Se rodar irá retornar um código no próprio terminal WSL
#### O que deve ser executado externamente:
* Em seguida, no próprio navegador acessamos o http://localhost:8080/#/ e já nos deparamos com a interface RabbitNQ
* Navegado até a aba `Queues and Streams`, preenchemos:
  * Name: Nome que deixamos na nossa configuração (no application.properties)
* Após essa definição `Add queue` (no canto inferior esquerdo)

### 3 commit
#### Mudanças no código:
* Alteração da porta do publisher e subscriber, no `application.properties` de cada uma, para 8081 e 8082 respectivamente. RabbitMQ já estava rodando na 8080, isso gera conflito
#### O que pode ser observado externamente:
* Agora já é possível rodar o projeto (com o container do RabbitMQ também rodando)
* Na aba do RabbitMQ, se dermos um click direito no nome da nossa fila e abrirmos ela em outra aba, podemos ver se a conexão foi estabelecida se conseguirmos ver `Consumers(1)`
* Mandando uma request `POST` para `http://localhost:8081` com body da mensagem desejada (não pode ser JSON), podemos ver os logs da aplicação mostrando que o sistema está funcionando e na mesma aba que vemos os consumers podemos ver também a mensagem ser receptada nas estatísticas.

### 4 commit
#### Mudanças no código:
* Na `application.properties` de ambas aplicações foram feitas modificalções que redefinem:
  * host, usuario, senha e porta do RabbitMQ
* No SubscriberService foi alterado o retorno para vermos apenas o conteudo da mensagem no log ao invés de todo o retorno do RabbitMQ
#### O que deve ser executado externamente (WSL):
* No WSL, devemos refazer o container para se adequar as nossas novas configurações com os seguintes comandos
  * docker stop my-rmq-container
    - para o container já iniciado, se der sucesso irá retornar o nome do container (my-rmq-container)
  * docker rm my-rmq-container
    - remove o container, se der sucesso irá retornar o nome do container (my-rmq-container)
  * docker run -d --name my-rmq-container -p 8080:15672 -p 8090:5672 rabbitmq:3-management
    - sobe um novo container na porta 8090
#### O que deve ser executado externamente:
* No navegador, entramos na porta do RabbitMQ e configuramos uma nova fila
* Abrimos a aba `Admin`, na área Add a user preenchemos
  * Username: meuusuario
  * Password: minhasenha
  * Confirma: minhasenha
* Em seguida clickamos em `Add user`
* Nisso já podemos ver ele na aba `Users`
* Click no nome meuusuario, em seguida `Set permission` sem modificar nada
* Em seguida fazemos aquele mesmo processo de criar uma fila com o mesmo nome que está nas nossas properties
* Agora rodamos nossas duas aplicações e testamos o envio de requisições

### 5 commit
#### Mudanças no código:
* No Subscriber, iremos fazer uma classe de configuração própria do RabbitMQ para testar outra forma de definir suas propriedades
  * seu caminho é `src/main/.../config/RabbitMQConfig`
  * ela possui atributos contendo suas informações e métodos, um para definir parâmetros de conexão e outra para ler mensagens da fila
* No Subscriber, foi feito também uma alteração no service para adicionar o método de leitura de mensagens recém criado ao nosso método de leitura
* No Publisher, também foi feita uma classe de configuração
  * seu caminho é `src/main/.../config/RabbitMQConfig` (mesmo do subscriber)
  * ela possui atributos contendo suas informações e apenas o método para definir parâmetros de conexão
  * também há um segundo método que configura o template para usar o método de conexão
#### O que pode ser observado externamente:
* é interessante rodar as duas aplicações e testar (não esquecer do container)

### 6 commit
#### Mudanças no código:
* Agora iremos fazer com que o formato das mensagens retorne e leia como JSON
* No controller e service do publisher, foram feito novos métodos método, no service está presente a lógica de serialização para cnverter o String para JSON
* No subscriber também foram feitas alterações similares
* Agora podemos mandar JSON no body
* OBS: se ocorrer algum erro, ir na interface do RabbitMQ, clicar na fila e dar um `Purge Messages` para limpar fila de erros. Em seguida rodar novamente as aplicações
* OBS: foi enviada no teste a mensagem json:
  - {\"key1\":\"value1\", \"key2\":\"value2\"}
* OBS: SEMPRE MANDAR NOS ENDPOINTS CERTOS, texto em texto e json em json, ou a aplicação irá quebrar em loop  