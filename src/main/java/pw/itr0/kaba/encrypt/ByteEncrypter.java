package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Base64;

public interface ByteEncrypter {
    byte[] encrypt(char[] password, byte[] secret) throws GeneralSecurityException;
    default String encrypt64(char[] password, byte[] secret) throws GeneralSecurityException {
        return Base64.getEncoder().encodeToString(encrypt(password, secret));
    }
}
