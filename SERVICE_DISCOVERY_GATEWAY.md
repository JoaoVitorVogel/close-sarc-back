# Service Discovery e Gateway API - Documentação

## Visão Geral

Este projeto agora utiliza **Spring Cloud** para implementar:
- **Service Discovery** (Eureka Server) - Descoberta automática de serviços
- **API Gateway** (Spring Cloud Gateway) - Ponto único de entrada para todos os serviços

## Arquitetura

```
┌─────────────┐
│   Gateway   │ (porta 8080)
│   Service   │
└──────┬──────┘
       │
       ├─────────────────┬─────────────────┐
       │                 │                 │
┌──────▼──────┐   ┌──────▼──────┐   ┌──────▼──────┐
│ Auth Service│   │Admin Service│   │User Service │
│  (porta     │   │  (porta     │   │  (porta     │
│   8083)     │   │   8081)     │   │   8082)     │
└──────┬──────┘   └──────┬──────┘   └──────┬──────┘
       │                 │                 │
       └─────────────────┴─────────────────┘
                         │
                 ┌───────▼───────┐
                 │ Eureka Server │
                 │  (porta 8761)  │
                 └────────────────┘
```

## Serviços

### 1. Discovery Service (Eureka Server)
- **Porta**: 8761
- **URL**: http://localhost:8761
- **Função**: Registra e descobre todos os microserviços
- **Dashboard**: http://localhost:8761 (interface web do Eureka)

### 2. Gateway Service (Spring Cloud Gateway)
- **Porta**: 8080
- **URL**: http://localhost:8080
- **Função**: Roteia requisições para os serviços apropriados

### 3. Auth Service
- **Porta Interna**: 8083
- **Registrado como**: `auth-service`
- **Endpoints via Gateway**: 
  - `POST /api/auth/login`
  - `GET /oauth2/jwks`

### 4. Admin Service
- **Porta Interna**: 8081
- **Registrado como**: `admin-service`
- **Endpoints via Gateway**: 
  - `POST /api/admin/professores`
  - `POST /api/admin/recursos`
  - `POST /api/admin/turma`

### 5. User Service
- **Porta Interna**: 8082
- **Registrado como**: `user-service`
- **Endpoints via Gateway**: 
  - `GET /api/user/recursos`
  - `POST /api/user/reservas`
  - `POST /api/user/eventos`

## Rotas do Gateway

O Gateway está configurado para rotear automaticamente baseado nos paths:

| Path | Serviço de Destino | Exemplo |
|------|-------------------|---------|
| `/api/auth/**` | auth-service | `http://localhost:8080/api/auth/login` |
| `/api/admin/**` | admin-service | `http://localhost:8080/api/admin/professores` |
| `/api/user/**` | user-service | `http://localhost:8080/api/user/recursos` |
| `/oauth2/jwks` | auth-service | `http://localhost:8080/oauth2/jwks` |

## Como Usar

### 1. Subir todos os serviços

```bash
docker-compose up --build
```

### 2. Verificar Service Discovery

Acesse o dashboard do Eureka:
```
http://localhost:8761
```

Você deve ver todos os serviços registrados:
- `GATEWAY-SERVICE`
- `AUTH-SERVICE`
- `ADMIN-SERVICE`
- `USER-SERVICE`

### 3. Usar o Gateway

Todas as requisições agora devem passar pelo Gateway na porta 8080:

**Exemplo de Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com", "password": "admin123"}'
```

**Exemplo de Requisição Autenticada:**
```bash
curl -X POST http://localhost:8080/api/admin/professores \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{"name": "João Silva", "email": "joao@example.com", "password": "senha123"}'
```

## Vantagens da Implementação

1. **Service Discovery**: Os serviços se descobrem automaticamente sem precisar conhecer IPs/portas hardcoded
2. **Load Balancing**: O Gateway distribui automaticamente a carga entre instâncias do mesmo serviço
3. **Ponto Único de Entrada**: Todas as requisições passam pelo Gateway, facilitando:
   - Autenticação centralizada
   - Rate limiting
   - Logging centralizado
   - CORS
4. **Resiliência**: Se um serviço cair, o Eureka detecta e remove do registro
5. **Escalabilidade**: Fácil adicionar novas instâncias de serviços

## Configurações Importantes

### Eureka Client Configuration
Todos os serviços clientes (auth, admin, user, gateway) estão configurados com:
```properties
eureka.client.service-url.defaultZone=http://discovery:8761/eureka/
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
```

### Gateway Load Balancing
O Gateway usa `lb://` prefix para fazer load balancing:
```yaml
spring.cloud.gateway.routes[0].uri=lb://auth-service
```

## Portas Expostas

| Serviço | Porta Externa | Porta Interna |
|---------|--------------|---------------|
| Gateway | 8080 | 8080 |
| Discovery | 8761 | 8761 |
| Auth | 8083 | 8083 |
| Admin | 8081 | 8081 |
| User | 8082 | 8082 |
| PostgreSQL | 5432 | 5432 |
| Prometheus | 9090 | 9090 |
| Grafana | 3000 | 3000 |

## Notas Importantes

1. **Porta do Auth Service**: Mudou de 8080 para 8083 para evitar conflito com o Gateway
2. **Acesso Direto**: Os serviços ainda podem ser acessados diretamente pelas portas internas, mas é recomendado usar o Gateway
3. **Service Discovery**: O Eureka precisa estar rodando antes dos outros serviços se registrarem
4. **Health Checks**: O Eureka monitora a saúde dos serviços automaticamente

