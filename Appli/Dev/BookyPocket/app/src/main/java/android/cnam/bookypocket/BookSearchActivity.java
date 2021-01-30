package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class BookSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchView searchView;
    private ImageButton itemImageButton;
    private ArrayList<String> titleList = new ArrayList<>();

    private ListView found_list;
    public List<Book> books_list;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);

        searchView = (SearchView) findViewById(R.id.search_book_button);
        found_list = (ListView) findViewById(R.id.found_list);
        updateListInterface();
    }

    public void updateListInterface(){
        List<Book> bookInSession = Session.getBooks();
        if(bookInSession != null){
            List<String> livres = new ArrayList<>();
            for (Book b: bookInSession) {
                livres.add(b.getTitle());
            }
            books_list = bookInSession;

            CustomAdapter ca = new CustomAdapter(this, (ArrayList<Book>) books_list);
            //adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, livres);
            found_list.setAdapter(ca);
            found_list.setOnItemClickListener(this);
        }
    }

    public void searchBook(View view){
        try{
            if(Session.isUserConnectedToInternet()){
                if(searchView.getQuery() == null || searchView.getQuery().equals(""))
                    Alert.ShowDialog(this, "0 résultat","Le champ de recherche est vide");

                else{
                    String keyword = searchView.getQuery().toString();
                    CallGoogleBookAPI task = new CallGoogleBookAPI(this, keyword);
                    task.execute();
                }
            }
            else{
                Alert.ShowDialog(this, "Pas de connexion internet","La recherche de livre est impossible sans connexion internet");
            }

        }catch(Exception ex){
            Alert.ShowError(this, "Le recherche de livre a provoqué une erreur",
                    "" + ex);
        }
    }

    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            Intent intent = new Intent(this, BookDetailsActivity.class);
            Book bookToSend = books_list.get(position);
            intent.putExtra("book", bookToSend);
            this.startActivity(intent);
        }
        catch (Exception ex){
            Alert.ShowDialog(this, "Erreur lors du changement de page", "" + ex);
        }
    }


    public void setBookCollection(List<Book> _books){
        books_list = _books;
        Session.setBooks(_books);
    }

    /**
    * Classe interne qui ne sert qu'à faire des appels asynchrone à l'api google BOOK
    * obligatoire pour ne pas encombre le thread UI principal
    * voir pour externaliser cette portion de code dans un fichier ?
    */
    private class CallGoogleBookAPI extends AsyncTask<Void, Void, String> {

        private List<Book> books;
        private BookSearchActivity it;
        private String keyword;

        public CallGoogleBookAPI(BookSearchActivity _it, String _keyword) {
            super();
            it = _it;
            keyword = _keyword;
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                Looper.prepare();
            }catch(Exception ex){
                ex.printStackTrace();
            }

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
                setBookCollection(books);
                updateListInterface();
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'affichage des données", "" + ex);
            }
        }
    }

}


