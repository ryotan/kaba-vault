package pw.itr0.kaba.encrypt;

import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;

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
     * Returns {@link Cipher} to encrypt bytes with {@code password}.
     *
     * @param password encrypting password
     * @return {@link Cipher} to be used on encrypt
     *
     * @throws InvalidKeyException if the {@code password} does not satisfy password specification of using encryption algorithm,
     *                             or encryption algorithm using specified password length is not usable on current JDK
     */
    Cipher getEncryptionCipher(char[] password) throws InvalidKeyException;

    /**
     * Encrypt {@code secret} based on password.
     *
     * @param secret   secret bytes
     * @param password encrypting password
     * @return encrypted secret
     *
     * @throws InvalidKeyException       if the {@code password} does not satisfy password specification of using encryption algorithm,
     *                                   or encryption algorithm using specified password length is not usable on current JDK
     * @throws BadPaddingException       if the {@code secret} is not properly padded
     * @throws IllegalBlockSizeException if the {@code secret} has wrong length
     */
    default byte[] encrypt(byte[] secret, char[] password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return this.getEncryptionCipher(password).doFinal(secret);
    }

    /**
     * Encrypt {@code secret} based on password.
     * <p>
     * Encrypted secret is encoded as Base64.
     *
     * @param secret   secret bytes
     * @param password encrypting password
     * @return encrypted secret (Base64 encoded string)
     *
     * @throws InvalidKeyException       if the {@code password} does not satisfy password specification of using encryption algorithm,
     *                                   or encryption algorithm using specified password length is not usable on current JDK
     * @throws BadPaddingException       if the {@code secret} is not properly padded
     * @throws IllegalBlockSizeException if the {@code secret} has wrong length
     */
    default String encrypt64(byte[] secret, char[] password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return Base64.getEncoder().encodeToString(this.encrypt(secret, password));
    }

    /**
     * Wrap {@link OutputStream} {@code output} to {@link CipherOutputStream} encrypting with {@code password}.
     *
     * @param output   {@link OutputStream} to wrap in {@link CipherOutputStream}
     * @param password encrypting password
     * @return encrypting {@link OutputStream}
     *
     * @throws InvalidKeyException if the {@code password} does not satisfy password specification of using encryption algorithm,
     *                             or encryption algorithm using specified password length is not usable on current JDK
     */
    default OutputStream wrap(OutputStream output, char[] password) throws InvalidKeyException {
        return new CipherOutputStream(output, this.getEncryptionCipher(password));
    }
}
