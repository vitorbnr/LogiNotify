# 🚚 LogiNotify - Sistema de Notificações de Entrega Assíncrono

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green?logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Messaging-orange?logo=rabbitmq)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-lightgrey?logo=swagger)

O **LogiNotify** é um microsserviço robusto desenvolvido em **Java (Spring Boot)** para gerenciar solicitações de entrega e notificações de forma totalmente assíncrona e resiliente.

Este projeto demonstra a implementação de uma arquitetura orientada a eventos, capaz de lidar com alto volume de requisições sem bloquear o usuário final, garantindo a integridade dos dados mesmo em caso de falhas.

---

## 🎯 Por que este projeto existe? (O Problema)

Em sistemas tradicionais (síncronos), quando um usuário solicita uma entrega, ele precisa esperar na tela de "carregando..." enquanto o servidor processa rotas, salva no banco e envia e-mails. Se o serviço de e-mail cair, o pedido do usuário falha.

### 💡 A Solução LogiNotify
O **LogiNotify** desacopla o recebimento do pedido do seu processamento:
1.  **Resposta Instantânea:** A API recebe o pedido, salva como `PENDENTE` e responde "OK" em milissegundos.
2.  **Processamento em Background:** Uma fila (RabbitMQ) gerencia as tarefas pesadas.
3.  **Resiliência:** Se o processamento falhar, o sistema possui mecanismos de segurança (Dead Letter Queues) para não perder dados.
4.  **Feedback:** Assim que processado, o sistema atualiza o status e notifica o cliente por e-mail automaticamente.

---

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3** (Web, Data JPA, AMQP, Mail, Validation)
* **RabbitMQ** (Message Broker & Dead Letter Queues)
* **PostgreSQL** (Banco de Dados Relacional)
* **Docker & Docker Compose** (Containerização da infraestrutura)
* **JavaMailSender** (Integração SMTP com Mailtrap)
* **Swagger / OpenAPI** (Documentação automática da API)
* **Bean Validation** (Blindagem de dados na entrada)

---

## 🏗️ Arquitetura do Sistema

O fluxo de dados segue o padrão **Producer-Consumer**:

1.  **API (Controller):** Recebe o JSON, valida os dados e salva o pedido como `PENDENTE` no PostgreSQL.
2.  **Producer:** Envia o ID do pedido para a fila `entrega.notificacao.queue` no RabbitMQ.
3.  **Consumer:** Ouve a fila, simula o processamento logístico, atualiza o status para `ENTREGUE` no banco.
4.  **Notificação:** O Consumer aciona o serviço de e-mail para avisar o cliente.
5.  **Falhas (DLQ):** Se algo der errado, a mensagem é movida automaticamente para a `entrega.notificacao.dlq` para análise futura.

---

## ⚙️ Pré-requisitos

* **Java 21** instalado.
* **Docker** e **Docker Compose** instalados e rodando.
* Uma conta no **Mailtrap** (para testes de envio de e-mail).

---

## 🏃‍♂️ Como Rodar o Projeto

### 1. Subir a Infraestrutura (Docker)
Na raiz do projeto, execute:
```bash
docker-compose up -d
```

Isso iniciará o PostgreSQL (porta 5432) e o RabbitMQ (portas 5672 e 15672).

### 2. Configurar Variáveis de Ambiente

Por segurança, o projeto não contém senhas hardcoded. Configure as variáveis de ambiente na sua IDE ou terminal para rodar a aplicação:

```dotenv
MAIL_USERNAME: Seu usuário do Mailtrap (SMTP).

MAIL_PASSWORD: Sua senha do Mailtrap (SMTP).

DB_USERNAME: (Opcional, padrão: postgres)

DB_PASSWORD: (Opcional, padrão: vazio ou confg do docker)
```

### 3. Executar a Aplicação

```bash
./mvnw spring-boot:run
```

Ou rode diretamente pela sua classe LogiNotifyApplication na IDE (certifique-se de configurar as variáveis de ambiente na configuração de execução da IDE).

---

### 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse a interface visual para testar os endpoints:
👉 http://localhost:8080/swagger-ui/index.html

---

### 🧪 Testando os Cenários

✅ 1. Caminho Feliz (Sucesso)
Envie um POST para http://localhost:8080/entregas:

```bash
{
  "idPedido": "ORD-12345",
  "nomeCliente": "Vitor Bernardo",
  "email": "vitor@teste.com",
  "endereco": "Av. Paulista, 1000"
}
```

Resultado:

Status **HTTP 200 OK**.

E-mail chega na caixa de entrada do Mailtrap.

Status no banco muda de **PENDENTE** para **ENTREGUE**.

---

❌ 2. Validação de Dados (Erro 400)

```bash
{
  "idPedido": "ORD-ERR",
  "nomeCliente": "",
  "endereco": "Rua Sem Nome"
}
```

Resultado:
A API retorna **400 Bad Request** detalhando os campos inválidos.

---

💀 3. Tolerância a Falhas (DLQ)
Para testar a resiliência:

Pare o container do banco:

```bash
docker stop loginotify-db
```

Envie uma requisição de entrega.

Verifique no RabbitMQ Management (http://localhost:15672 - login: guest/guest):

A mensagem sairá da fila principal.

A mensagem será movida para **entrega.notificacao.dlq**.

---

🛠️ Melhorias Futuras

[ ] Implementar reprocessamento automático da DLQ.

[ ] Adicionar autenticação (Spring Security/JWT).

[ ] Criar front-end para visualização dos pedidos.

---

Obrigado por analisar o meu projeto! Fique à vontade para entrar em contato. 😁
