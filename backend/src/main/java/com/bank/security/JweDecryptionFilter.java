package com.bank.security;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class JweDecryptionFilter extends OncePerRequestFilter {

    private final CryptoUtils cryptoUtils;

    public JweDecryptionFilter(CryptoUtils cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Intercept POST/PUT requests specifically meant for JWE (application/jose)
        if (("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) 
                && request.getContentType() != null && request.getContentType().contains("application/jose")) {
            try {
                // 1. Read JWE string from body
                String jweString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                
                if (jweString == null || jweString.trim().isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 2. Parse JWE Object using Nimbus JOSE
                JWEObject jweObject = JWEObject.parse(jweString);

                // 3. Decrypt using RSA Private Key
                RSADecrypter decrypter = new RSADecrypter((RSAPrivateKey) cryptoUtils.getRsaPrivateKey());
                jweObject.decrypt(decrypter);

                // 4. Extract decrypted payload
                // The frontend packs { "data": {...}, "jwt": "..." } inside the JWE
                String decryptedPayload = jweObject.getPayload().toString();
                
                // For demonstration, establish trust in SecurityContextHolder
                // In production, parse the payload to extract and validate the JWT here
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("secure-user", null, Collections.emptyList()));

                // 5. Wrap request with raw decrypted JSON so controllers receive standard @RequestBody
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
