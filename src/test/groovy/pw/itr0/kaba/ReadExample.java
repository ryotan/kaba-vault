package pw.itr0.kaba;

import pw.itr0.kaba.exception.IncorrectPasswordException;
import pw.itr0.kaba.vault.keystore.KeyStoreVault;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadExample {
    public static void main(String[] args) throws IncorrectPasswordException {
        final Path file = Paths.get("target/test-keystore.jks");
        final char[] password = "password dayo".toCharArray();
        final KeyStoreVault keyStoreVault = new KeyStoreVault(file, password);
        System.out.println(keyStoreVault.retrieve("key-id", "key-id2-password".toCharArray()));
    }
}
