package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class RegisterBookActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText titleValue;
    private String title;
    private EditText authorValue;
    private String author;
    private EditText publicationYearValue;
    private String publicationYear;
    private Spinner categorySpinner;
    private Spinner genreSpinner;
    private Button scanButton;
    private ImageButton addBookButton;

    //will be fulled with model data
    String[] categories = {"Hello", "World"};
    String[] genres = {"Epic", "Romantic"};

    //private Category category;
    //private Genre genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_book);

        //details of the book to register
        titleValue = (EditText)findViewById(R.id.register_titleValue);
        this.title = titleValue.getText().toString();

        authorValue = (EditText)findViewById(R.id.register_authorValue);
        this.author = authorValue.getText().toString();

        publicationYearValue = (EditText)findViewById(R.id.register_publicationYearValue);
        this.publicationYear = publicationYearValue.getText().toString();

        //category and genre of the book to register
        createCategorySpinner();
        createGenreSpinner();

        //Buttons
        scanButton = findViewById(R.id.register_scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scanOrTakePicture();
            }
        });

        addBookButton = findViewById(R.id.register_addButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addBook();
            }
        });
    }

    private void createCategorySpinner(){
        categorySpinner = (Spinner) findViewById(R.id.register_categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                categories);

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

    private void createGenreSpinner(){
        genreSpinner = (Spinner) findViewById(R.id.register_genreSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                genres);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.genreSpinner.setAdapter(adapter);

        // When user select a List-Item.
        this.genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandlerGenre(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onItemSelectedHandlerCategory(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
//        Category category = (Category) adapter.getItem(position);
        //this.category = category;
//        Toast.makeText(getApplicationContext(), "Selected Category : " + category.getName() ,Toast.LENGTH_SHORT).show();
    }

    private void onItemSelectedHandlerGenre(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
//        Genre genre = (Genre) adapter.getItem(position);
        //this.genre = genre;
//        Toast.makeText(getApplicationContext(), "Selected Genre: " + genre.getName() ,Toast.LENGTH_SHORT).show();
    }

    private void scanOrTakePicture(){

    }

    //Request to add a book
    private void addBook(){

    }
}