# Changelog

## [Unreleased]
### Fixed
- Fixed backend test `BankingSystemApplicationTests` by providing `application.properties` with an H2 in-memory database and test configurations for missing `app.security.db-encryption-key` placeholder.
- Added missing H2 database dependency in test scope in `pom.xml`.
- Fixed `AccountControllerTest.testTransfer` by adding `toAccount` field in the mock request payload to satisfy `@NotBlank` validation rules, resolving the 400 Bad Request error.
- Fixed `JweDecryptionFilterTest.testDoFilterInternal_WithPostRequest` by simulating an actual JWE payload encryption using `CryptoUtils`'s RSA public key and specifying the `application/jose` content type, satisfying the filter logic and successfully asserting decryption.
- Added a local default fallback for `SUPABASE_DB_URL` placeholder in `application.properties` to prevent startup crashes when the environment variable is missing.
- Fixed frontend ESLint errors by resolving `react-hooks/set-state-in-effect` and `@typescript-eslint/no-explicit-any` warnings in the Next.js app.
