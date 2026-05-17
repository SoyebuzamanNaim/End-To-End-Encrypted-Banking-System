package com.bank.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    private CryptoUtils cryptoUtils;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        cryptoUtils = new CryptoUtils();
    }

    @Test
    void testRsaKeyPairGeneration() {
        assertNotNull(cryptoUtils.getRsaPublicKey(), "Public key should not be null");
        assertNotNull(cryptoUtils.getRsaPrivateKey(), "Private key should not be null");
    }

    @Test
    void testAesGcmEncryptionAndDecryption() throws Exception {
        String secretKeyBase64 = Base64.getEncoder().encodeToString("12345678901234567890123456789012".getBytes());
        String plainText = "Sensitive Data 123";
        
        String cipherText = cryptoUtils.encryptAesGcm(plainText, secretKeyBase64);
        assertNotNull(cipherText, "Cipher text should not be null");
        assertNotEquals(plainText, cipherText, "Cipher text should differ from plain text");

        String decryptedText = cryptoUtils.decryptAesGcm(cipherText, secretKeyBase64);
        assertEquals(plainText, decryptedText, "Decrypted text should match original plain text");
    }

    @Test
    void testAesGcmDecryptionWithWrongKey() throws Exception {
        String secretKeyBase64 = Base64.getEncoder().encodeToString("12345678901234567890123456789012".getBytes());
        String wrongKeyBase64 = Base64.getEncoder().encodeToString("00000000000000000000000000000000".getBytes());
        String plainText = "Sensitive Data";
        
        String cipherText = cryptoUtils.encryptAesGcm(plainText, secretKeyBase64);
        
        assertThrows(Exception.class, () -> {
            cryptoUtils.decryptAesGcm(cipherText, wrongKeyBase64);
        }, "Should throw exception when decrypting with wrong key");
    }
}
