# API Clientes
Challenge API Clientes for Builders.

### Getting Started

Todos os plugins e dependências estão disponíveis em: [Maven Central](https://search.maven.org/). 

* JDK 16 (somente para execução em IDE ou por linha de comando) 
* Maven 3.8+ (somente para execução em IDE ou por linha de comando)
* Docker 20.10.5, build 55c4c88

### Construindo e executando localmente

* `docker-compose up -d`

#### Test the Endpoint

Se encontra no próprio repositório os seguintes arquivos para serem importados no [Postman](https://www.postman.com/)

* Challenge Builders.postman_collection.json
* Builders Prod.postman_environment.json (Heroku)
* Builders Dev.postman_environment.json (via docker)

### Documentação da API

#### Heroku
* `https://cliente-api-builders.herokuapp.com/swagger-ui.html`

#### Local via docker
* `http://localhost:8080/swagger-ui.html`