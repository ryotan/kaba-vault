package pw.itr0.kaba.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Utility class for handling character sequences or arrays.
 *
 * @author ryotan
 * @since 1.0.0
 */
public final class CharUtil {

    /**
     * Hidden constructor for utility class.
     */
    private CharUtil() {
        // nop
    }

    /**
     * Converts char array to byte array using {@link StandardCharsets#UTF_8 UTF-8}.
     *
     * @param chars converting char array
     * @return converted byte array
     */
    public static byte[] bytes(char[] chars) {
        return bytes(chars, StandardCharsets.UTF_8);
    }

    /**
     * Converts {@link CharSequence} to byte array using {@link StandardCharsets#UTF_8 UTF-8}.
     *
     * @param chars converting {@link CharSequence}
     * @return converted byte array
     */
    public static byte[] bytes(CharSequence chars) {
        return bytes(chars, StandardCharsets.UTF_8);
    }

    /**
     * Converts char array to byte array using {@code charset}.
     *
     * @param chars converting char array
     * @param charset character set used to encode character to bytes
     * @return converted byte array
     */
    public static byte[] bytes(char[] chars, Charset charset) {
        return array(charset.encode(CharBuffer.wrap(chars.clone())));
    }

    /**
     * Converts {@link CharSequence} to byte array using {@code charset}.
     *
     * @param chars converting {@link CharSequence}
     * @param charset character set used to encode characters to bytes
     * @return converted byte array
     */
    public static byte[] bytes(CharSequence chars, Charset charset) {
        return array(charset.encode(CharBuffer.wrap(chars)));
    }

    /**
     * Converts byte array to char array using {@link StandardCharsets#UTF_8 UTF-8}.
     *
     * @param bytes converting byte array
     * @return converted char array
     */
    public static char[] chars(byte[] bytes) {
        return chars(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Converts byte array to char array using {@code charset}.
     * @param charset sharacter set used to decode bytes to characters
     * @param bytes converting byte array
     *
     * @return converted char array
     */
    public static char[] chars(byte[] bytes, Charset charset) {
        return array(charset.decode(ByteBuffer.wrap(bytes.clone())));
    }

    /**
     * Extract backed array from {@link ByteBuffer}. Backed array is trimmed to {@link ByteBuffer#limit()}.
     *
     * @param buffer {@link ByteBuffer}
     * @return extracted array
     */
    private static byte[] array(ByteBuffer buffer) {
        return Arrays.copyOf(buffer.array(), buffer.limit());
    }

    /**
     * Extract backed array from {@link CharBuffer}. Backed array is trimmed to {@link CharBuffer#limit()}.
     *
     * @param buffer {@link CharBuffer}
     * @return extracted array
     */
    private static char[] array(CharBuffer buffer) {
        return Arrays.copyOf(buffer.array(), buffer.limit());
    }
}
