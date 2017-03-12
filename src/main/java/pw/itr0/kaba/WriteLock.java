package pw.itr0.kaba;

import java.nio.file.Path;
import java.nio.file.Paths;

import pw.itr0.kaba.vault.keystore.KeyStoreVault;

/**
 * Created by ryotan on 16/06/14.
 */
public class WriteLock {
    public static void main(String[] args) {
        final Path file = Paths.get("target/test-keystore.jks");
        final char[] password = "password dayo".toCharArray();
        final KeyStoreVault keyStoreVault = new KeyStoreVault(file, password);
        keyStoreVault.persist(file, password);
    }
}
