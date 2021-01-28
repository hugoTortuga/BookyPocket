package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;

public class ManageAccountActivity extends AppCompatActivity {

    //User inputs for a book
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String dateOfBirth;

    private Button changePhoto;
    private Button deleteAccount;

    private Image userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //user info
        /*
        EditText lastNameValue = (EditText)findViewById(R.id.manage_lastNameValue);
        this.lastName = lastNameValue.getText().toString();

        EditText firstNameValue = (EditText)findViewById(R.id.manage_firstNameValue);
        this.firstName = firstNameValue.getText().toString();

        //need to verify pswrd
        EditText passwordValue = (EditText)findViewById(R.id.manage_passwordValue);
        this.password = passwordValue.getText().toString();

        changePhoto = findViewById(R.id.manage_changePhotoButton);
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserPhoto();
            }
        });

//        deleteAccount = findViewById(R.id.manage_deleteAccount);
//        deleteAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteUserAccount();
//            }
//        });
        deleteAccount = findViewById(R.id.manage_deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });


         */
    }

    private void changeUserPhoto(){
        //open file system and get new image file path
        //Image newImage = ...;
        //this.userPhoto = newImage;
    }

    private void deleteUserAccount(){
        //delete user account return to login activity
        //open "Are you sure you want to delete your account" dialog
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }
}