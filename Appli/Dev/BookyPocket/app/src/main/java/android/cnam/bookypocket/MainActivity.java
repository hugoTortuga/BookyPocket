package android.cnam.bookypocket;

import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private Button findLibraryButton;
    private Button seeReadingsButton;
    private Button registerBookButton;
    private Button manageAccountButton;
    private Button findBookButton;

    //private User user;
    //user info to request
    private String firstName;
    private String lastName;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);


        int nav_home = R.id.nav_home;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        Menu menu = navigationView.getMenu();
//        MenuItem homeItem = menu.findItem(R.id.nav_home);
//
//        MenuItem mapItem = menu.findItem(R.id.nav_map);
//
//        MenuItem readingsItem = menu.findItem(R.id.nav_readings);
//        setNavigationViewListener();

        //ImageView userPhoto = (ImageView) findViewById(R.id.imageUserView);

        //on devra peut être enregistrer les photos en local pour les retrouver avec un objet drawable
        //userPhoto.setImageResource(R.drawable.woman);

        //TextView textPseudoUser = (TextView) findViewById(R.id.pseudoUser);
        //TextView textMailUser = (TextView) findViewById(R.id.mailUser);

        Reader user = Session.getCurrentUser();

        //if (textPseudoUser == null || textMailUser == null || user == null)
        //    return;

        //textPseudoUser.setText(user.getFirstName());
        //textMailUser.setText(user.getEmailAddress());

        createButtons();
    }

    private void createButtons() {
        findLibraryButton = (Button) findViewById(R.id.home_button_findLibrary);
        findLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeActivity.ChangeActivity(view.getContext(),BookyMapTestActivity.class);
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

        manageAccountButton = (Button) findViewById(R.id.home_button_manageAccount);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_home:
                ChangeActivity.ChangeActivity(this,MainActivity.class);

                return true;
            case R.id.nav_map:
                //open map activity
                ChangeActivity.ChangeActivity(this,BookyMapActivity.class);
                return true;
            case R.id.nav_readings:
                //open readings activity
                ChangeActivity.ChangeActivity(this,ReadingsActivity.class);
                return true;
        }
        return true;
//            default:
//                return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}