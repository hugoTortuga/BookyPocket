package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private Button findLibraryButton;
    private Button seeReadingsButton;
    private ImageButton manageAccountButton;
    private Button friendsButton;
    private Button findBookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButtons();
    }

    private void createButtons() {
        findLibraryButton = (Button) findViewById(R.id.home_button_findLibrary);
        findLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),BookyMapActivity.class);
            }
        });

        seeReadingsButton = (Button) findViewById(R.id.home_button_seeReadings);
        seeReadingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),ReadingsActivity.class);
            }
        });


        manageAccountButton = (ImageButton) findViewById(R.id.home_button_manageAccount);
        manageAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),ManageAccountActivity.class);
            }
        });
        findBookButton = (Button) findViewById(R.id.find_book_button);
        findBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),BookSearchActivity.class);
            }
        });
        friendsButton = (Button) findViewById(R.id.home_button_friend);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(), FriendsActivity.class);
            }
        });
    }


    public void disconnectButton(View view) {
        Session.setCurrentUser(null);
        Session.setBooks(null);
        this.finish();
        ChangeActivity.ChangeActivity(this,LoginActivity.class);
    }
}