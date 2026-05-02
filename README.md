# Projeto de Sistema de Agendamentos e Consultas.

Repositorio unificado do projeto de lista de tarefas.

Ele contem:

- `src/`: API REST em Spring Boot com autenticacao JWT.
- `projeto-estudos1/`: estudos em Java com gerenciador de tarefas no console.

## API REST

API de gerenciamento de tarefas com autenticacao JWT, desenvolvida com Spring Boot e PostgreSQL.

### API online

```text
https://project-tarefas.onrender.com
```

### Swagger

```text
https://project-tarefas.onrender.com/swagger-ui/index.html
```

### Tecnologias da API

- Java 17+
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

### Como rodar localmente

Pre-requisitos:

- Java 17+
- Maven
- PostgreSQL

Banco de dados:

```sql
CREATE USER tarefas_user WITH PASSWORD 'sua_senha';
CREATE DATABASE tarefas_db;
GRANT ALL PRIVILEGES ON DATABASE tarefas_db TO tarefas_user;
```

Variaveis de ambiente:

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/tarefas_db
DATABASE_USERNAME=tarefas_user
DATABASE_PASSWORD=<senha_local_do_postgres>
JWT_SECRET=<secret_forte_com_minimo_32_caracteres>
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

Nao use os valores de exemplo em producao. Configure `DATABASE_PASSWORD` e `JWT_SECRET` como variaveis de ambiente reais no servidor.

Rodando:

```bash
mvn spring-boot:run
```

Swagger local:

```text
http://localhost:8080/swagger-ui/index.html
```

### Endpoints

Autenticacao:

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/auth/register` | Registrar usuario |
| POST | `/auth/login` | Login, retorna token JWT |

Tarefas:

```text
Authorization: Bearer <token>
```

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | `/tarefas` | Listar tarefas do usuario |
| POST | `/tarefas` | Criar tarefa |
| PUT | `/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/tarefas/{id}` | Remover tarefa |

Exemplo:

```json
{
  "titulo": "Estudos",
  "descricao": "Revisar Spring Security",
  "concluida": false,
  "dataVencimento": "2026-05-10"
}
```

## Projeto Java de Console

O projeto de estudos fica em:

```text
projeto-estudos1/
|- exerciciosjava/
|  `- exercicios.java
`- src/
   |- Main.java
   |- model/
   |  `- Task.java
   `- service/
      `- TaskManager.java
```

Funcionalidades:

- Adicionar tarefas.
- Listar tarefas.
- Editar tarefas.
- Alternar tarefa entre pendente e concluida.
- Remover tarefas.
- Buscar ou filtrar tarefas.
- Trabalhar com datas de vencimento.
- Salvar tarefas em arquivo local ignorado pelo Git.

Compilando a versao organizada:

```bash
javac projeto-estudos1/src/Main.java projeto-estudos1/src/model/Task.java projeto-estudos1/src/service/TaskManager.java
```

Compilando a versao simples:

```bash
javac projeto-estudos1/exerciciosjava/exercicios.java
```

## Organizacao

Arquivos locais como `.env`, `.vscode`, `target`, `.class`, `.jar` e arquivos de tarefas salvas ficam fora do Git por seguranca e organizacao.
