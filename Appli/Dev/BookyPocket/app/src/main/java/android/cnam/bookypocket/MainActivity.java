package android.cnam.bookypocket;

import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private Button findLibraryButton;
    private Button seeReadingsButton;
    private Button registerBookButton;
    private ImageButton manageAccountButton;
    private Button findBookButton;

    //private User user;
    //user info to request
    private String firstName;
    private String lastName;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
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

        registerBookButton = (Button) findViewById(R.id.home_button_registerBook);
        registerBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),RegisterBookActivity.class);
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
    }

    private void setNavigationViewListener() {
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }


}