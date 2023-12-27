package programming3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Zhang Runyan
 */

public class MD5Hash {
    public static String doHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(input.getBytes(StandardCharsets.UTF_8));

        byte[] hashBytes = md.digest();

        StringBuilder sb = new StringBuilder();
        
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * re hash , Decrypt base64
     *
     * @param bs64Str
     * @return
     */
    public static String reHash(String bs64Str) {
        byte[] base64decodedBytes = Base64.getDecoder().decode(bs64Str);
        return new String(base64decodedBytes, StandardCharsets.UTF_8);
    }
}

