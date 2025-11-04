package com.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JwksControllerTest {

    @Mock
    private JWKSet jwkSet;

    @InjectMocks
    private JwksController jwksController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jwksController).build();
    }

    @Test
    void testJwks_ShouldReturnJwkSet() throws Exception {
        Map<String, Object> jwkSetMap = new HashMap<>();
        jwkSetMap.put("keys", new Object[]{});

        when(jwkSet.toJSONObject()).thenReturn(jwkSetMap);

        mockMvc.perform(get("/oauth2/jwks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys").exists());
    }
}

