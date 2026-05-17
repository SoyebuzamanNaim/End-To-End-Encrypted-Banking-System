package com.bank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JweDecryptionFilterTest {

    private JweDecryptionFilter filter;
    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() throws Exception {
        cryptoUtils = new CryptoUtils();
        filter = new JweDecryptionFilter(cryptoUtils);
    }

    @Test
    void testDoFilterInternal_WithPostRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/account/transfer");
        request.setContent("encrypted_jwe_payload".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        verify(filterChain).doFilter(requestCaptor.capture(), eq(response));

        HttpServletRequest wrappedRequest = requestCaptor.getValue();
        BufferedReader reader = wrappedRequest.getReader();
        String decryptedPayload = reader.readLine();
        
        assertEquals("{\"message\": \"This is decrypted payload\"}", decryptedPayload);
    }

    @Test
    void testDoFilterInternal_WithGetRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/account/balance");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
