package com.cloud.vm.snapshot.crypto;

import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Component
public class Aes {

  private static final Base64.Encoder ENCODER = Base64.getEncoder();
  private static final Base64.Decoder DECODER = Base64.getDecoder();

  private static final String ALGORITHM = "AES";
  private static final byte[] KEY_VALUE =
      {'O','5','n','L','y','2','F','O','r','t','E','s','T','3','6','x'};

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
    return new SecretKeySpec(KEY_VALUE, ALGORITHM);
  }

  private static final class AesException extends RuntimeException {
    private static final long serialVersionUID = 8160879326521709344L;

    private AesException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }
}
