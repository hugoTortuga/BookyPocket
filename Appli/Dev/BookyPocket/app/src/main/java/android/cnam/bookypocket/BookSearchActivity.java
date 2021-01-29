package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.ui.ReadingsListAdapter;
import android.cnam.bookypocket.utils.Alert;
import android.cnam.bookypocket.utils.ChangeActivity;
import android.content.Context;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookSearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageButton itemImageButton;

    private ArrayList<String> titleList = new ArrayList<>(
            Arrays.asList("Liste des ouvrages"));

    private ListView found_list;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);

        searchView = (SearchView) findViewById(R.id.search_book_button);
        found_list = (ListView) findViewById(R.id.found_list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
        found_list.setAdapter(adapter);
    }

    public void searchBook(View view){
        try{
            if(Session.isUserConnectedToInternet()){
                if(searchView.getQuery() == null || searchView.getQuery().equals(""))
                    Alert.ShowDialog(this, "0 résultat","Le champ de recherche est vide");

                else{
                    String keyword = searchView.getQuery().toString();
                    MyAsyncTask task = new MyAsyncTask(this,keyword);
                    task.execute();
                }



            }
            else{
                Alert.ShowDialog(this, "Pas de connexion internet",
                        "La recherche de livre est impossible sans connexion internet");
            }

        }catch(Exception ex){
            Alert.ShowError(this, "Le recherche de livre a provoqué une erreur",
                    "" + ex);
        }
    }

    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private List<Book> books;
        private BookSearchActivity it;
        private String keyword;

        public MyAsyncTask(BookSearchActivity _it, String _keyword) {
            super();
            it = _it;
            keyword = _keyword;
        }

        @Override
        protected String doInBackground(Void... params) {
            Alert.ShowError(it, "Début recherche", "" + keyword);
            books = new ArrayList<Book>();

            try {
                books = API_GoogleBooks.Request(keyword, it);
            } catch (Exception ex) {
                Log.e("FATAL","fatal 55555555" + ex);
                Alert.ShowError(it, "Erreur lors de l'appel à l'api Google Books", "" + ex);
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                List<String> livres = new ArrayList<>();
                for (Book b: books) {
                    livres.add(b.getTitle());
                }

                //found_list = (ListView) findViewById(R.id.found_list);
                adapter=new ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, livres);
                found_list.setAdapter(adapter);
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'affichage des données", "" + ex);
            }


        }

    }

}


