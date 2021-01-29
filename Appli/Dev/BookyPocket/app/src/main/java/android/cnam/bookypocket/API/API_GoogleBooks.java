package android.cnam.bookypocket.API;

import android.cnam.bookypocket.Model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

public class API_GoogleBooks {

    private static List<Book> JSON_Decryptor(JSONObject json) throws JSONException {
        List<Book> books = new ArrayList<>();
        JSONArray items = json.getJSONArray("items");
        int nbitems = json.getInt("totalItems");
        for(int i = 0; i < 30; i++){
            JSONObject item = items.getJSONObject(i);
            JSONObject volumeInfo = item.getJSONObject("volumeInfo");
            String title = volumeInfo.getString("title");

            Book b1 = new Book();
            b1.setTitle(title + " " + i);
            books.add(b1);
        }



        return books;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String ReadStringJSON(String url) throws IOException{
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } finally {
            is.close();
        }
    }

    private static JSONObject ReadJsonFromUrl(String url) throws IOException, JSONException {
        return new JSONObject(ReadStringJSON(url));
    }

    public static List<Book> Request(String keyword) throws Exception {
        if(keyword == null || keyword.trim().equals(""))
            throw new Exception("Le champ de recherche est vide");

        keyword = keyword.replace(' ', '+');
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=" + keyword +  " &maxResults=30");
        return JSON_Decryptor(json);
    }


}
