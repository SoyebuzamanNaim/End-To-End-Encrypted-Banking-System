package com.bank.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Converter
@Component
public class CryptoConverter implements AttributeConverter<String, String> {

    private static CryptoUtils cryptoUtils;
    private static String secretKey;

    public CryptoConverter(CryptoUtils utils, @Value("${app.security.db-encryption-key}") String key) {
        CryptoConverter.cryptoUtils = utils;
        CryptoConverter.secretKey = key;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return cryptoUtils.encryptAesGcm(attribute, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt database column", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return cryptoUtils.decryptAesGcm(dbData, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt database column", e);
        }
    }
}
