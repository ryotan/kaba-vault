package pw.itr0.kaba.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import java.io.InputStream;
import java.security.InvalidKeyException;
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
     * Returns {@link Cipher} to decrypt bytes with {@code password}.
     *
     * @param password decrypting password
     * @return {@link Cipher} to be used on decrypt
     * @throws InvalidKeyException if the {@code password} does not satisfy password specification of using decryption algorithm,
     *                             or decryption algorithm using specified password length is not usable on current JDK
     */
    Cipher getDecryptionCipher(char[] password) throws InvalidKeyException;

    /**
     * Decrypt {@code secret} based on password.
     *
     * @param password decrypting password
     * @param secret   secret bytes
     * @return decrypted secret
     * @throws InvalidKeyException       if the {@code password} does not satisfy password specification of using decrypting algorithm,
     *                                   or decrypting algorithm using specified password length is not usable on current JDK
     * @throws BadPaddingException       if the {@code secret} is not properly padded
     * @throws IllegalBlockSizeException if the {@code secret} has wrong length
     */
    default byte[] decrypt(char[] password, byte[] secret)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return getDecryptionCipher(password).doFinal(secret);
    }

    /**
     * Decrypt {@code secret} based on password.
     * <p>
     * Encrypted secret is encoded as Base64.
     *
     * @param password decrypting password
     * @param secret   secret bytes (Base64 encoded string)
     * @return decrypted secret
     * @throws InvalidKeyException       if the {@code password} does not satisfy password specification of using decrypting algorithm,
     *                                   or decrypting algorithm using specified password length is not usable on current JDK
     * @throws BadPaddingException       if the {@code secret} is not properly padded
     * @throws IllegalBlockSizeException if the {@code secret} has wrong length
     */
    default byte[] decrypt64(char[] password, String secret)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return this.decrypt(password, Base64.getDecoder().decode(secret));
    }

    /**
     * Wrap {@link InputStream} {@code in} to {@link CipherInputStream} decrypting with {@code password}.
     *
     * @param in       {@link InputStream} to wrap in {@link CipherInputStream}
     * @param password decrypting password
     * @return decrypting {@link InputStream}
     * @throws InvalidKeyException if the {@code password} does not satisfy password specification of using decrypting algorithm,
     *                             or decrypting algorithm using specified password length is not usable on current JDK
     */
    default InputStream wrap(InputStream in, char[] password) throws InvalidKeyException {
        return new CipherInputStream(in, getDecryptionCipher(password));
    }
}
