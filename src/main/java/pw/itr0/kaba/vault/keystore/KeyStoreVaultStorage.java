package pw.itr0.kaba.vault.keystore;

import pw.itr0.kaba.exception.ImplementationError;
import pw.itr0.kaba.vault.VaultStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Stream;

public class KeyStoreVaultStorage implements VaultStorage<KeyStore.Entry> {

    private static final ConcurrentHashMap<String, Thread> SHUTDOWN_HOOKS = new ConcurrentHashMap<>();
    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();

    private final KeyStore keyStore;
    private final String KEYSTORE_TYPE= "JCEKS";

    public KeyStoreVaultStorage(Path path, char[] password) throws IOException {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Could not find KeyStore Provider of " + KEYSTORE_TYPE + '.', e);
        }
        if (Files.exists(path)) {
            try (InputStream file = Files.newInputStream(path)) {
                keyStore.load(file, password);
            } catch (CertificateException e) {
                throw new ImplementationError("This vault cannot store Certificates.", e);
            } catch (NoSuchAlgorithmException e) {
                throw new ImplementationError("Algorithm must be statically implemented. This exception must not occur.", e);
            }
        } else {
            try (OutputStream file = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
                keyStore.load(null, password);
                keyStore.store(file, password);
            } catch (CertificateException e) {
                throw new ImplementationError("This vault cannot store Certificates.", e);
            } catch (NoSuchAlgorithmException e) {
                throw new ImplementationError("Algorithm must be statically implemented. This exception must not occur.", e);
            } catch (KeyStoreException e) {
                throw new IllegalStateException("KeyStore is not initialized.", e);
            }
        }

        Thread hook = THREAD_FACTORY.newThread(() -> {
            try {
                this.save(path, password);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to save KeyStore file to " + path + '.', e);
            }
        });
        SHUTDOWN_HOOKS.compute(path.toRealPath().toFile().getCanonicalPath(), (p, orig) -> {
            Runtime.getRuntime().addShutdownHook(hook);
            return hook;
        });
    }

    @Override
    public void save(Path path, char[] password) throws IOException {

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
             FileLock lock = channel.lock(0, Long.MAX_VALUE, false);
             OutputStream file = Channels.newOutputStream(channel)) {
            if (lock != null) {
                try {
                    keyStore.store(file, password);
                } catch (CertificateException e) {
                    throw new ImplementationError("This vault cannot store Certificates.", e);
                } catch (NoSuchAlgorithmException e) {
                    throw new ImplementationError("Algorithm must be statically implemented. This exception must not occur.", e);
                } catch (KeyStoreException e) {
                    throw new IllegalStateException("KeyStore is not initialized.", e);
                }
            }
        } catch (ClosedChannelException ignore) {
            // Ignore ClosedChannelException occurred at java.nio.channels.FileLock#release(),
            // because KeyStore#store(OutputStream, char[]) closes OutputStream...
            if (Stream.of(ignore.getStackTrace()).noneMatch(elm ->
                    "java.nio.channels.FileLock".equals(elm.getClassName()) && "close".equals(elm.getMethodName()))) {
                throw ignore;
            }
        } catch (OverlappingFileLockException e) {
            throw new IllegalStateException("Failed to get lock on KeyStore file, and skipped saving file." +
                    "Multiple KeyStore instances of path=[" + path + "] might be created in current JVM.", e);
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
        store(name, entry, new char[]{' '});
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
        return retrieve(name, new char[]{' '});
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
