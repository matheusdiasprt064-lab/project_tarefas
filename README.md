# Task Manager API

API REST de gerenciamento de tarefas com autenticação JWT, desenvolvida com Spring Boot e PostgreSQL.

## 🌐 API Online
```
https://project-tarefas.onrender.com
```

## 📄 Swagger
```
https://project-tarefas.onrender.com/swagger-ui/index.html
```

## Tecnologias

- Java 17+
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

## Como rodar localmente

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL

### Configuração do banco
Crie um banco e um usuário no PostgreSQL:
```sql
CREATE USER tarefas_user WITH PASSWORD 'sua_senha';
CREATE DATABASE tarefas_db;
GRANT ALL PRIVILEGES ON DATABASE tarefas_db TO tarefas_user;
```

### Configuração da aplicação
Crie o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tarefas_db
spring.datasource.username=tarefas_user
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
jwt.secret=seu-secret-com-minimo-32-caracteres-aqui
jwt.expiration=86400000
```

### Rodando
```bash
mvn spring-boot:run
```

## Documentação

Acesse o Swagger em: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Endpoints

### Autenticação
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/auth/register` | Registrar usuário |
| POST | `/auth/login` | Login, retorna token JWT |

### Tarefas (requer token)
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/tarefas` | Listar tarefas do usuário |
| POST | `/tarefas` | Criar tarefa |
| PUT | `/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/tarefas/{id}` | Remover tarefa |

## Autenticação

Após o login, use o token retornado no header:
```
Authorization: Bearer <token>
```
