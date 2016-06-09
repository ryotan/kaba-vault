package pw.itr0.kaba.vault.keystore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import pw.itr0.kaba.keystore.impl.PlainPasswordKeyStore;
import pw.itr0.kaba.util.CharUtil;
import pw.itr0.kaba.vault.Vault;

public class SecretKeyStoreVault implements Vault<byte[]> {

    private final KeyStore keyStore;

    public SecretKeyStoreVault(Path path, char[] password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore = new PlainPasswordKeyStore(KeyStore.getInstance("JCEKS"));
        if (Files.exists(path)) {
            try (InputStream file = Files.newInputStream(path)) {
                keyStore.load(file, password);
            }
        } else {
            keyStore.load(null, password);
            try (OutputStream file = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
                keyStore.store(file, password);
            }
        }
    }

    @Override
    public List<String> list() throws KeyStoreException {
        return Collections.list(keyStore.aliases());
    }

    @Override
    public void store(String name, byte[] secret) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException {
        KeySpec spec = new PBEKeySpec(CharUtil.chars(secret));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHMACSHA512AndAES_128");
        keyStore.setEntry(name, new SecretKeyEntry(keyFactory.generateSecret(spec)), new PasswordProtection(new char[] {' '}));
    }

    @Override
    public void store(String name, byte[] secret, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException,
            KeyStoreException {
        //        KeySpec spec = secret.getKeySpec();
        //        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHMACSHA512AndAES_128");
        //        keyStore.setEntry(name, new SecretKeyEntry(keyFactory.generateSecret(spec)), new PasswordProtection(password));
    }

    @Override
    public byte[] retrieve(String name) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        Key entry = keyStore.getKey(name, new char[] {' '});
        return entry.getEncoded();
    }

    @Override
    public byte[] retrieve(String name, char[] password) {
        return null;
    }

    @Override
    public void delete(String name) {
        // TODO: Auto generated code.
    }

    @Override
    public void delete(String name, char[] password) {
        // TODO: Auto generated code.
    }
}
