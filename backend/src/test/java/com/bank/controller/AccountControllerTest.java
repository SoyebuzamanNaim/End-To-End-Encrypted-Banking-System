package com.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AccountController controller = new AccountController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetBalance() throws Exception {
        mockMvc.perform(get("/api/v1/account/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("15000.00"))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void testTransfer() throws Exception {
        String payload = "{\"amount\": \"100\"}";
        mockMvc.perform(post("/api/v1/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }
}
