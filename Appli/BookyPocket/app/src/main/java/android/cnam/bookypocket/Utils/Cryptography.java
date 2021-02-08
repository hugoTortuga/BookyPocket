package android.cnam.bookypocket.Utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe comportant les méthodes de chiffrement
 */
public class Cryptography {

    /**
     * méthode de hash sha256
     * @param original
     * @return
     */
    public static String Hash(String original){

        //return DigestUtils.sha256Hex(original);
        return new String(Hex.encodeHex(DigestUtils.sha256(original)));
    }

}
