package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.Network;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

/**
 * Activité de la recherche de livres : contient une classe privée qui extend asynTask pour envoyer
 * des requetes http à google book
 */
public class BookSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Attributs
    private SearchView searchView;
    private ListView found_list;
    public List<Book> books_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        initializeView();
        updateListInterface();
    }

    //Initialise tous les éléments de la vue
    private void initializeView(){
        searchView = (SearchView) findViewById(R.id.search_book_button);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBook(null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

        found_list = (ListView) findViewById(R.id.found_list);
    }

    //Si la session contient déjà des livres recherchés, les affiches
    public void updateListInterface(){
        List<Book> bookInSession = Session.getBooks();
        if(bookInSession != null){
            if(bookInSession.size() > 0){
                books_list = bookInSession;
                CustomBookAdapter ca = new CustomBookAdapter(this, (ArrayList<Book>) books_list);
                //adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, livres);
                found_list.setAdapter(ca);
                found_list.setOnItemClickListener(this);
            }
            else{
                Alert.ShowDialog(this, "Vide", "Aucun livre n'a été trouvé avec ces mots-clefs");
            }
        }
    }

    /**
     * Méthode permettant d'appelé une requete asynchrone de recherche de livre
     * à partie des mots clefs renseignées par le user
     * @param view
     */
    public void searchBook(View view){
        try{

            // si on est connecté à internet, on cherche sur google books
            if(Network.isNetworkAvailable( this)){
                if(searchView.getQuery() == null || searchView.getQuery().equals(""))
                    Alert.ShowDialog(this, "0 résultat","Le champ de recherche est vide");

                else{
                    String keyword = searchView.getQuery().toString();
                    CallGoogleBookAPI task = new CallGoogleBookAPI(this, keyword);
                    task.execute();
                }
            }
            //si on est pas connecté à internet on va chercher dans la base embarqué
            else{
                Alert.ShowDialog(this, "Pas de connexion internet","La recherche de livre est limitée à la base embarquée");
                String keyword = searchView.getQuery().toString();
                List<Book> books = DataBaseSingleton.GetDataBaseSingleton(this).getBooksByKeyWord(searchView.getQuery().toString().split(" "));
                if(books != null)
                    if(books.size()>0)
                        Session.setBooks(books);
                updateListInterface();
            }

        }catch(Exception ex){
            Alert.ShowError(this, "Le recherche de livre a provoqué une erreur",
                    "" + ex);
        }
    }

    /**
     * Bouton pour revenir en arrière
     * @param view
     */
    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    /**
     * Click sur un livre, redirige vers l'activité détail livre
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            Book book = (Book) parent.getItemAtPosition(position);
            Book bookToSend = books_list.get(position);
            if(book != null)
                ChangeActivity.GoToBookDetailActivity(this, book);
        }
        catch (Exception ex){
            Alert.ShowDialog(this, "Erreur lors du changement de page", "Le livre n'a pas d'ISBN");
        }
    }

    /**
     * Ajoute les livres recherchés dans les données de session
     * @param _books
     */
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
        private ProgressDialog dialog;

        public CallGoogleBookAPI(BookSearchActivity _it, String _keyword) {
            super();
            it = _it;
            keyword = _keyword;
            dialog = new ProgressDialog(_it);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Chargement...");
            dialog.setIndeterminate(true);
            dialog.show();
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
                Alert.ShowError(it, "Erreur lors de l'appel à l'api Google Books", "" + ex);
            }
            return "";
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                dialog.dismiss();
                setBookCollection(books);
                updateListInterface();
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'affichage des données", "" + ex);
            }
        }
    }

}


