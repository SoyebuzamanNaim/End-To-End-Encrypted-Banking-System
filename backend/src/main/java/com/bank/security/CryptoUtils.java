package com.bank.security;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Component
public class CryptoUtils {

    private static final String AES_ALGO = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private KeyPair rsaKeyPair;

    public CryptoUtils() throws NoSuchAlgorithmException {
        // Generate RSA Key Pair for JWE Transport Security
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        this.rsaKeyPair = keyGen.generateKeyPair();
    }

    public PublicKey getRsaPublicKey() {
        return rsaKeyPair.getPublic();
    }

    public PrivateKey getRsaPrivateKey() {
        return rsaKeyPair.getPrivate();
    }

    // --- AES-256-GCM for Database Field-Level Encryption ---

    public String encryptAesGcm(String plainText, String secretKeyBase64) throws Exception {
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyBase64);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");

        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_ALGO);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    public String decryptAesGcm(String encryptedTextBase64, String secretKeyBase64) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(encryptedTextBase64);
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyBase64);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");

        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(AES_ALGO);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }
}
