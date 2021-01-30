package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.utils.ChangeActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity {

    //Title of the view
    private TextView detailsBookText;

    //Book details values
    private TextView title;
    private TextView author;
    private TextView publicationYear;
    private TextView category;
    private TextView genre;
    private TextView description;
    private TextView nbPages;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Book book = getIntent().getParcelableExtra("book");

        Log.e("JSONME", "recevied book : " +book);
        initializeView();
        updateView(book);

    }

    private void initializeView() {

        title = (TextView) findViewById(R.id.details_titleValue);
        author = (TextView) findViewById(R.id.details_authorValue);
        publicationYear = (TextView) findViewById(R.id.details_publicationYearValue);
        category = (TextView) findViewById(R.id.details_categoryValue);
        genre = (TextView) findViewById(R.id.details_genreValue);
        description = (TextView) findViewById(R.id.description_textview);
        nbPages = (TextView) findViewById(R.id.nb_pages);
        image = (ImageView) findViewById(R.id.imageView);
    }

    private void updateView(Book book){
        title.setText(book.getTitle());
        publicationYear.setText(""+book.getYearPublication());
        description.setText(book.getBackCover());
        nbPages.setText(book.getNbPages() + " pages");
        if(book.getCategory() != null)
            category.setText(book.getCategory().getName());
        try{
            if(book.getPhoto() != null) {
                byte[] chartData = book.getPhoto().getImage();
                Bitmap bm = BitmapFactory.decodeByteArray(chartData, 0, chartData.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                image.setMinimumHeight(dm.heightPixels);
                image.setMinimumWidth(dm.widthPixels);
                image.setImageBitmap(bm);
            }
        }catch(Exception ex) {

        }
    }

    public void GoBack(View view) {
        ChangeActivity.ChangeActivity(this, BookSearchActivity.class);
    }
}