package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.Model.Book;
import android.os.Bundle;
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
    private ImageView backCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Book book = getIntent().getParcelableExtra("book");
        initializeView();
        updateView(book);

    }

    private void initializeView() {

        title = (TextView) findViewById(R.id.details_titleValue);
        author = (TextView) findViewById(R.id.details_authorValue);
        publicationYear = (TextView) findViewById(R.id.details_publicationYearValue);
        category = (TextView) findViewById(R.id.details_categoryValue);
        genre = (TextView) findViewById(R.id.details_genreValue);
    }

    private void updateView(Book book){
        title.setText(book.getTitle());
        publicationYear.setText(""+book.getYearPublication());
    }
}