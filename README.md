# Task Manager API

API REST de gerenciamento de tarefas com autenticacao JWT, desenvolvida com Spring Boot e PostgreSQL.

## API online

```text
https://project-tarefas.onrender.com
```

## Swagger

```text
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

### Pre-requisitos

- Java 17+
- Maven
- PostgreSQL

### Banco de dados

```sql
CREATE USER tarefas_user WITH PASSWORD 'sua_senha';
CREATE DATABASE tarefas_db;
GRANT ALL PRIVILEGES ON DATABASE tarefas_db TO tarefas_user;
```

### Configuracao

O projeto aceita variaveis de ambiente, mas tambem possui valores padrao para desenvolvimento local:

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/tarefas_db
DATABASE_USERNAME=tarefas_user
DATABASE_PASSWORD=<senha_local_do_postgres>
JWT_SECRET=<secret_forte_com_minimo_32_caracteres>
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

Nao use os valores de exemplo em producao. Configure `DATABASE_PASSWORD` e `JWT_SECRET` como variaveis de ambiente reais no servidor.

### Rodando

```bash
mvn spring-boot:run
```

Swagger local:

```text
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

### Autenticacao

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/auth/register` | Registrar usuario |
| POST | `/auth/login` | Login, retorna token JWT |

### Tarefas

Requer header:

```text
Authorization: Bearer <token>
```

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | `/tarefas` | Listar tarefas do usuario |
| POST | `/tarefas` | Criar tarefa |
| PUT | `/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/tarefas/{id}` | Remover tarefa |

Exemplo de tarefa:

```json
{
  "titulo": "Estudos",
  "descricao": "Revisar Spring Security",
  "concluida": false,
  "dataVencimento": "2026-05-10"
}
```
