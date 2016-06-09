package pw.itr0.kaba.keystore;

public interface HashedPassword extends PasswordKey {

    boolean verify(byte[] password);
}
