package com.cloud.vm.snapshot.crypto;

import static java.lang.Thread.currentThread;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Component
public class Aes {

  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Base64.Decoder DECODER = Base64.getDecoder();

  private static final String AES_PROPERTIES = "META-INF/conf/aes.properties";
  private static final String ALGORITHM = "AES";

  private Aes(){
  }

  public static String encrypt(final String value){
    try {
      final Key key = generateKey();

      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);

      final byte[] encryptValue = cipher.doFinal(value.getBytes());

      return ENCODER.encodeToString(encryptValue);
    } catch (final IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException |
        NoSuchPaddingException | BadPaddingException cause) {
      throw new AesException("Could not encrypt password. ", cause);
    }
  }

  public static String decrypt(final String value){
    try {
      final Key key = generateKey();

      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);

      final byte[] decodedValue = DECODER.decode(value);

      return new String(cipher.doFinal(decodedValue));
    } catch (final InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException |
        NoSuchPaddingException | BadPaddingException cause) {
      throw new AesException("Could not decrypt password. ", cause);
    }
  }

  private static Key generateKey(){
    final ClassLoader classLoader = currentThread().getContextClassLoader();

    try (final InputStream input = classLoader.getResourceAsStream(AES_PROPERTIES)){
      final Properties config = new Properties();
      config.load(input);
      final String secretKey = config.getProperty("secret.key");

      return new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    } catch (final IOException cause) {
      throw new AesException("Could not open configuration file. ", cause);
    }
  }

  private static final class AesException extends RuntimeException {
    private static final long serialVersionUID = 8160879326521709344L;

    private AesException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }
}
