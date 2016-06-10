package pw.itr0.kaba.vault;

import java.nio.charset.StandardCharsets;
import java.util.List;

import pw.itr0.kaba.exception.IncorrectPasswordException;
import pw.itr0.kaba.exception.InvalidPasswordFormatException;

public interface Vault {

    List<String> list();

    boolean contains(String name);

    void store(String name, byte[] secret);

    void store(String name, byte[] secret, char[] password) throws InvalidPasswordFormatException;

    default void store(String name, String secret) {
        this.store(name, secret.getBytes(StandardCharsets.UTF_8));
    }

    default void store(String name, String secret, char[] password) throws InvalidPasswordFormatException {
        this.store(name, secret.getBytes(StandardCharsets.UTF_8), password);
    }

    default String retrieve(String name) throws IncorrectPasswordException {
        byte[] bytes = this.retrieveEncoded(name);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    default String retrieve(String name, char[] password) throws IncorrectPasswordException {
        byte[] bytes = this.retrieveEncoded(name, password);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    byte[] retrieveEncoded(String name) throws IncorrectPasswordException;

    byte[] retrieveEncoded(String name, char[] password) throws IncorrectPasswordException;

    void delete(String name);
}
