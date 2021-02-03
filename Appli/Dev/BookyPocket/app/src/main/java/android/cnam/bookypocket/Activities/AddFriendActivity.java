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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView friend_list;
    public List<Reader> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friends = new ArrayList<>();
    }

    public void goBackFriendsActivity(View view) {
        finish();
    }

    public void searchFriendButtonClick(View view) {
        Reader r = new Reader("maile", "password", "nom", "prenom", null, null);
        friends.add(r);
        friends.add(r);
        friends.add(r);

        friend_list = (ListView) findViewById(R.id.friends_list);

        CustomReaderAdapter ca = new CustomReaderAdapter(this, (ArrayList<Reader>) friends);
        friend_list.setAdapter(ca);
        friend_list.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Reader friend = friends.get(position);
            Alert.ShowDialog(this,"Succès", "La demande d'amie doit");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}