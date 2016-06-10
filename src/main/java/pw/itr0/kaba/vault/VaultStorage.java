package pw.itr0.kaba.vault;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface VaultStorage<E> {

    void save(Path path, char[] password) throws IOException;

    List<String> list();

    boolean contains(String name);

    void store(String name, Entry<? extends E> entry);

    void store(String name, Entry<? extends E> entry, char[] password);

    byte[] retrieve(String name);

    byte[] retrieve(String name, char[] password);

    void delete(String name);

    interface Entry<E> {
        E item();

        byte[] content();
    }
}
