package pw.itr0.kaba.keystore;

public interface DecryptablePassword extends PasswordKey {

    byte[] decrypt();
}
