package pw.itr0.kaba.keystore.impl;

import java.security.KeyStore;

public class PlainPasswordKeyStore extends KeyStore {

    /**
     * Creates a KeyStore object of the given type, and encapsulates the given
     * provider implementation (SPI object) in it.
     *
     * @param keyStore the delegating {@link KeyStore}
     */
    public PlainPasswordKeyStore(KeyStore keyStore) {
        super(new PlainPasswordKeyStoreSpi(keyStore), keyStore.getProvider(), keyStore.getType());
    }
}
