package pw.itr0.kaba.vault.keystore;

import java.security.spec.KeySpec;

public interface KeyStoreVaultEntry<T> {

    KeySpec getKeySpec();

    T getEntry();
}
