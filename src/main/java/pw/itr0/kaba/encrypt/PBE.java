package pw.itr0.kaba.encrypt;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PBE {

    private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";
    private static final byte[] SALT = new byte[]{
            (byte) 0xd8, (byte) 0x3d, (byte) 0xde, (byte) 0x0b, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8
    };

    public static Encrypter getEncrypter() {
        return new Encrypter(SALT, 50);
    }

    public static Decrypter getDecrypter() {
        return new Decrypter(SALT, 50);
    }

    private static Cipher getCipher(int mode, char[] password, byte[] salt, int iterationCount) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        PBEKeySpec keySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(keySpec);

        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(mode, key, paramSpec);
        return cipher;
    }

    public static class Encrypter implements ByteEncrypter {

        private final byte[] salt;
        private final int iterationCount;

        private Encrypter(byte[] salt, int iterationCount) {
            this.salt = salt;
            this.iterationCount = iterationCount;
        }

        @Override
        public byte[] encrypt(char[] password, byte[] secret) throws GeneralSecurityException {
            return getCipher(Cipher.ENCRYPT_MODE, password, salt, iterationCount).doFinal(secret);
        }
    }

    public static class Decrypter implements ByteDecrypter {
        private final byte[] salt;
        private final int iterationCount;

        private Decrypter(byte[] salt, int iterationCount) {
            this.salt = salt;
            this.iterationCount = iterationCount;
        }

        @Override
        public byte[] decrypt(char[] password, byte[] secret) throws GeneralSecurityException {
            return getCipher(Cipher.DECRYPT_MODE, password, salt, iterationCount).doFinal(secret);
        }
    }

    private PBE() {
    }
}
