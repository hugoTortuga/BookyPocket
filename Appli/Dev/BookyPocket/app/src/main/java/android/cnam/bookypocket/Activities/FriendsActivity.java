package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchView searchView;

    private ListView friend_list;
    public List<Reader> friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friends = new ArrayList<>();

        Reader r = new Reader("maile", "password","nom","prenom", null, null);
        friends.add(r);
        friends.add(r);
        friends.add(r);

        friend_list = (ListView) findViewById(R.id.friends_list);

        CustomReaderAdapter ca = new CustomReaderAdapter(this, (ArrayList<Reader>) friends);
        friend_list.setAdapter(ca);
        friend_list.setOnItemClickListener(this);


    }

    public void goBackFriendsActivity(View view){
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(this, FriendPageActivity.class);
            Reader friend = friends.get(position);
            intent.putExtra("friend", friend);
            this.startActivity(intent);
        } catch (Exception ex) {
            Alert.ShowDialog(this, "Erreur lors du changement de page", "" + ex);
        }
    }

}
