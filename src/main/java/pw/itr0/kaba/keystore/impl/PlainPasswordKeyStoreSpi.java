package pw.itr0.kaba.keystore.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;

public class PlainPasswordKeyStoreSpi extends KeyStoreSpi {

    private final KeyStore delegate;

    PlainPasswordKeyStoreSpi(KeyStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        try {
            return delegate.getKey(alias, password);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public Certificate[] engineGetCertificateChain(String alias) {
        try {
            return delegate.getCertificateChain(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public Certificate engineGetCertificate(String alias) {
        try {
            return delegate.getCertificate(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public Date engineGetCreationDate(String alias) {
        try {
            return delegate.getCreationDate(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) throws KeyStoreException {
        delegate.setKeyEntry(alias, key, password, chain);
    }

    @Override
    public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) throws KeyStoreException {
        delegate.setKeyEntry(alias, key, chain);
    }

    @Override
    public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
        delegate.setCertificateEntry(alias, cert);
    }

    @Override
    public void engineDeleteEntry(String alias) throws KeyStoreException {
        delegate.deleteEntry(alias);
    }

    @Override
    public Enumeration<String> engineAliases() {
        try {
            return delegate.aliases();
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public boolean engineContainsAlias(String alias) {
        try {
            return delegate.containsAlias(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public int engineSize() {
        try {
            return delegate.size();
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public boolean engineIsKeyEntry(String alias) {
        try {
            return delegate.isKeyEntry(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public boolean engineIsCertificateEntry(String alias) {
        try {
            return delegate.isCertificateEntry(alias);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public String engineGetCertificateAlias(Certificate cert) {
        try {
            return delegate.getCertificateAlias(cert);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        try {
            delegate.store(stream, password);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public void engineStore(LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        try {
            delegate.store(param);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }

    @Override
    public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        delegate.load(stream, password);
    }

    @Override
    public void engineLoad(LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        delegate.load(param);
    }

    @Override
    public Entry engineGetEntry(String alias, ProtectionParameter protParam)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        return delegate.getEntry(alias, protParam);
    }

    @Override
    public void engineSetEntry(String alias, Entry entry, ProtectionParameter protParam)
            throws KeyStoreException {
        delegate.setEntry(alias, entry, protParam);
    }

    @Override
    public boolean engineEntryInstanceOf(String alias, Class<? extends Entry> entryClass) {
        try {
            return delegate.entryInstanceOf(alias, entryClass);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Delegating KeyStore is not loaded.", e);
        }
    }
}
