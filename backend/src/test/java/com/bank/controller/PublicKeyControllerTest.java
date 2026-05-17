package com.bank.controller;

import com.bank.security.CryptoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicKeyControllerTest {

    private MockMvc mockMvc;
    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() throws Exception {
        cryptoUtils = new CryptoUtils();
        PublicKeyController controller = new PublicKeyController(cryptoUtils);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetPublicKey() throws Exception {
        mockMvc.perform(get("/api/v1/crypto/public-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicKey").exists())
                .andExpect(jsonPath("$.publicKey").isString());
    }
}
