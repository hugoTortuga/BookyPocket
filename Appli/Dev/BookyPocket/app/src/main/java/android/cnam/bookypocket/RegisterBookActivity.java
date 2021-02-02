package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.*;
import android.cnam.bookypocket.Utils.Alert;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collection;
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

    //will be fulled with model data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_book);
        context = this;
        InitializeViewComponents();
    }

    private void InitializeViewComponents(){
        //details of the book to register
        titleValue = (EditText)findViewById(R.id.register_titleValue);
        this.title = titleValue.getText().toString();

        authorValue = (EditText)findViewById(R.id.register_authorValue);
        this.author = authorValue.getText().toString();

        publicationYearValue = (EditText)findViewById(R.id.register_publicationYearValue);
        this.publicationYear = publicationYearValue.getText().toString();

        createCategorySpinner();

        //Buttons
        scanButton = findViewById(R.id.register_scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scanOrTakePicture();
            }
        });

        addBookButton = findViewById(R.id.register_addButton);
        RegisterBook();
    }

    private void RegisterBook(){
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Book book = getInfoBookFromForm();
                    if(book == null)
                        return;

                    Reader reader = Session.getCurrentUser();

                    ReaderBook readerBook = new ReaderBook(reader, book);

                    if(reader != null){
                        //On insère notre reader
                        ORMSQLiteManager db = new ORMSQLiteManager(context);
                        db.insertObjectInDB(book, Book.class);
                        db.insertObjectInDB(readerBook, ReaderBook.class);
                        db.close();
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

    private Book getInfoBookFromForm() {
        Book b = new Book();
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
            ORMSQLiteManager DB_Manager = new ORMSQLiteManager(this);
            categories = DB_Manager.getAllObjects(Category.class);
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
        //BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
    }

    //Request to add a book
    private void addBook(){

    }

}