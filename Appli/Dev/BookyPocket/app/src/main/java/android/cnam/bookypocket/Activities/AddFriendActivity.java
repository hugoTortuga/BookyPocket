package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Model.ReaderFriend;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
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
        InitializeView();
    }

    private void InitializeView(){
        friend_list = (ListView) findViewById(R.id.friends_found_list);
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
        ChangeActivity.ChangeActivity(this, FriendsActivity.class);
    }

    public void searchFriendButtonClick(){

        if(searchView.getQuery() != null)
            if(!StringUtil.IsNullOrEmpty(searchView.getQuery().toString())){
                try{
                    friends = DataBaseSingleton.GetDataBaseSingleton(this).getUserByKeyword(searchView.getQuery().toString());
                    if(friends == null)
                        friends = new ArrayList<>();
                    CustomReaderAdapter ca = new CustomReaderAdapter(this, (ArrayList<Reader>) friends);
                    friend_list.setAdapter(ca);
                    friend_list.setOnItemClickListener(this);
                }
                catch(Exception ex){
                    Alert.ShowDialog(this,"Erreur", "" + ex);
                }
            }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Reader friend = friends.get(position);
            if(Session.getCurrentUser() != null && friend != null){
                ReaderFriend rf = new ReaderFriend(Session.getCurrentUser(), friend);
                DataBaseSingleton.GetDataBaseSingleton(this).insertObjectInDB(rf, ReaderFriend.class);
            }
            Alert.ShowDialog(this,"Succès", "L'ami a été ajouté ");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}