package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.AuthorBook;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.ReaderBook;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity {

    //Attributs
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

            Book book = initializeView();
            if(book != null){
                currentBook = book;
                updateView(book);
                //On insère le livre dans la BD, ainsi les livres dont on a consulté les infos pourront
                //etre accessible meme en mode déconnecté
                insertInDb(book);
            }

    }

    /**
     * On check les infos du livre et on le sauvegarde
     * @param book
     */
    private void insertInDb(Book book) {
        try {
            DataBaseSingleton.GetDataBaseSingleton(this).insertObjectInDB(book, Book.class);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        try{
            authorToInsert = DataBaseSingleton.GetDataBaseSingleton(this).getAuthorFromName(authorName);
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
                DataBaseSingleton.GetDataBaseSingleton(this).insertObjectInDB(ab, AuthorBook.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * On initialize la vue et on renvoi un livre, le livre du context de la vue
     * @return
     */
    private Book initializeView() {
        //Initialisation des composants
        title = (TextView) findViewById(R.id.details_titleValue);
        author = (TextView) findViewById(R.id.details_author_value);
        publicationYear = (TextView) findViewById(R.id.details_publicationYearValue);
        category = (TextView) findViewById(R.id.details_categoryValue);
        description = (TextView) findViewById(R.id.description_textview);
        nbPages = (TextView) findViewById(R.id.nb_pages);
        isbn_value = (TextView) findViewById(R.id.isbn_value);
        image = (ImageView) findViewById(R.id.imageView);

        //récupération des paramètres
        try {
            Book book = getIntent().getParcelableExtra("book");
            authorName = getIntent().getStringExtra("author");
            if(book != null && !StringUtil.IsNullOrEmpty(authorName))
                if(book.getAuthor() == null){
                    Author a = DataBaseSingleton.GetDataBaseSingleton(this).getAuthorFromName(authorName);
                    if(a != null)
                        book.setAuthor(a);
                    else{
                        a = new Author();
                        a.setArtistName(authorName);
                        book.setAuthor(a);
                    }
                }
            return book;
        } catch (Exception ex) {
            Alert.ShowError(this, "Erreur", "" + ex);
            return null;
        }
    }

    /**
     * On met à jour les champs de la vue à partir des infos du livre courant
     * @param book
     */
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

    /**
     * Bouton retour click
     * @param view
     */
    public void GoBack(View view) {
        this.finish();
    }

    /**
     * Ajoute le livre dans les lectures de l'utilisateur courant
     * @param view
     */
    public void addToMyReadings(View view) {

        try {
            ReaderBook rb = new ReaderBook(Session.getCurrentUser(), currentBook);
            DataBaseSingleton.GetDataBaseSingleton(this).insertObjectInDB(rb, ReaderBook.class);
            Alert.ShowDialog(this, "Succès", "Ajout réussi à vos lectures");
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert.ShowDialog(this, "Erreur", "Le livre est malformé ou il est déjà présent dans vos lectures");
        }

    }

    /**
     * redirige vers la page google book du bouquin
     * @param view
     */
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

    /**
     * Redirige vers la page de l'auteur
     * @param view
     */
    public void GoToAuthorPage(View view) {
        Intent intent = new Intent(this, AuthorActivity.class);
        try {
            if (!StringUtil.IsNullOrEmpty(authorName)) {
                intent.putExtra("author", authorName);
            }
            startActivity(intent);
        } catch (Exception ex) {
            Alert.ShowError(this, "Erreur", "Cet auteur n'est pas référencé");
        }

    }
}