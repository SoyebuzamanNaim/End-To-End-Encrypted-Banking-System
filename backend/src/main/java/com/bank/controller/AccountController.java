package com.bank.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @GetMapping("/balance")
    public Map<String, String> getBalance() {
        return Map.of("balance", "15000.00", "currency", "USD", "status", "Encrypted at rest");
    }
    
    @PostMapping("/transfer")
    public Map<String, String> transfer(@RequestBody Map<String, Object> payload) {
        // Payload has been decrypted by JweDecryptionFilter
        return Map.of("status", "success", "message", "Secure transfer executed");
    }
}
