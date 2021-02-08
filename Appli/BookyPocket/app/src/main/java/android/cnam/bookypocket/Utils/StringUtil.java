package android.cnam.bookypocket.Utils;

/**
 * Classe util d'opération sur les string
 */
public class StringUtil {

    /**
     * Check si un string est null ou vide renvoi un boolean
     * @param s
     * @return
     */
    public static boolean IsNullOrEmpty(String s){
        return (s == null || s.equals("") || s.trim().equals(""));
    }

}
