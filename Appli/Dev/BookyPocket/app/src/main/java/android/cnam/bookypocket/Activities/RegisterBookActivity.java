package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.*;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.Network;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterBookActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText titleValue;
    private String title;
    private EditText authorValue;
    private String author;
    private EditText publicationYearValue;
    private String publicationYear;
    private Spinner categorySpinner;
    private Button scanButton;
    private Button addBookButton;

    private Context context;

    private  String getISBNFromIntent;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_book);
        context = this;

        InitializeViewComponents();

        getISBNFromIntent = getIntent().getStringExtra("ISBN");

        if(!StringUtil.IsNullOrEmpty(getISBNFromIntent)){
            try {
                getBookFromScan();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void getBookFromScan() throws Exception {
        //CHeck if connected
        if(Network.isNetworkAvailable(this)) {
            Book book = DataBaseSingleton.GetDataBaseSingleton(context).getBookByISBN(getISBNFromIntent);
            if (book != null) {
                this.titleValue.setText(book.getTitle());
                //multiple values maybe
                //this.authorValue.setText(book.getAuthor());
                this.publicationYearValue.setText(book.getYearPublication());
                updateInterface(book);
            }
            else{
                CallGoogleBookAPI task = new CallGoogleBookAPI(this, getISBNFromIntent);
                task.execute();
            }
        }
    }

    private void InitializeViewComponents(){

        titleValue = (EditText)findViewById(R.id.register_titleValue);
        authorValue = (EditText)findViewById(R.id.register_authorValue);
        publicationYearValue = (EditText)findViewById(R.id.register_publicationYearValue);

        createCategorySpinner();

        //Buttons
        scanButton = findViewById(R.id.register_scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanOrTakePicture();
            }
        });

        if(getISBNFromIntent != null){
            try {
                getBookFromScan();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        addBookButton = findViewById(R.id.register_addButton);
        RegisterBook();
    }

    private void RegisterBook(){
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("******************************ISBN******************************" + getISBNFromIntent);

                try {

                    if(currentBook == null)
                        return;

                    Reader reader = Session.getCurrentUser();

                    ReaderBook readerBook = new ReaderBook(reader, currentBook);

                    if(reader != null){
                        //On insère notre reader
                        DataBaseSingleton.GetDataBaseSingleton(context).insertObjectInDB(currentBook, Book.class);
                        DataBaseSingleton.GetDataBaseSingleton(context).insertObjectInDB(readerBook, ReaderBook.class);
                        Alert.ShowDialog(context, "Succès", "Livre enregistré");
                    }

                    //On retourne à l'écran de connexion
                    //goToHome();

                } catch (Exception ex) {
                    if(ex.getClass() == java.sql.SQLException.class)
                        Alert.ShowDialog(context, "Erreur", "Le livre a été enregistré");
                    else
                        Alert.ShowDialog(context, "Erreur", ex.getMessage());

                }
            }
        });
    }

    private Book getInfoBookFromForm(Book scannedBook) {
        Book b ;
        if(scannedBook != null){
            b = scannedBook;
        } else{
            b = new Book();
        }
        if(title == null || title.trim().equals("")){
            Alert.ShowDialog(this, "Information manquante", "Le titre n'est pas renseigné");
            return null;
        }

        // TODO check all info
        b.setCategory((Category)this.categorySpinner.getSelectedItem());

        return b;
    }

    private void createCategorySpinner(){

        try{
            List<Category> categories = new ArrayList<>();
            categories = DataBaseSingleton.GetDataBaseSingleton(this).getAllObjects(Category.class);
            List<String> libelleCategories = new ArrayList<>();
            for(Category cat : categories){
                libelleCategories.add(cat.getName());
            }
            Collections.sort(libelleCategories);
            categorySpinner = (Spinner) findViewById(R.id.register_categorySpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,
                    libelleCategories);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.categorySpinner.setAdapter(adapter);

            // When user select a List-Item.
            this.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onItemSelectedHandlerCategory(parent, view, position, id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private void onItemSelectedHandlerCategory(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
//        Category category = (Category) adapter.getItem(position);
        //this.category = category;
//        Toast.makeText(getApplicationContext(), "Selected Category : " + category.getName() ,Toast.LENGTH_SHORT).show();
    }


    private void scanOrTakePicture(){
        ChangeActivity.ChangeActivity(this, BarCodeReaderActivity.class);
    }

    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    public void updateInterface(Book book){
        if(book != null){
            titleValue.setText(book.getTitle());
            //if(book.getAuthor() != null)
            //    authorValue.setText(book.getAuthor().getArtistName());
            //publicationYearValue.setText(book.getYearPublication());
            //if(book.getCategory() != null){
            //    //TODO afficher la catégorie dans le spinner
            //}
            currentBook = book;
        }
    }



    /**
     * Classe interne qui ne sert qu'à faire des appels asynchrone à l'api google BOOK
     * obligatoire pour ne pas encombre le thread UI principal
     * voir pour externaliser cette portion de code dans un fichier ?
     */
    private class CallGoogleBookAPI extends AsyncTask<Void, Void, String> {

        private Book book;
        private RegisterBookActivity it;
        private String isbn;
        private ProgressDialog dialog;

        public CallGoogleBookAPI(RegisterBookActivity _it, String _isbn) {
            super();
            it = _it;
            isbn = _isbn;
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
            try {
                List<Book> books = API_GoogleBooks.RequestISBN(isbn, it);
                if(books != null)
                    if(books.size()>0)
                        book = books.get(0);
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'appel à l'api Google Books", "" + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            updateInterface(book);
        }
    }


}