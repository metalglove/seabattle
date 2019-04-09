package domain.cryptography;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PBKDF2 {
  private static final int ITERATIONS = 10000;
  private static final int SALT_SIZE = 32;
  private static final int HASH_SIZE = 512;
  public static final String ALGORITHM = "PBKDF2WithHmacSHA1";

  public static byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_SIZE];
    random.nextBytes(salt);
    return salt;
  }

  public static byte[] calculateHash(SecretKeyFactory skf, char[] password, byte[] salt) throws InvalidKeySpecException {
    PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE);
    return skf.generateSecret(spec).getEncoded();
  }

  public static boolean verifyPassword(SecretKeyFactory skf, byte[] originalHash, char[] password, byte[] salt) throws InvalidKeySpecException {
    byte[] comparisonHash = calculateHash(skf, password, salt);
    return comparePasswords(originalHash, comparisonHash);
  }

  /**
   * Compares the two byte arrays in length-constant time using XOR.
   *
   * @param originalHash   The original password hash
   * @param comparisonHash The comparison password hash
   * @return True if both match, false otherwise
   */
  private static boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
    int diff = originalHash.length ^ comparisonHash.length;
    for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
      diff |= originalHash[i] ^ comparisonHash[i];
    }

    return diff == 0;
  }
}
