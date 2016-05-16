package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Interface for class to encrypt byte array based on password.
 * <p>
 * This interface provides default implementation to return encrypted byte array as Base64 string.
 *
 * @author ryotan
 * @since 1.0.0
 */
public interface PasswordBasedEncrypter {
    /**
     * Encrypt {@code secret} based on password.
     *
     * @param password encrypting password
     * @param secret   secret bytes
     * @return encrypted secret
     * @throws GeneralSecurityException on encryption failure
     */
    byte[] encrypt(char[] password, byte[] secret) throws GeneralSecurityException;

    /**
     * Encrypt {@code secret} based on password.
     * <p>
     * Encrypted secret is encoded as Base64.
     *
     * @param password encrypting password
     * @param secret   secret bytes
     * @return encrypted secret (Base64 encoded string)
     * @throws GeneralSecurityException on encryption failure
     */
    default String encrypt64(char[] password, byte[] secret) throws GeneralSecurityException {
        return Base64.getEncoder().encodeToString(this.encrypt(password, secret));
    }
}
