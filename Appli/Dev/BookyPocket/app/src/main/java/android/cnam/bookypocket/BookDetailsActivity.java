package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.AuthorBook;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.ReaderBook;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

public class BookDetailsActivity extends AppCompatActivity {

    //Title of the view
    private TextView detailsBookText;

    //Book details values
    private TextView title;
    private TextView author;
    private TextView publicationYear;
    private TextView category;
    private TextView description;
    private TextView nbPages;
    private TextView isbn_value;
    private ImageView image;

    private Book currentBook;
    private String authorName;
    private Author authorToInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        try {
            //TODO arranger ce code, il est moche
            Book book = getIntent().getParcelableExtra("book");
            authorName = getIntent().getStringExtra("author");

            initializeView();
            currentBook = book;
            updateView(book);
            insertInDb(book);
            //currentBook = book;
        } catch (Exception ex) {
            Alert.ShowError(this, "Erreur", "" + ex);
        }
    }

    private void insertInDb(Book book) {
        ORMSQLiteManager db = new ORMSQLiteManager(this);
        try {
            db.insertObjectInDB(book, Book.class);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try{
            authorToInsert = db.getAuthorFromName(authorName);
            if (authorToInsert == null) {
                authorToInsert = new Author();
                authorToInsert.setArtistName(authorName);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }



        try {
            if (authorToInsert != null) {
                AuthorBook ab = new AuthorBook(authorToInsert, book);
                db.insertObjectInDB(ab, AuthorBook.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
    }

    private void initializeView() {
        title = (TextView) findViewById(R.id.details_titleValue);
        author = (TextView) findViewById(R.id.details_author_value);
        publicationYear = (TextView) findViewById(R.id.details_publicationYearValue);
        category = (TextView) findViewById(R.id.details_categoryValue);
        description = (TextView) findViewById(R.id.description_textview);
        nbPages = (TextView) findViewById(R.id.nb_pages);
        isbn_value = (TextView) findViewById(R.id.isbn_value);
        image = (ImageView) findViewById(R.id.imageView);
    }

    private void updateView(Book book) {
        title.setText(book.getTitle());
        publicationYear.setText("" + book.getYearPublication());
        description.setText(book.getBackCover());
        nbPages.setText(book.getNbPages() + " pages");
        isbn_value.setText("ISBN : " + book.getISBN());
        if (!StringUtil.IsNullOrEmpty(authorName))
            author.setText(authorName);

        if (book.getCategory() != null)
            category.setText(book.getCategory().getName());
        try {
            if (book.getPhoto() != null) {
                byte[] chartData = book.getPhoto().getImage();
                Bitmap bm = BitmapFactory.decodeByteArray(chartData, 0, chartData.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                image.setMinimumHeight(dm.heightPixels);
                image.setMinimumWidth(dm.widthPixels);
                image.setImageBitmap(bm);
            }
        } catch (Exception ex) {

        }
    }

    public void GoBack(View view) {
        ChangeActivity.ChangeActivity(this, BookSearchActivity.class);
    }

    public void addToMyReadings(View view) {

        try {
            ReaderBook rb = new ReaderBook(Session.getCurrentUser(), currentBook);
            ORMSQLiteManager ormsqLiteManager = new ORMSQLiteManager(this);
            ormsqLiteManager.insertObjectInDB(rb, ReaderBook.class);
            Alert.ShowDialog(this, "Succès", "Ajout réussi à vos lectures");
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert.ShowError(this, "Erreur", "" + ex);
        }

    }

    public void go_preview_page(View view) {
        if (StringUtil.IsNullOrEmpty(currentBook.getPreviewLink())) {
            Alert.ShowDialog(this, "Information", "Lien vide");
            return;
        }
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(currentBook.getPreviewLink()));
            startActivity(i);
        } catch (Exception ex) {
            Alert.ShowDialog(this, "Information", "Lien mort");
        }
    }

    public void GoToAuthorPage(View view) {
        Intent intent = new Intent(this, AuthorActivity.class);
        try {
            if (!StringUtil.IsNullOrEmpty(authorName)) {
                intent.putExtra("author", authorName);
            }
            startActivity(intent);
        } catch (Exception ex) {
            Alert.ShowError(this, "Erreur", "" + ex);
        }

    }
}