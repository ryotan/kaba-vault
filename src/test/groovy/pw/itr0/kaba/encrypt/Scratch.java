package pw.itr0.kaba.encrypt;

import java.security.MessageDigest;
import java.security.Security;

/**
 * Created by ryotan on 16/05/17.
 */
public class Scratch {
    public void test() throws Exception {
        Security.getAlgorithms("Cipher").stream().filter(algorithm -> algorithm.startsWith("PBE")).sorted().forEachOrdered(algorithm -> {
            System.out.println("algorithm = " + algorithm);
        });

        byte[][] bytes = new byte[][] {
                "byte1".getBytes(),
                "byte2".getBytes(),
                "byte3".getBytes(),
        };

        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (byte[] aByte : bytes) {
            md5.update(aByte);
        }
        System.out.println("md5.digest() = " + md5.digest());

    }
}
