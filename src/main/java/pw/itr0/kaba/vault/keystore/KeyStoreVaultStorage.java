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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

import pw.itr0.kaba.exception.ImplementationError;
import pw.itr0.kaba.vault.VaultStorage;

public class KeyStoreVaultStorage implements VaultStorage<KeyStore.Entry> {

    private final KeyStore keyStore;

    public KeyStoreVaultStorage(Path path, char[] password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance("JCEKS");
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

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.save(path, password);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save KeyStore file to " + path + ".", e);
            }
        }));
    }

    @Override
    public void save(Path path, char[] password) throws IOException {
        try (OutputStream file = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            keyStore.store(file, password);
        } catch (CertificateException e) {
            throw new ImplementationError("This vault cannot store Certificates.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new ImplementationError("Encryption/decryption algorithm must be statically implemented. This exception must not occur.", e);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    @Override
    public List<String> list() {
        try {
            return Collections.list(keyStore.aliases());
        } catch (KeyStoreException e) {
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    @Override
    public boolean contains(String name) {
        try {
            return keyStore.containsAlias(name);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    @Override
    public void store(String name, VaultStorage.Entry<? extends KeyStore.Entry> entry) {
        store(name, entry, new char[] {' '});
    }

    @Override
    public void store(String name, VaultStorage.Entry<? extends KeyStore.Entry> entry, char[] password) {
        try {
            keyStore.setEntry(name, entry.item(), new PasswordProtection(password));
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Failed to store key. alias=[" + name + "].", e);
        }
    }

    @Override
    public byte[] retrieve(String name) {
        return retrieve(name, new char[] {' '});
    }

    @Override
    public byte[] retrieve(String name, char[] password) {
        try {
            Key entry = keyStore.getKey(name, password);
            if (entry == null) {
                return null;
            }
            return entry.getEncoded();
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException e) {
            throw new RuntimeException(e);  // TODO: Auto generated code.
        } catch (KeyStoreException e) {
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    @Override
    public void delete(String name) {
        try {
            keyStore.deleteEntry(name);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    public interface Entry extends VaultStorage.Entry<KeyStore.Entry> {
    }
}
