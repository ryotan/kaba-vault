package pw.itr0.kaba.vault;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * @author Ryo Tanaka
 */
public interface Vault<T> {

    List<String> list() throws KeyStoreException;

    void store(String name, T secret) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    void store(String name, T secret, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException;

    T retrieve(String name);

    T retrieve(String name, char[] password);

}
