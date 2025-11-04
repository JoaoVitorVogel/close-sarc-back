#!/bin/bash

# Script para gerar chaves RSA para JWT
# Execute este script antes de rodar o projeto

echo "🔐 Gerando chaves RSA para JWT..."

# Criar diretório se não existir
mkdir -p services/auth/src/main/resources/certs

# Gerar chave privada RSA (2048 bits)
echo "📝 Gerando chave privada..."
openssl genrsa -out services/auth/src/main/resources/certs/private_key.pem 2048

# Extrair chave pública
echo "📝 Extraindo chave pública..."
openssl rsa -in services/auth/src/main/resources/certs/private_key.pem -pubout -out services/auth/src/main/resources/certs/public_key.pem

echo "✅ Chaves geradas com sucesso!"
echo "📁 Localização: services/auth/src/main/resources/certs/"
echo ""
echo "Agora você pode rodar o projeto com: docker-compose up --build"

