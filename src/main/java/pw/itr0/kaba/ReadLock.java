package pw.itr0.kaba;

import java.nio.file.Paths;

import pw.itr0.kaba.vault.keystore.KeyStoreVault;

public class ReadLock {
    public static void main(String[] args) {
        final KeyStoreVault keyStoreVault = new KeyStoreVault(Paths.get("target/test-keystore.jks"), "password dayo".toCharArray());
    }
}
