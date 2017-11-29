package pw.itr0.kaba;

import pw.itr0.kaba.vault.keystore.KeyStoreVault;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteExample {
    public static void main(String[] args) {
        final Path file = Paths.get("target/test-keystore.jks");
        final char[] password = "password dayo".toCharArray();
        final KeyStoreVault keyStoreVault = new KeyStoreVault(file, password);
        keyStoreVault.store("key-id", "char".getBytes(), "key-id-password".toCharArray());
        keyStoreVault.store("key-id", "char2".getBytes(), "key-id2-password".toCharArray());
    }
}
