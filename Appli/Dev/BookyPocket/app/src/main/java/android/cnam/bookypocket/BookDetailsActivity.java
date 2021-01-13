package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity {

    //Title of the view
    private TextView detailsBookText;

    //Book details values
    private TextView detailsTitleValue;
    private TextView detailsAuthorValue;
    private TextView detailsPublicationYearValue;
    private TextView detailsCategoryValue;
    private TextView detailsGenreValue;

    private ImageView backCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
    }
}