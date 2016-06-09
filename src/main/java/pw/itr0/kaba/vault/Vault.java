package pw.itr0.kaba.vault;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface Vault<T> {

    List<String> list() throws KeyStoreException;

    void store(String name, T secret) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    void store(String name, T secret, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    byte[] retrieve(String name) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException;

    byte[] retrieve(String name, char[] password);

    void delete(String name);

    void delete(String name, char[] password);
}
