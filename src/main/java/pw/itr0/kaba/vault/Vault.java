package pw.itr0.kaba.vault;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface Vault {

    ConcurrentHashMap<String, Vault> cache = new ConcurrentHashMap<>();

    static Vault computeIfAbsent(String name, Function<String, Vault> factory) {
        return cache.computeIfAbsent(name, factory);
    }

    static Vault get(String name) {
        return cache.get(name);
    }

    List<String> list() throws KeyStoreException;

    void store(String name, byte[] secret) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    void store(String name, byte[] secret, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    byte[] retrieve(String name) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException;

    byte[] retrieve(String name, char[] password);

    void delete(String name);

    void delete(String name, char[] password);
}
