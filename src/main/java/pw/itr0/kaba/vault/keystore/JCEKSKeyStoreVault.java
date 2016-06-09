package pw.itr0.kaba.vault.keystore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import pw.itr0.kaba.vault.Vault;

public class JCEKSKeyStoreVault implements Vault<KeyStoreEntry> {

    private final KeyStore keyStore;

    public JCEKSKeyStoreVault(Path path, char[] password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance("JCEKS");
        if (Files.exists(path)) {
            try (InputStream file = Files.newInputStream(path)) {
                keyStore.load(file, password);
            }
        } else {
            keyStore.load(null, password);
            keyStore.store(Files.newOutputStream(path, StandardOpenOption.CREATE), password);
        }
    }

    public JCEKSKeyStoreVault(LoadStoreParameter param) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(param);
    }

    @Override
    public List<String> list() throws KeyStoreException {
        return Collections.list(keyStore.aliases());
    }

    @Override
    public void store(String name, KeyStoreEntry secret) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException {
        PBEKeySpec spec = secret.getKeySpec();
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHMACSHA512AndAES_128");
        keyStore.setEntry(name, new SecretKeyEntry(keyFactory.generateSecret(spec)), null);
    }

    @Override
    public void store(String name, KeyStoreEntry secret, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException,
            KeyStoreException {
        PBEKeySpec spec = secret.getKeySpec();
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHMACSHA512AndAES_128");
        keyStore.setEntry(name, new SecretKeyEntry(keyFactory.generateSecret(spec)), new PasswordProtection(password));
    }

    @Override
    public byte[] retrieve(String name) {
        return null;
    }

    @Override
    public byte[] retrieve(String name, char[] password) {
        return null;
    }

    @Override
    public void delete(String name) {
    }

    @Override
    public void delete(String name, char[] password) {
    }
}
