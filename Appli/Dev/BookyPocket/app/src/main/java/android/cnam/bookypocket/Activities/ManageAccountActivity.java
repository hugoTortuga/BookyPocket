package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
            password.setText(currentUser.getPassword());
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
        ShowSaveDialog("Confirmation", "Etes-vous sur de vouloir sauvegarder ?");
    }

    public void deleteAccount(View view) {
        //TODO delete account
        Alert.ShowDialog(this, "Confirmation", "Etes-vous sur de supprimer votre comptre ?");
    }

    private Reader checkInfoReaderBeforeSave() throws Exception {

        if(currentUser == null)
            throw new Exception("L'utilisateur courant est null");

        //TODO tester le formet des input pas trop long bien formé etc...

        String emailStr = email.getText().toString();
        String pwdStr = password.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String firstNameStr = firstName.getText().toString();

        //On test les valeurs d'entrée
        if(emailStr != null && !emailStr.trim().isEmpty())
            currentUser.setEmailAddress(emailStr.trim());

        if(pwdStr != null && !pwdStr.trim().isEmpty())
            currentUser.setPassword(pwdStr.trim());

        if(lastNameStr != null && !lastNameStr.trim().isEmpty())
            currentUser.setLastName(lastNameStr.trim());

        if(firstNameStr != null && !firstNameStr.trim().isEmpty())
            currentUser.setFirstName(firstNameStr.trim());

        return currentUser;

    }

    private void ShowSaveDialog(String title, String msg){
        Context currentContext = this;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DataBaseSingleton.GetDataBaseSingleton(currentContext).updateReaderInfo(checkInfoReaderBeforeSave());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        Alert.ShowDialog(currentContext, "Succès", "Modification enregistrée");
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

}