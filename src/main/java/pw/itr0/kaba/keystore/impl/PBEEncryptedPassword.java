package pw.itr0.kaba.keystore.impl;

import pw.itr0.kaba.keystore.DecryptablePassword;

public class PBEEncryptedPassword implements DecryptablePassword {

    @Override
    public String getAlgorithm() {
        return "kaba-decryptable-password";
    }

    @Override
    public String getFormat() {
        return null;  // TODO: Auto generated code.
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];  // TODO: Auto generated code.
    }

    @Override
    public byte[] decrypt() {
        return new byte[0];  // TODO: Auto generated code.
    }
}
