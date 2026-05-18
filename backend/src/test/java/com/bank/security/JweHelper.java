package com.bank.security;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import java.security.interfaces.RSAPublicKey;
public class JweHelper {
    public static String createJwe(String payload, RSAPublicKey publicKey) throws Exception {
        JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
        Payload jwePayload = new Payload(payload);
        JWEObject jweObject = new JWEObject(header, jwePayload);
        jweObject.encrypt(new RSAEncrypter(publicKey));
        return jweObject.serialize();
    }
}
