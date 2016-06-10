package pw.itr0.kaba.vault.keystore;

import java.security.InvalidKeyException;
import java.security.KeyStore.Entry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import pw.itr0.kaba.encrypt.PBE;
import pw.itr0.kaba.exception.ImplementationError;
import pw.itr0.kaba.util.CharUtil;

public class PBEKeyEntry implements KeyStoreVaultStorage.Entry {

    private final byte[] secret;

    public PBEKeyEntry(byte[] secret) {
        try {
            this.secret = PBE.getEncrypter().encrypt(secret, "password".toCharArray());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);  // TODO: Auto generated code.
        }
    }

    @Override
    public Entry item() {
        KeySpec spec = new PBEKeySpec(CharUtil.chars(content()));
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHMACSHA512AndAES_128");
            return new SecretKeyEntry(keyFactory.generateSecret(spec));
        } catch (NoSuchAlgorithmException e) {
            throw new ImplementationError("Encryption/decryption algorithm must be statically implemented. This exception must not occur.", e);
        } catch (InvalidKeySpecException e) {
            throw new ImplementationError("Key specification must be type safely implemented. This exception must not occur.", e);
        }
    }

    @Override
    public byte[] content() {
        try {
            return PBE.getDecrypter().decrypt(secret, "password".toCharArray());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
