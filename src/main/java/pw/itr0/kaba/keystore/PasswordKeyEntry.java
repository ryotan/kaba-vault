package pw.itr0.kaba.keystore;

import java.security.KeyStore;

public class PasswordKeyEntry implements KeyStore.Entry {

    private final PasswordKey key;

    public PasswordKeyEntry(PasswordKey key) {
        this.key = key;
    }

    public PasswordKey getKey() {
        return key;
    }
}
