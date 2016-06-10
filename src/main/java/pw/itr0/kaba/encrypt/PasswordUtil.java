package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Encryption utility for password of vaults, such as the password used to unlock the keystore.
 * <p>
 * Encrypt / decrypt password algorithm using PBE.
 *
 * @author ryotan
 * @since 1.0.0
 */
public final class PasswordUtil {

    /**
     * Error message for null password argument.
     */
    private static final String PASSWORD_MUST_NOT_BE_NULL = "password must not be null";

    /**
     * Prefix of sealed password string.
     */
    private static final String SEALED_PASSWORD_PREFIX = "SEALED:V1:";

    /**
     * Hidden constructor for utility class.
     */
    private PasswordUtil() {
        // nop
    }

    /**
     * Seal the {@code password} byte array.
     *
     * @param password encryption target
     * @return PBE encrypted password
     *
     * @throws GeneralSecurityException on encryption failure
     */
    public static String seal(byte[] password) throws GeneralSecurityException {
        Objects.requireNonNull(password, PASSWORD_MUST_NOT_BE_NULL);

        char[] notSecretPassword = new char[] {
                '#', '#', '#', ' ', 'n', 'o', 't', ' ', 's', 'o', ' ', 'm', 'u', 'c', 'h', ' ', 's', 'e', 'c', 'r', 'e', 't', ' ', 'p', 'a', 's',
                's', 'w', 'o', 'r', 'd', ' ', '*', '*', '*'
        };
        String encrypted = PBE.getEncrypter().encrypt64(password, notSecretPassword);
        Arrays.fill(notSecretPassword, (char) 0x00); // セキュリティ情報なので上書き削除しておく。

        return SEALED_PASSWORD_PREFIX + encrypted;
    }

    /**
     * Unseal the {@code password} string.
     * <p>
     * If {@code password} is prepended with {@code SEALED:V1:}, {@code password} will be unsealed using PBE.
     * Otherwise, password is unsealed using {@link String#getBytes()}.
     *
     * @param password decryption target
     * @return decrypted password
     *
     * @throws GeneralSecurityException on decryption failure
     */
    public static byte[] unseal(String password) throws GeneralSecurityException {
        Objects.requireNonNull(password, PASSWORD_MUST_NOT_BE_NULL);

        if (!isSealed(password)) {
            return password.getBytes();
        }

        char[] notSecretPassword = new char[] {
                '#', '#', '#', ' ', 'n', 'o', 't', ' ', 's', 'o', ' ', 'm', 'u', 'c', 'h', ' ', 's', 'e', 'c', 'r', 'e', 't', ' ', 'p', 'a', 's',
                's', 'w', 'o', 'r', 'd', ' ', '*', '*', '*'
        };
        byte[] decrypted = PBE.getDecrypter().decrypt64(password.substring(SEALED_PASSWORD_PREFIX.length(), password.length()), notSecretPassword);
        Arrays.fill(notSecretPassword, (char) 0x00); // セキュリティ情報なので上書き削除しておく。

        return decrypted;
    }

    /**
     * Return {@code true} if password is sealed.
     *
     * @param password possibly encrypted password
     * @return {@code true} if password is sealed
     */
    private static boolean isSealed(String password) {
        return password.startsWith(SEALED_PASSWORD_PREFIX);
    }
}
