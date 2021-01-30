package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ManageAccountActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText dateOfBirth;

    private ImageView userPhoto;

    private Reader currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        initilizeForm();
    }

    private void initilizeForm() {

        if(Session.getCurrentUser() == null)
        {
            Alert.ShowDialog(this,"Information", "L'utilisateur courant n'a pas pu être récupéré");
            return;
        }

        currentUser = Session.getCurrentUser();

        lastName = (EditText) findViewById(R.id.manage_lastNameValue);
        firstName = (EditText) findViewById(R.id.manage_firstNameValue);
        email = (EditText) findViewById(R.id.manage_email_edit);
        password = (EditText) findViewById(R.id.manage_passwordValue);
        try{
            dateOfBirth = (EditText) findViewById(R.id.manage_DateOfBirth);
            dateOfBirth.setText(currentUser.getDateOfBirth().toString());
        }
        catch (Exception ex){

        }
        try{
            lastName.setText(currentUser.getLastName());
            firstName.setText(currentUser.getFirstName());
            email.setText(currentUser.getEmailAddress());
            password.setText("******");
        }
        catch (Exception ex){

        }
    }

    private void changeUserPhoto(){
        //open file system and get new image file path
        //Image newImage = ...;
        //this.userPhoto = newImage;
    }

    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    public void save(View view) {
        // TODO save modifications
    }

    public void deleteAccount(View view) {
        //TODO delete account
    }
}