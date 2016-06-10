package pw.itr0.kaba.vault.keystore;

import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import pw.itr0.kaba.vault.Vault;

public class KeyStoreVault implements Vault {

    private final KeyStoreVaultStorage storage;

    public KeyStoreVault(Path file, char[] password) {
        try {
            this.storage = new KeyStoreVaultStorage(file, password);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> list() {
        return storage.list();
    }

    @Override
    public boolean contains(String name) {
        return storage.contains(name);
    }

    @Override
    public void store(String name, byte[] secret) {
        storage.store(name, new PBEKeyEntry(secret));
    }

    @Override
    public void store(String name, byte[] secret, char[] password) {
        storage.store(name, new PBEKeyEntry(secret), password);
    }

    @Override
    public byte[] retrieveEncoded(String name) {
        return storage.retrieve(name);
    }

    @Override
    public byte[] retrieveEncoded(String name, char[] password) {
        return storage.retrieve(name, password);
    }

    @Override
    public void delete(String name) {
        storage.delete(name);
    }
}
