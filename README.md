# Close SARC - Backend
Tomáz Maciel, João Vitor Vogel, Isabella Zarosa, Eduardo Camana, Augusto Ely, Diogo Camargo

Repositório para o backend do projeto Close SARC, implementado com arquitetura de microserviços.

## Arquitetura

O projeto consiste em 3 microserviços:
- **auth** (porta 8080): Serviço de autenticação com JWT
- **admin** (porta 8081): Serviço de administração (professores, recursos, turmas)
- **user** (porta 8082): Serviço de usuários (eventos, reservas)

Todos os serviços compartilham o mesmo banco de dados PostgreSQL.

## Pré-requisitos

- Docker e Docker Compose instalados
- Git (para clonar o repositório)

## Como Rodar o Projeto

### 1. Gerar Certificados JWT (Obrigatório)

O serviço de autenticação precisa de chaves RSA para assinar e validar tokens JWT. Execute o seguinte comando para gerar os certificados:

```bash
# Criar diretório para certificados
mkdir -p services/auth/src/main/resources/certs

# Gerar par de chaves RSA (2048 bits)
openssl genrsa -out services/auth/src/main/resources/certs/private_key.pem 2048

# Extrair chave pública
openssl rsa -in services/auth/src/main/resources/certs/private_key.pem -pubout -out services/auth/src/main/resources/certs/public_key.pem
```

### 2. Subir os Serviços com Docker Compose

No diretório raiz do projeto, execute:

```bash
docker-compose up --build
```

Este comando irá:
- Construir as imagens Docker de todos os serviços
- Subir o banco de dados PostgreSQL
- Subir os três microserviços (auth, admin, user)
- Aguardar o banco ficar saudável antes de iniciar os serviços

### 3. Verificar se os Serviços Estão Rodando

Após alguns minutos (tempo de build e inicialização), verifique se os serviços estão funcionando:

- **Auth Service**: http://localhost:8080
- **Admin Service**: http://localhost:8081
- **User Service**: http://localhost:8082

### 4. Acessar a Documentação Swagger

Cada serviço possui documentação Swagger/OpenAPI:

- **Auth Swagger**: http://localhost:8080/swagger-ui.html
- **Admin Swagger**: http://localhost:8081/swagger-ui.html
- **User Swagger**: http://localhost:8082/swagger-ui.html

## Estrutura do Banco de Dados

O banco de dados é inicializado automaticamente com o script `db/init.sql` que cria:
- Tabelas: `professor`, `student`, `admin`, `resource`, `class`, `event`, `reservation`
- Usuário administrador padrão:
  - Email: `admin@example.com`
  - Senha: `admin123`

## Endpoints Principais

## LINKS SWAGGER 
-'http://localhost:8080/swagger-ui/index.html#/'- AUTH
-'http://localhost:8081/swagger-ui/index.html#/'- ADMIN
-'http://localhost:8082/swagger-ui/index.html#/'- USER

### Auth Service (http://localhost:8080)
- `POST /api/auth/login` - Login de usuário/professor ou admin
- `GET /oauth2/jwks` - Endpoint JWKS para validação de tokens

### Admin Service (http://localhost:8081)
- `POST /api/admin/professores` - Cadastrar professor (requer token JWT com role ADMIN)
- `POST /api/admin/recursos` - Cadastrar recurso (requer token JWT com role ADMIN)
- `POST /api/admin/turma` - Cadastrar turma (requer token JWT com role ADMIN)

### User Service (http://localhost:8082)
- Em desenvolvimento...

## Fluxo de Autenticação

1. Faça login no serviço de auth:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email": "admin@example.com", "password": "admin123"}'
   ```

2. Use o token retornado nas requisições aos outros serviços:
   ```bash
   curl -X POST http://localhost:8081/api/admin/professores \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer SEU_TOKEN_AQUI" \
     -d '{"name": "João Silva", "email": "joao@example.com", "password": "senha123"}'
   ```

## Comandos Úteis

### Parar os serviços
```bash
docker-compose down
```

### Parar e remover volumes (apaga o banco de dados)
```bash
docker-compose down -v
```

### Ver logs dos serviços
```bash
docker-compose logs -f [nome_do_servico]
# Exemplo: docker-compose logs -f auth
```

### Reconstruir apenas um serviço
```bash
docker-compose up --build [nome_do_servico]
# Exemplo: docker-compose up --build auth
```

## Desenvolvimento Local (sem Docker)

Se preferir rodar localmente sem Docker:

1. Tenha um PostgreSQL rodando na porta 5432
2. Crie o banco de dados `appdb`
3. Execute o script `db/init.sql` no banco
4. Configure as variáveis de ambiente ou edite `application.properties` de cada serviço
5. Execute cada serviço com Maven:
   ```bash
   cd services/auth && mvn spring-boot:run
   cd services/admin && mvn spring-boot:run
   cd services/user && mvn spring-boot:run
   ```

## Troubleshooting

### Erro: "Cannot find classpath:certs/private_key.pem"
- Certifique-se de ter gerado os certificados JWT (passo 1)

### Erro: "Connection refused" no banco de dados
- Aguarde o PostgreSQL inicializar completamente
- Verifique se a porta 5432 não está em uso

### Erro: "Port already in use"
- Pare os serviços: `docker-compose down`
- Verifique se há processos usando as portas 8080, 8081, 8082, 5432

### Serviços não iniciam
- Verifique os logs: `docker-compose logs [nome_do_servico]`
- Certifique-se de que o Docker tem memória suficiente (recomendado: 4GB+)
