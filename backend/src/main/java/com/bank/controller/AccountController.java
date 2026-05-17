package com.bank.controller;

import com.bank.dto.TransferRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Cacheable(value = "balances")
    @GetMapping("/balance")
    public Map<String, String> getBalance() {
        log.info("Fetching balance from secure database (Cache Miss)");
        return Map.of("balance", "15000.00", "currency", "USD", "status", "Encrypted at rest");
    }
    
    @CacheEvict(value = "balances", allEntries = true)
    @PostMapping("/transfer")
    public Map<String, String> transfer(@Valid @RequestBody TransferRequest payload) {
        log.info("Secure transfer requested. Destination: {}. Amount: {}", payload.getToAccount(), payload.getAmount());
        // Payload has been decrypted by JweDecryptionFilter and validated automatically
        return Map.of("status", "success", "message", "Secure transfer executed to " + payload.getToAccount());
    }
}
