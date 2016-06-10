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
            //            throw e;  // TODO: Auto generated code.
        } catch (NoSuchAlgorithmException e) {
            //            throw e;  // TODO: Auto generated code.
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
            throw new IllegalStateException("KeyStore is not initialized.", e);
        }
    }

    @Override
    public byte[] retrieve(String name) {
        return retrieve(name, new char[] {' '});
    }

    @Override
    public byte[] retrieve(String name, char[] password) {
        try {
            Key entry = keyStore.getKey(name, new char[] {' '});
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

    public interface Entry extends VaultStorage.Entry<KeyStore.Entry> {
    }
}
