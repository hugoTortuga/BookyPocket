package android.cnam.bookypocket.Utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Cryptography {

    public static String Hash(String original){
        return DigestUtils.sha256Hex(original);
    }

}
