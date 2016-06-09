package pw.itr0.kaba.keystore;

public final class PasswordKeyFactory {

    private PasswordKeyFactory() {
    }

    public static PasswordKeyFactory getInstance() {
        return new PasswordKeyFactory();
    }

    public PasswordKey generate(PasswordKeySpec keySpec) {
        return null;
    }
}
