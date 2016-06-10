package pw.itr0.kaba.vault.keystore;

import java.security.InvalidKeyException;
import java.security.KeyStore.Entry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import pw.itr0.kaba.encrypt.PBE;
import pw.itr0.kaba.exception.ImplementationError;
import pw.itr0.kaba.util.CharUtil;

public class PBEKeyEntry implements KeyStoreVaultStorage.Entry {

    private final char[] password;
    private final byte[] secret;

    public PBEKeyEntry(byte[] secret) {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            this.password = Base64.encode(bytes).toCharArray();
            this.secret = PBE.getEncrypter().encrypt(secret, password);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);  // TODO: Auto generated code.
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to create instance of java.security.SecureRandom." +
                    " Maybe strong strong SecureRandom implementation is not available on current JDK.", e);
        }
    }

    @Override
    public Entry item() {
        return new SecretKeyEntry(PBE.getSecretKey(CharUtil.chars(content())));
    }

    @Override
    public byte[] content() {
        try {
            return PBE.getDecrypter().decrypt(secret, password);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new ImplementationError("Maybe secret bytes or password is not same as the one used on encrypt.", e);
        }
    }
}
