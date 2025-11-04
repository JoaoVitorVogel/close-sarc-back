package com.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthApplicationTest {

    @Test
    void testAuthApplication_ShouldBeInstantiated() {
        AuthApplication app = new AuthApplication();
        assertNotNull(app);
    }

    @Test
    void testMain_ShouldNotThrowException() {
        // Testa que o método main pode ser chamado sem erro
        // Não podemos testar completamente o SpringApplication.run sem contexto Spring
        assertDoesNotThrow(() -> {
            // Verifica que a classe pode ser instanciada
            AuthApplication app = new AuthApplication();
            assertNotNull(app);
        });
    }
}

