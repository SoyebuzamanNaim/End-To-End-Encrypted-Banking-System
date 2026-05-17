package com.bank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class JweDecryptionFilter extends OncePerRequestFilter {

    private final CryptoUtils cryptoUtils;

    public JweDecryptionFilter(CryptoUtils cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // For demonstration: intercept POST/PUT to decrypt JWE payload
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            try {
                // 1. Read JWE string from body (pseudo-code)
                // 2. Decrypt using cryptoUtils.getRsaPrivateKey()
                // 3. Extract JWT, validate it, and set SecurityContextHolder
                // 4. Wrap request with raw decrypted JSON
                
                String decryptedPayload = "{\"message\": \"This is decrypted payload\"}"; // Mock
                
                HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                    @Override
                    public BufferedReader getReader() throws IOException {
                        return new BufferedReader(new InputStreamReader(
                                new ByteArrayInputStream(decryptedPayload.getBytes(StandardCharsets.UTF_8))));
                    }
                };
                filterChain.doFilter(wrappedRequest, response);
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
