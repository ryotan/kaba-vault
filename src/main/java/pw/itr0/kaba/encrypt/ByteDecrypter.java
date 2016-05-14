package pw.itr0.kaba.encrypt;

import java.security.GeneralSecurityException;
import java.util.Base64;

public interface ByteDecrypter {
    byte[] decrypt(char[] password, byte[] secret) throws GeneralSecurityException;
    default byte[] decrypt64(char[] password, String secret) throws GeneralSecurityException {
        return decrypt(password, Base64.getDecoder().decode(secret));
    }
}
