package pw.itr0.kaba.vault;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import pw.itr0.kaba.exception.IncorrectPasswordException;
import pw.itr0.kaba.exception.InvalidPasswordFormatException;

public interface VaultStorage<E> {

    void save(Path path, char[] password) throws IOException;

    List<String> list();

    boolean contains(String name);

    void store(String name, Entry<? extends E> entry);

    void store(String name, Entry<? extends E> entry, char[] password) throws InvalidPasswordFormatException;

    byte[] retrieve(String name) throws IncorrectPasswordException;

    byte[] retrieve(String name, char[] password) throws IncorrectPasswordException;

    void delete(String name) throws IncorrectPasswordException;

    interface Entry<E> {
        E item();

        byte[] content();
    }
}
