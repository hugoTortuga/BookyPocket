package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView friend_list;
    public List<Reader> friends;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        friend_list = (ListView) findViewById(R.id.friends_list);
        searchView = (SearchView) findViewById(R.id.searchview_add_friend);




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFriendButtonClick();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });
    }

    public void goBackFriendsActivity(View view) {
        finish();
    }

    public void searchFriendButtonClick() {

        friends = new ArrayList<>();
        Alert.ShowDialog(this, "Recherche", "");
        Reader r = new Reader("mail", "password", "nom", "prenom", null, null);
        friends.add(r);
        friends.add(r);
        friends.add(r);



        CustomReaderAdapter ca = new CustomReaderAdapter(this, (ArrayList<Reader>) friends);
        friend_list.setAdapter(ca);
        friend_list.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Reader friend = friends.get(position);
            Alert.ShowDialog(this,"Succès", "La demande d'amie doit " + friend);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}