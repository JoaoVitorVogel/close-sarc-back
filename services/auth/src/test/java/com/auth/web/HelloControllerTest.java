package com.auth.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HelloControllerTest {

    @InjectMocks
    private HelloController helloController;

    @Test
    void testHello_ShouldReturnMessage() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(helloController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auth service is running"));
    }
}

