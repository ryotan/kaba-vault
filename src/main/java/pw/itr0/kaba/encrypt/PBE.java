package pw.itr0.kaba.encrypt;

import pw.itr0.kaba.exception.MissImplementationException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Utility class for PBE encryption/decryption.
 *
 * @author ryotan
 * @since 1.0.0
 */
public final class PBE {

    /**
     * PBE encryption algorithm name.
     */
    private static final String ENCRYPTION_ALGORITHM = "PBEWithHMACSHA512AndAES_128";

    /**
     * {@link MessageDigest} algorithm to generate IV.
     * <p>
     * Because {@value #ENCRYPTION_ALGORITHM} requires 16byte length IV, this algorithm should generate 16byte(128bit) length byte array.
     */
    private static final String DIGEST_ALGORITHM = "MD5";

    /**
     * Default salt bytes of PBE encryption.
     */
    private static final byte[] DEFAULT_SALT = new byte[]{
            (byte) 0xd8, (byte) 0x3d, (byte) 0xde, (byte) 0x0b, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8,
            (byte) 0xd8, (byte) 0x3d, (byte) 0xde, (byte) 0x0b, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8, (byte) 0xd8
    };

    /**
     * Default iteration count of PBE encryption.
     */
    private static final int DEFAULT_ITERATION_COUNT = 50;

    /**
     * Hidden constructor for utility class.
     */
    private PBE() {
        // nop
    }

    /**
     * Returns default PBE encrypter.
     *
     * @return default PBE encrypter
     */
    public static Encrypter getEncrypter() {
        return getEncrypter(DEFAULT_SALT, DEFAULT_ITERATION_COUNT);
    }

    /**
     * Returns PBE encrypter whose salt is {@code salt} and iterationCount is {@code iterationCount}.
     *
     * @param salt           salt of PBE encryption
     * @param iterationCount iteration count of PBE encryption
     * @return PBE encrypter
     */
    public static Encrypter getEncrypter(byte[] salt, int iterationCount) {
        byte[] copied = new byte[salt.length];
        System.arraycopy(salt, 0, copied, 0, salt.length);
        return new Encrypter(copied, iterationCount);
    }

    /**
     * Returns default PBE decrypter.
     *
     * @return default PBE decrypter
     */
    public static Decrypter getDecrypter() {
        return getDecrypter(DEFAULT_SALT, DEFAULT_ITERATION_COUNT);
    }

    /**
     * Returns PBE decrypter whose salt is {@code salt} and iterationCount is {@code iterationCount}.
     *
     * @param salt           salt of PBE decryption
     * @param iterationCount iteration count of PBE decryption
     * @return PBE decrypter
     */
    public static Decrypter getDecrypter(byte[] salt, int iterationCount) {
        byte[] copied = new byte[salt.length];
        System.arraycopy(salt, 0, copied, 0, salt.length);
        return new Decrypter(copied, iterationCount);
    }

    /**
     * Returns {@link Cipher} for PBE encryption/decryption.
     *
     * @param mode           encryption({@link Cipher#ENCRYPT_MODE})/decryption({@link Cipher#DECRYPT_MODE})
     * @param password       password of PBE
     * @param salt           salt of PBE
     * @param iterationCount iteration count of PBE
     * @return {@link Cipher} for PBE encryption/decryption
     * @throws NoSuchAlgorithmException if the running JRE does not support "{@value ENCRYPTION_ALGORITHM}"
     * @throws InvalidKeyException      if the given key size exceeds the maximum allowable key size
     */
    private static Cipher getCipher(int mode, char[] password, byte[] salt, int iterationCount) throws NoSuchAlgorithmException, InvalidKeyException {

        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            PBEKeySpec keySpec = new PBEKeySpec(password);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount, new IvParameterSpec(generateIV(password, salt)));
            cipher.init(mode, keyFactory.generateSecret(keySpec), paramSpec);
            return cipher;
        } catch (NoSuchPaddingException e) {
            throw new MissImplementationException("Padding algorithm must be statically implemented. This exception must not occur.", e);
        } catch (InvalidKeySpecException e) {
            throw new MissImplementationException("Key specification must be type safely implemented. This exception must not occur.", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new MissImplementationException("Algorithm parameter must be type safely implemented. This exception must not occur.", e);
        }
    }

    /**
     * Generate IV according to Open SSL like algorithm.
     *
     * @param chars chars used to generate IV
     * @param bytes bytes used to generate IV
     * @return generated IV
     */
    private static byte[] generateIV(char[] chars, byte[] bytes) {
        ByteBuffer buffer = Charset.forName("US-ASCII").encode(CharBuffer.wrap(chars));
        byte[] copiedChars = new byte[chars.length];
        buffer.get(copiedChars, 0, chars.length);
        byte[] copiedBytes = Arrays.copyOf(bytes, bytes.length);

        try {
            final MessageDigest md5 = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md5.update(copiedChars);
            md5.update(copiedBytes);
            md5.update(md5.digest());
            md5.update(copiedChars);
            md5.update(copiedBytes);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(DIGEST_ALGORITHM + " is not available on current JDK.", e);
        }
    }

    /**
     * Encrypter for PBE.
     *
     * @author ryotan
     * @since 1.0.0
     */
    public static final class Encrypter implements PasswordBasedEncrypter {

        /**
         * Salt bytes of PBE encryption.
         */
        private final byte[] salt;

        /**
         * iteration count of PBE encryption.
         */
        private final int iterationCount;

        /**
         * Create PBE encrypter using given {@code salt} and {@code iterationCount}.
         *
         * @param salt           salt bytes of PBE encryption
         * @param iterationCount iteration count of PBE encryption
         */
        private Encrypter(byte[] salt, int iterationCount) {
            this.salt = new byte[salt.length];
            System.arraycopy(salt, 0, this.salt, 0, salt.length);
            this.iterationCount = iterationCount;
        }

        /**
         * Encrypt secret bytes using password for PBE.
         *
         * @param password encrypting password
         * @param secret   secret bytes
         * @return PBE encrypted secret
         * @throws GeneralSecurityException on encryption error
         */
        @Override
        public byte[] encrypt(char[] password, byte[] secret) throws GeneralSecurityException {
            return getCipher(Cipher.ENCRYPT_MODE, password, this.salt, this.iterationCount).doFinal(secret);
        }
    }

    /**
     * Decrypter for PBE.
     *
     * @author ryotan
     * @since 1.0.0
     */
    public static final class Decrypter implements PasswordBasedDecrypter {

        /**
         * Salt bytes of PBE decryption.
         */
        private final byte[] salt;

        /**
         * iteration count of PBE encryption.
         */
        private final int iterationCount;


        /**
         * Create PBE decrypter using given {@code salt} and {@code iterationCount}.
         *
         * @param salt           salt bytes of PBE decryption
         * @param iterationCount iteration count of PBE decryption
         */
        private Decrypter(byte[] salt, int iterationCount) {
            this.salt = new byte[salt.length];
            System.arraycopy(salt, 0, this.salt, 0, salt.length);
            this.iterationCount = iterationCount;
        }

        /**
         * Decrypt secret bytes using password for PBE.
         *
         * @param password decrypting password
         * @param secret   secret bytes
         * @return PBE decrypted secret
         * @throws GeneralSecurityException on decryption error
         */
        @Override
        public byte[] decrypt(char[] password, byte[] secret) throws GeneralSecurityException {
            return getCipher(Cipher.DECRYPT_MODE, password, this.salt, this.iterationCount).doFinal(secret);
        }
    }
}
