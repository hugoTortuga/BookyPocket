package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Activité rassemblant la liste d'amis
 */
public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Attributs
    private ListView friend_list;
    public List<Reader> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initializeView();
    }

    /**
     * Initialise les composants de la vue
     */
    private void initializeView() {
        if(Session.getCurrentUser() != null){
            try {
                friends = DataBaseSingleton.GetDataBaseSingleton(this).GetFriendsByReader(Session.getCurrentUser().getId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(friends == null){
                friends = new ArrayList<>();
            }
            friend_list = (ListView) findViewById(R.id.friends_list);

            CustomReaderAdapter ca = new CustomReaderAdapter(this, (ArrayList<Reader>) friends);
            friend_list.setAdapter(ca);
            friend_list.setOnItemClickListener(this);
        }
        else
            Alert.ShowDialog(this,"Anomalie","L'utilisateur courant est vide");
    }

    /**
     * Bouton retour click
     * @param view
     */
    public void goBackFriendsActivity(View view){
        ChangeActivity.ChangeActivity(this,MainActivity.class);
    }

    /**
     * Lorsqu'on click sur ami, redirige vers sa page ami
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(this, FriendPageActivity.class);
            Reader friend = friends.get(position);
            intent.putExtra("friend", (Reader)friend);
            this.startActivity(intent);
        } catch (Exception ex) {
            Alert.ShowDialog(this, "Erreur lors du changement de page", "" + ex);
        }
    }

    /**
     * Bouton ajouter un ami click, redirige vers la page ajouter un ami
     * @param view
     */
    public void add_friend_button_click(View view) {
        ChangeActivity.ChangeActivity(this, AddFriendActivity.class);
    }
}
