package com.management.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by Lukman.Arogundade on 10/31/2016.
 */


public class AES {

    private final static Logger logger = LoggerFactory.getLogger(AES.class);

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey() {
        MessageDigest sha;
        String myKey = "IsWpAsSW0rd0@$7&878776_+$@$@$@$@@$";

        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            logger.error("setKey  Exception: " + e.getMessage());
            LoggerUtil.logError(logger, e);
        } catch (UnsupportedEncodingException e) {
            logger.error("setKey  Exception: " + e.getMessage());
            LoggerUtil.logError(logger, e);
        }
    }

    public static String encrypt(String strToEncrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {

            logger.error("encrypt  Exception: " + e.getMessage());
            LoggerUtil.logError(logger, e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            logger.error("decrypt  Exception: " + e.getMessage());
            LoggerUtil.logError(logger, e);
        }
        return null;
    }
}
