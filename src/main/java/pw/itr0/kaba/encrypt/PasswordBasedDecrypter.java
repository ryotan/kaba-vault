package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Interface for class to decrypt byte array based on password.
 * <p>
 * This interface provides default implementation to decrypt from Base64 encoded string.
 *
 * @author ryotan
 * @since 1.0.0
 */
public interface PasswordBasedDecrypter {
    /**
     * Decrypt {@code secret} based on password.
     *
     * @param password decrypting password
     * @param secret   secret bytes
     * @return decrypted secret
     * @throws GeneralSecurityException on decryption failure
     */
    byte[] decrypt(char[] password, byte[] secret) throws GeneralSecurityException;

    /**
     * Decrypt {@code secret} based on password.
     * <p>
     * Encrypted secret is encoded as Base64.
     *
     * @param password decrypting password
     * @param secret   secret bytes (Base64 encoded string)
     * @return decrypted secret
     * @throws GeneralSecurityException on decryption failure
     */
    default byte[] decrypt64(char[] password, String secret) throws GeneralSecurityException {
        return this.decrypt(password, Base64.getDecoder().decode(secret));
    }
}
