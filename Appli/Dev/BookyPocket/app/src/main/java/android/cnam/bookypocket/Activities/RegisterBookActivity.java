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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterBookActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText titleValue;
    private EditText authorValue;
    private EditText publicationYearValue;
    private EditText description_manual;
    private Spinner categorySpinner;
    private ImageView image;
    private EditText register_isbn_manual;
    private Context context;

    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_book);

        currentBook = new Book();
        context = this;
        InitializeViewComponents();
    }

    private void InitializeViewComponents() {

        titleValue = (EditText) findViewById(R.id.register_titleValue);
        authorValue = (EditText) findViewById(R.id.register_authorValue);
        publicationYearValue = (EditText) findViewById(R.id.register_publicationYearValue);
        description_manual = (EditText) findViewById(R.id.description_manual);
        image = (ImageView) findViewById(R.id.imageViewBookManual);
        register_isbn_manual = (EditText) findViewById(R.id.register_isbn_manual);
        createCategorySpinner();
    }

    public void saveBook(View view) {
        if (currentBook == null)
            return;

        if(StringUtil.IsNullOrEmpty(register_isbn_manual.getText().toString()))
            Alert.ShowDialog(this,"Formulaire", "Vous devez remplir l'ISBN");
        currentBook.setISBN(register_isbn_manual.getText().toString());

        if(StringUtil.IsNullOrEmpty(titleValue.getText().toString()))
            Alert.ShowDialog(this,"Formulaire", "Vous devez remplir le titre");
        currentBook.setTitle(titleValue.getText().toString());

        try{

        }catch (Exception ex){
            currentBook.setYearPublication(Integer.parseInt(publicationYearValue.getText().toString()));
        }

        Author a = new Author();
        try{

            a.setArtistName(authorValue.getText().toString());
            currentBook.setAuthor(a);
            currentBook.setBackCover(description_manual.getText().toString());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            Reader reader = Session.getCurrentUser();
            ReaderBook readerBook = new ReaderBook(reader, currentBook);

            if (reader != null) {
                //On insère notre reader
                DataBaseSingleton.GetDataBaseSingleton(context).insertObjectInDB(currentBook, Book.class);
                DataBaseSingleton.GetDataBaseSingleton(context).insertObjectInDB(readerBook, ReaderBook.class);
                Alert.ShowDialog(context, "Succès", "Livre enregistré");
            }

            //On retourne à l'écran de connexion
            //goToHome();

        } catch (Exception ex) {
                Alert.ShowError(context, "Erreur", "" + ex);

        }
    }


    private void createCategorySpinner() {

        try {
            List<Category> categories = new ArrayList<>();
            categories = DataBaseSingleton.GetDataBaseSingleton(this).getAllObjects(Category.class);
            List<String> libelleCategories = new ArrayList<>();
            for (Category cat : categories) {
                libelleCategories.add(cat.getName());
            }
            Collections.sort(libelleCategories);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception ex) {
            Alert.ShowDialog(this,"Erreur", ""+ex);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);

            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Photo p = new Photo();
                p.setImage(byteArray);
                currentBook.setPhoto(p);

            } catch (Exception ex) {
                Alert.ShowDialog(this,"Erreur", "" + ex);
            }

        }
    }


    private void onItemSelectedHandlerCategory(AdapterView<?> adapterView, View view,
                                               int position, long id) {
        Adapter adapter = adapterView.getAdapter();
//        Category category = (Category) adapter.getItem(position);
        //this.category = category;
//        Toast.makeText(getApplicationContext(), "Selected Category : " + category.getName() ,Toast.LENGTH_SHORT).show();
    }


    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

}