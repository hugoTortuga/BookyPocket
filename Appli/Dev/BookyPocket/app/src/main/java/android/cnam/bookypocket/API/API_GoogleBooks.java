package android.cnam.bookypocket.API;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Category;
import android.cnam.bookypocket.Model.Photo;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Context;

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

        int realLength = items.length();
        for (int i = 0; i < 20 && i < items.length() ; i++) {
            //pour chaque livre on récupère les informations qui nous intéresse

            JSONObject item = items.getJSONObject(i);

            // On instancie le nouveau livre avec les infos du JSON
            Book book = JSON_Book_Formator(item, context);
            if(!StringUtil.IsNullOrEmpty(book.getTitle()))
                books.add(book);
        }
        return books;
    }

    private static Book JSON_Book_Formator(JSONObject item, Context context) throws JSONException {
        Book book = new Book();
        JSONObject volumeInfo = item.getJSONObject("volumeInfo");
        String title = volumeInfo.getString("title");
        book.setTitle(title);
        //published date
        try {
            String publishedDate = volumeInfo.getString("publishedDate");
            if (!StringUtil.IsNullOrEmpty(publishedDate)) {
                int yearpublished = Integer.parseInt(publishedDate.substring(0, 4));
                book.setYearPublication(yearpublished);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // description
        try {
            String description = volumeInfo.getString("description");
            if (!StringUtil.IsNullOrEmpty(description))
                book.setBackCover(description);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //nb page
        try {
            int pageCount = volumeInfo.getInt("pageCount");
            book.setNbPages(pageCount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Category
        try {
            JSONArray categories = volumeInfo.getJSONArray("categories");
            Category categoryRequested = new Category((String) categories.get(0), false);

            Category categoryInDB = DataBaseSingleton.GetDataBaseSingleton(context).getCategoryByName(categoryRequested.getName());

            if (categoryInDB != null) {
                book.setCategory(categoryInDB);
            }
            else{
                book.setCategory(categoryRequested);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //ISBN 13
        try {
            JSONArray identifiants = volumeInfo.getJSONArray("industryIdentifiers");
            String isbn13 = ((JSONObject) identifiants.get(0)).getString("identifier");
            if (!StringUtil.IsNullOrEmpty(isbn13))
                book.setISBN(isbn13);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SMALL PICTURE
        try {
            JSONObject links = volumeInfo.getJSONObject("imageLinks");
            String smallImageURL = links.getString("smallThumbnail");

            Photo photo = new Photo(API_GetImageCouverture.GetByteArrayImageFromURL(smallImageURL));
            book.setPhoto(photo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //previewlink
        try {
            String previewLink = volumeInfo.getString("previewLink");
            if (!StringUtil.IsNullOrEmpty(previewLink))
                book.setPreviewLink(previewLink);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //author
        try{
            JSONArray authors = volumeInfo.getJSONArray("authors");
            if(authors != null) {
                if(authors.length()>0){
                    String authorA = authors.get(0).toString();
                    Author a = DataBaseSingleton.GetDataBaseSingleton(context).getAuthorFromName(authorA);
                    if(a == null) {
                        Author newAuthor = new Author();
                        newAuthor.setArtistName(authorA);
                        book.setAuthor(newAuthor);
                    }
                    else
                        book.setAuthor(a);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
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
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=" + keyword + " &maxResults=20");
        return JSON_Decryptor(json, context);
    }

    public static List<Book> RequestISBN(String ISBN, Context context) throws Exception {
        if (StringUtil.IsNullOrEmpty(ISBN))
            throw new Exception("Le champ de recherche est vide");
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=+ISBN=" + ISBN + " &maxResults=1");
        return JSON_Decryptor(json, context);
    }

    public static List<Book> RequestAuthor(String artistName, Context context) throws Exception {
        if (StringUtil.IsNullOrEmpty(artistName))
            throw new Exception("Le champ de recherche est vide");
        artistName = artistName.replace(' ', '+');
        JSONObject json = API_GoogleBooks.ReadJsonFromUrl("https://www.googleapis.com/books/v1/volumes?q=+inauthors=" + artistName + " &maxResults=1");
        return JSON_Decryptor(json, context);
    }

}
