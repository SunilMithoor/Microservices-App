package com.app.service;

import com.app.config.LoggerService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.app.utils.Utils.tagMethodName;


@Service
public class EncryptDecryptServiceImpl implements EncryptDecryptService {

    private static final String TAG = "EncryptDecryptServiceImpl";
    private static final String KEY_FOLDER = "src/main/resources/keys/";
    private static final String PUBLIC_KEY_FILE = KEY_FOLDER + "public_key.pem";
    private static final String PRIVATE_KEY_FILE = KEY_FOLDER + "private_key.pem";
    private final LoggerService logger;

    public EncryptDecryptServiceImpl(LoggerService logger) {
        this.logger = logger;
        initializeKeys();
    }

    /**
     * Generate RSA key pair if not already present
     */
    private void initializeKeys() {
        String methodName = "initializeKeys";
        try {
            if (!Files.exists(Paths.get(PUBLIC_KEY_FILE)) || !Files.exists(Paths.get(PRIVATE_KEY_FILE))) {
                logger.info(tagMethodName(TAG, methodName), "Keys not found, generating new RSA key pair...");
                createAndStoreKeys();
            } else {
                logger.info(tagMethodName(TAG, methodName), "Existing keys found in resources/keys/");
            }
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Error initializing keys", e);
        }
    }

    /**
     * Generates RSA public/private keys and stores them in the resources folder.
     */
    public void createAndStoreKeys() {
        String methodName = "createAndStoreKeys";
        logger.info(tagMethodName(TAG, methodName), "Generating RSA key pair");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            Files.createDirectories(Paths.get(KEY_FOLDER));

            // Save public key
            String pubKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
                    "\n-----END PUBLIC KEY-----";
            Files.write(Paths.get(PUBLIC_KEY_FILE), pubKeyPEM.getBytes());

            // Save private key
            String privKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                    "\n-----END PRIVATE KEY-----";
            Files.write(Paths.get(PRIVATE_KEY_FILE), privKeyPEM.getBytes());

            logger.info(tagMethodName(TAG, methodName), "Keys stored successfully in resources/keys/");
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to generate/store keys", e);
        }
    }

    /**
     * Encrypt message using the public key
     */
    public String encryptMessage(String plainText) {
        String methodName = "encryptMessage";
        logger.info(tagMethodName(TAG, methodName), "Encrypting message");
        try {
            PublicKey publicKey = readPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to encrypt message", e);
        }
        return "";
    }

    /**
     * Decrypt message using the private key
     */
    public String decryptMessage(String encryptedMessage) {
        String methodName = "decryptMessage";
        logger.info(tagMethodName(TAG, methodName), "Decrypting message");
        try {
            PrivateKey privateKey = readPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            return new String(decryptedBytes);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to decrypt message", e);
        }
        return "";
    }

    /**
     * Reads private key from PEM file
     */
    private PrivateKey readPrivateKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    /**
     * Reads public key from PEM file
     */
    private PublicKey readPublicKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
