import { importSPKI, CompactEncrypt } from 'jose';

export async function fetchPublicKey(): Promise<string> {
  // In a real application, you would handle errors and potentially cache this key
  const res = await fetch('http://localhost:8080/api/v1/crypto/public-key');
  const data = await res.json();
  return data.publicKey;
}

export async function secureFetch(url: string, payload: unknown, publicKeyPem: string, jwtToken: string) {
  // 1. Import the backend's RSA Public Key
  const publicKey = await importSPKI(publicKeyPem, 'RSA-OAEP-256');

  // 2. Wrap the actual payload and the JWT inside the JSON object
  const secretPayload = JSON.stringify({
    data: payload,
    jwt: jwtToken
  });

  // 3. Create JWE payload (Encrypted with RSA-OAEP-256 and AES-256-GCM)
  const jwe = await new CompactEncrypt(new TextEncoder().encode(secretPayload))
    .setProtectedHeader({ alg: 'RSA-OAEP-256', enc: 'A256GCM' })
    .encrypt(publicKey);

  // 4. Send the encrypted JWE string to the backend
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/jose'
    },
    body: jwe
  });
}
