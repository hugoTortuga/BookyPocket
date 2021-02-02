package android.cnam.bookypocket;

import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Utils.Alert;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AuthorActivity extends AppCompatActivity {

    private TextView nameAuthor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_activity);
        nameAuthor = (TextView) findViewById(R.id.idNomAuthor);
        try{
            String authorParam = getIntent().getStringExtra("author");
            nameAuthor.setText(authorParam);
        }
        catch (Exception ex){
            Alert.ShowError(this, "Erreur" ,""+ ex);
        }
    }

    public void GoBackActivity(View view) {
        this.finish();
    }
}
