package com.bank.controller;

import com.bank.security.CryptoUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cache.annotation.Cacheable;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/crypto")
public class PublicKeyController {

    private final CryptoUtils cryptoUtils;

    public PublicKeyController(CryptoUtils cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    @Cacheable("publicKeys")
    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        // Export public key as Base64 encoded SPKI format for 'jose' library
        String encodedKey = Base64.getEncoder().encodeToString(cryptoUtils.getRsaPublicKey().getEncoded());
        String pem = "-----BEGIN PUBLIC KEY-----\n" +
                     encodedKey.replaceAll("(.{64})", "$1\n") +
                     "\n-----END PUBLIC KEY-----";
                     
        return Map.of("publicKey", pem);
    }
}
