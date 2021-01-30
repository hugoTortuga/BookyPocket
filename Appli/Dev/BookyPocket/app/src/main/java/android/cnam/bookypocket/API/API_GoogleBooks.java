package android.cnam.bookypocket.API;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Category;
import android.cnam.bookypocket.Model.Photo;
import android.cnam.bookypocket.utils.StringUtil;
import android.content.Context;
import android.util.Log;

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

        for (int i = 0; i < 10; i++) {
            //pour chaque livre on récupère les informations qui nous intéresse
            JSONObject item = items.getJSONObject(i);

            // On instancie le nouveau livre avec les infos du JSON
            Book book = JSON_Book_Formator(item, context);
            books.add(book);
        }
        return books;
    }

    private static Book JSON_Book_Formator(JSONObject item, Context context) throws JSONException {
        Book book = new Book();
        JSONObject volumeInfo = item.getJSONObject("volumeInfo");
        Log.i("JSONME", "volumeInfo : " + volumeInfo);
        String title = volumeInfo.getString("title");
        book.setTitle(title);
        Log.i("JSONME", "title : " + title);

        try {
            String publishedDate = volumeInfo.getString("publishedDate");
            Log.i("JSONME", "published : " + publishedDate);
            if (!StringUtil.IsNullOrEmpty(publishedDate)) {
                int yearpublished = Integer.parseInt(publishedDate.substring(0, 4));
                book.setYearPublication(yearpublished);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String description = volumeInfo.getString("description");
            if (!StringUtil.IsNullOrEmpty(description))
                book.setBackCover(description);

            Log.i("JSONME", "description : " + description);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        try {
            int pageCount = volumeInfo.getInt("pageCount");
            Log.i("JSONME", "pageCount : " +pageCount);
            book.setNbPages(pageCount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ORMSQLiteManager DB_Manager = new ORMSQLiteManager(context);
        try {
            JSONArray categories = volumeInfo.getJSONArray("categories");
            Category categoryRequested = new Category((String) categories.get(0), false);

            Log.i("JSONME", "categories : " + categoryRequested);

            Category categoryInDB = DB_Manager.getCategoryByName(categoryRequested.getName());

            if (categoryInDB != null) {
                book.setCategory(categoryInDB);
            }
            else{
                book.setCategory(categoryRequested);
                DB_Manager.insertObjectInDB(categoryRequested, Category.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            DB_Manager.close();
        }

        //ISBN 13
        try {
            JSONArray identifiants = volumeInfo.getJSONArray("industryIdentifiers");
            String isbn13 = ((JSONObject) identifiants.get(0)).getString("identifier");
            Log.i("JSONME", "isbn : " +isbn13);
            if (!StringUtil.IsNullOrEmpty(isbn13) && isbn13.length() == 13)
                book.setISBN(isbn13);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SMALL PICTURE
        try {
            JSONObject links = volumeInfo.getJSONObject("imageLinks");
            String smallImageURL = links.getString("smallThumbnail");
            Log.i("JSONME", "photo : " + smallImageURL);

            Photo photo = new Photo(API_GetImageCouverture.GetByteArrayImageFromURL(smallImageURL));
            book.setPhoto(photo);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return book;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String ReadStringJSON(String url) throws IOException {
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
        if (keyword == null || keyword.trim().equals(""))
            throw new Exception("Le champ de recherche est vide");

        keyword = keyword.replace(' ', '+');
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=" + keyword + " &maxResults=10");
        return JSON_Decryptor(json, context);
    }


}
