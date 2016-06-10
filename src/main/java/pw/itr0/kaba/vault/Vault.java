package pw.itr0.kaba.vault;

import java.nio.charset.StandardCharsets;
import java.util.List;

public interface Vault {

    List<String> list();

    boolean contains(String name);

    void store(String name, byte[] secret);

    void store(String name, byte[] secret, char[] password);

    default void store(String name, String secret) {
        this.store(name, secret.getBytes(StandardCharsets.UTF_8));
    }

    default void store(String name, String secret, char[] password) {
        this.store(name, secret.getBytes(StandardCharsets.UTF_8), password);
    }

    default String retrieve(String name) {
        byte[] bytes = this.retrieveEncoded(name);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    default String retrieve(String name, char[] password) {
        byte[] bytes = this.retrieveEncoded(name, password);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    byte[] retrieveEncoded(String name);

    byte[] retrieveEncoded(String name, char[] password);

    void delete(String name);

    void delete(String name, char[] password);
}
