# [:scream: Runtime Terror] Template de Arquitetura Hexagonal 

:gem: Esta aplicação é um exemplo de implementação do nosso framework de desenvolvimento,
que fala sobre arquitetura hexagonal + TDD.

O caso de uso considera que dejamos cadastrar, cancelar e encontrar pedidos (_OrderDto_) que possuem uma
lista de itens (_OrderItemDto_), um status do pedido, o valor total do pedido. Cada item
da lista é composto por um produto (_Product_) e por sua quantidade. Por sua vez, um produto
possui nome e preço.

![sequence-diagram](/Documentation/sequence-diagram.png)

## :wrench: Ferramentas utilizadas

:coffee: Java 11

:leaves: SpringBoot

:bird: Maven

:hot_pepper: Lombok

:newspaper: Logback (SL4j)

:crystal_ball: Junit5

:stuck_out_tongue_winking_eye: Mockito

:whale: docker

:cloud: Localstack (open-source mock para os serviços reais da AWS)

## :play_pause: Instalação e execução

### :arrow_down: Instalação

> :warning: Já ter instalado, Java 11, Maven e Lombok. Para mais dicas consultar a coluna

:busts_in_silhouette: Clonar este repositório na sua máquina.

:computer: Importar o projeto no seu editor favorito.

### :arrow_forward: Execução

:shell: No terminal executar `mvn clean install`, para verificar se o build está correto.

:shell: No terminal inicialize a infraestrura disponível

  ```shell
  docker-compose up -d
  
  Para verificar se todos os serviços estão sendo executados corretamente, acesse http://localhost:4566/health.
  ```

> :bulb: Mais dicas sobre o serviço de mensageria utilizado (SQS) podem ser encontradas na [doc de infraestrutura](/Documentation/Infraestrutura.md).

:first_quarter_moon: Executar a aplicação em sua IDE favorita.

> :warning: Ao terminar a execução, lembre de desligar a instância do seu docker via `docker-compose down`.

## :handshake: Como contribuir

:rocket: Abrir uma pull request.
