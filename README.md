## Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Postgresql](https://www.postgresql.org/about/)

#### Acessar aplicação em `http://localhost:8080`


## Arquitetura

#### Desenho da solução
![Desenho da solução](.docs/System-design.png)

#### Diagrama de sequencia
![Diagrama de sequencia](.docs/Diagrama-sequencia.png)


## API

- POST http://localhost:8080/transaction

```json
{
    "toAccount": 1,
    "fromAccount": 2,
    "value": 100.0
}
```
