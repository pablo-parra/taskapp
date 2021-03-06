package com.example.taskapp.authmanagement.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Class to read the content of PEM files
 */
@Slf4j
public class PemUtils {

    private static byte[] parsePEMReader(Reader pemReader) throws IOException {
        try(PemReader reader = new PemReader(pemReader)){
            PemObject pemObject = reader.readPemObject();
            return pemObject.getContent();
        }
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
        PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not reconstruct the private key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            log.error("Could not reconstruct the private key");
        }
        return privateKey;
    }

    /**
     * Reads the content of a PEM file
     * @param reader the {@link Reader}
     * @param algorithm the algorithm to be used
     * @return the {@link PrivateKey}
     * @throws IOException if error occurs
     */
    public static PrivateKey readPrivateKeyFromReader(Reader reader, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMReader(reader);
        return PemUtils.getPrivateKey(bytes, algorithm);
    }

    /**
     * The Constructor
     */
    private PemUtils(){

    }

}
