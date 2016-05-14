package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Objects;

public class PasswordUtil {
    public static String seal(byte[] password) throws GeneralSecurityException {
        Objects.requireNonNull(password, "password must not be null");

        char[] notSecretPassword = new char[]{
                '#', '#', '#', ' ', 'n', 'o', 't', ' ', 's', 'o', ' ', 'm', 'u', 'c', 'h', ' ', 's', 'e', 'c', 'r', 'e', 't', ' ', 'p', 'a', 's',
                's', 'w', 'o', 'r', 'd', ' ', '*', '*', '*'
        };
        String encrypted = PBE.getEncrypter().encrypt64(notSecretPassword, password);
        Arrays.fill(notSecretPassword, (char) 0x00); // セキュリティ情報なので上書き削除しておく。

        return "SEALED:V1:" + encrypted;
    }

    public static byte[] unseal(String password) throws GeneralSecurityException {
        Objects.requireNonNull(password, "password must not be null");

        if (!isSealed(password)) {
            return password.getBytes();
        }

        char[] notSecretPassword = new char[]{
                '#', '#', '#', ' ', 'n', 'o', 't', ' ', 's', 'o', ' ', 'm', 'u', 'c', 'h', ' ', 's', 'e', 'c', 'r', 'e', 't', ' ', 'p', 'a', 's',
                's', 'w', 'o', 'r', 'd', ' ', '*', '*', '*'
        };
        byte[] decrypted = PBE.getDecrypter().decrypt64(notSecretPassword, password.substring(10, password.length()));
        Arrays.fill(notSecretPassword, (char) 0x00); // セキュリティ情報なので上書き削除しておく。

        return decrypted;
    }

    private static boolean isSealed(String password) {
        Objects.requireNonNull(password, "password must not be null");
        return 10 < password.length() && password.startsWith("SEALED:");
    }

    private PasswordUtil() {
    }
}
