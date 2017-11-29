package pw.itr0.kaba.vault.keystore;

import pw.itr0.kaba.vault.Vault;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

public class KeyStoreVault implements Vault {

    private final KeyStoreVaultStorage storage;

    public KeyStoreVault(Path file, char[] password) {
        try {
            this.storage = new KeyStoreVaultStorage(file, password);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load KeyStore file. path=[" + file + "].", e);
        }
    }

    @Override
    public List<String> list() {
        return this.storage.list();
    }

    @Override
    public boolean contains(String name) {
        return this.storage.contains(name);
    }

    @Override
    public void store(String name, byte[] secret) {
        this.storage.store(name, new PBEKeyEntry(secret));
    }

    @Override
    public void store(String name, byte[] secret, char[] password) {
        this.storage.store(name, new PBEKeyEntry(secret), password);
    }

    @Override
    public byte[] retrieveEncoded(String name) {
        return this.storage.retrieve(name);
    }

    @Override
    public byte[] retrieveEncoded(String name, char[] password) {
        return this.storage.retrieve(name, password);
    }

    @Override
    public void delete(String name) {
        this.storage.delete(name);
    }
}
