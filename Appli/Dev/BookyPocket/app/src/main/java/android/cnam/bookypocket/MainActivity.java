package android.cnam.bookypocket;

import android.cnam.bookypocket.ui.readings.ReadingsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        int nav_home = R.id.nav_home;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu menu = navigationView.getMenu();
        MenuItem homeItem = menu.findItem(R.id.nav_home);

        MenuItem mapItem = menu.findItem(R.id.nav_map);

        MenuItem readingsItem = menu.findItem(R.id.nav_readings);
        setNavigationViewListener();

        ImageView userPhoto = (ImageView)findViewById(R.id.imageUserView);

        //on devra peut être enregistrer les photos en local pour les retrouver avec un objet drawable
        //userPhoto.setImageResource(R.drawable.woman);

        TextView textPseudoUser = (TextView) findViewById(R.id.pseudoUser);
        //textView.setText("firstname + lastname");

        TextView textMailUser = (TextView) findViewById(R.id.mailUser);
        //textView.setText("mailUser");

        createButtons();
    }
    private void createButtons(){
        findLibraryButton = (Button) findViewById(R.id.home_button_findLibrary) ;
        findLibraryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerBookIntent = new Intent(view.getContext(), BookyMapActivity.class);
                startActivity(registerBookIntent);
            }
        });

        seeReadingsButton = (Button) findViewById(R.id.home_button_seeReadings) ;
        seeReadingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerBookIntent = new Intent(view.getContext(), ReadingsFragment.class);
                startActivity(registerBookIntent);
            }
        });

        registerBookButton = (Button) findViewById(R.id.home_button_registerBook) ;
        registerBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerBookIntent = new Intent(view.getContext(), RegisterBookActivity.class);
                startActivity(registerBookIntent);
            }
        });

        manageAccountButton = (Button) findViewById(R.id.home_button_manageAccount) ;
        manageAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerBookIntent = new Intent(view.getContext(), BookDetailsActivity.class);
                startActivity(registerBookIntent);
            }
        });

    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_home:
                //open home/main activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                return true;
            case R.id.nav_map:
                //open map activity
                Intent mapIntent = new Intent(this, BookyMapActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.nav_readings:
                //open readings activity
                Intent readingsIntent = new Intent(this, ReadingsActivity.class);
                startActivity(readingsIntent);
                return true;
        }
                return true;
//            default:
//                return super.onOptionsItemSelected(item);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.nav_home:
//                //open home/main activity
//
//                return true;
//            case R.id.nav_map:
//                //open map activity
//                Intent intent = new Intent(this, BookyMapActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.nav_readings:
//                //open readings activity
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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