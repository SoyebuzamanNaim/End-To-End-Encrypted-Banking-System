package com.bank.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoConverterTest {

    private CryptoConverter cryptoConverter;
    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() throws Exception {
        cryptoUtils = new CryptoUtils();
        String secretKey = Base64.getEncoder().encodeToString("12345678901234567890123456789012".getBytes());
        cryptoConverter = new CryptoConverter(cryptoUtils, secretKey);
    }

    @Test
    void testConvertToDatabaseColumn() {
        String plainText = "100.50";
        String cipherText = cryptoConverter.convertToDatabaseColumn(plainText);
        
        assertNotNull(cipherText);
        assertNotEquals(plainText, cipherText);
        
        // Null handling
        assertNull(cryptoConverter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute() {
        String plainText = "100.50";
        String cipherText = cryptoConverter.convertToDatabaseColumn(plainText);
        
        String decryptedText = cryptoConverter.convertToEntityAttribute(cipherText);
        assertEquals(plainText, decryptedText);
        
        // Null handling
        assertNull(cryptoConverter.convertToEntityAttribute(null));
    }
}
