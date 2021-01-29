package android.cnam.bookypocket.API;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Category;
import android.cnam.bookypocket.utils.StringUtil;
import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

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

public class API_GoogleBooks {

    private static List<Book> JSON_Decryptor(JSONObject json, Context context) throws JSONException {
        List<Book> books = new ArrayList<>();

        //on récupère la liste de livre
        JSONArray items = json.getJSONArray("items");
        int nbitems = json.getInt("totalItems");

        for(int i = 0; i < 30; i++){
            //pour chaque livre on récupère les informations qui nous intéresse
            JSONObject item = items.getJSONObject(i);
            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

            // On instancie le nouveau livre avec les infos du JSON
            Book book = new Book();

            String publishedDate = volumeInfo.getString("publishedDate");
            if(StringUtil.IsNullOrEmpty(publishedDate)){
                int yearpublished = Integer.parseInt(publishedDate.substring(0,4));
                book.setYearPublication(yearpublished);
            }

            String title = volumeInfo.getString("title");
            book.setTitle(title);

            String description = volumeInfo.getString("description");
            if(StringUtil.IsNullOrEmpty(description))
                book.setBackCover(description);

            int pageCount = volumeInfo.getInt("pageCount");
            book.setNbPages(pageCount);


            JSONArray categories = volumeInfo.getJSONArray("categories");
            try{
                Category categoryRequested = new Category((String)categories.get(0), false);
                ORMSQLiteManager DB_Manager = new ORMSQLiteManager(context);
                Category categoryInDB = DB_Manager.getCategoryByName(categoryRequested.getName());

                if(categoryInDB != null){
                    book.setCategory(categoryInDB);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            JSONArray identifiants = volumeInfo.getJSONArray("industryIdentifiers");
            try {
                String isbn13 = ((JSONObject) identifiants.get(0)).getString("identifier");
                if(StringUtil.IsNullOrEmpty(isbn13) && isbn13.length() == 13)
                    book.setISBN(isbn13);
            } catch (Exception e) {
                e.printStackTrace();
            }



            books.add(book);
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

    public static List<Book> Request(String keyword, Context context) throws Exception {
        if(keyword == null || keyword.trim().equals(""))
            throw new Exception("Le champ de recherche est vide");

        keyword = keyword.replace(' ', '+');
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=" + keyword +  " &maxResults=30");
        return JSON_Decryptor(json, context);
    }


}
