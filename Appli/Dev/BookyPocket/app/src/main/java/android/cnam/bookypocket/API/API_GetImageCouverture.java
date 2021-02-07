package android.cnam.bookypocket.API;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe comprenant les méthodes de récupération d'image en ligne
 */
public class API_GetImageCouverture {

    /**
     * Prend en paramètre le chemin URL d'une image et renvoi un table de byte de l'image
     * @param _URL
     * @return
     * @throws MalformedURLException
     */
    public static byte[] GetByteArrayImageFromURL(String _URL) throws MalformedURLException {
        URL url = new URL(_URL);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = url.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }

}
