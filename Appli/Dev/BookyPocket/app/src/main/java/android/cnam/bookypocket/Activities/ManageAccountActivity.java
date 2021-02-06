package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.CheckForm;
import android.cnam.bookypocket.Utils.Cryptography;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManageAccountActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText currentpassword;
    private EditText newpassword;
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

        if (Session.getCurrentUser() == null) {
            Alert.ShowDialog(this, "Information", "L'utilisateur courant n'a pas pu être récupéré");
            return;
        }

        currentUser = Session.getCurrentUser();
        lastName = (EditText) findViewById(R.id.manage_lastNameValue);
        firstName = (EditText) findViewById(R.id.manage_firstNameValue);
        email = (EditText) findViewById(R.id.manage_email_edit);
        currentpassword = (EditText) findViewById(R.id.id_current_password);
        newpassword = (EditText) findViewById(R.id.id_new_password);
        try {

            dateOfBirth = (EditText) findViewById(R.id.editTextDate);
            if (currentUser.getDateOfBirth() != null) {
                Date date = currentUser.getDateOfBirth();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateOfBirth.setText(dateFormat.format(date));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            lastName.setText(currentUser.getLastName());
            firstName.setText(currentUser.getFirstName());
            email.setText(currentUser.getEmailAddress());
        } catch (Exception ex) {

        }
    }

    private void changeUserPhoto() {
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

        if (currentUser == null)
            throw new Exception("L'utilisateur courant est null");

        //TODO tester le formet des input pas trop long bien formé etc...

        String emailStr = email.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String firstNameStr = firstName.getText().toString();
        String date = dateOfBirth.getText().toString();

        //On test les valeurs d'entrée


        if (emailStr != null && !emailStr.trim().isEmpty())
            currentUser.setEmailAddress(emailStr.trim());

        if (lastNameStr != null && !lastNameStr.trim().isEmpty())
            currentUser.setLastName(lastNameStr.trim());

        if (firstNameStr != null && !firstNameStr.trim().isEmpty())
            currentUser.setFirstName(firstNameStr.trim());

        Date dateBirth = null;
        try {
            dateBirth = CheckForm.CheckAndCastStringToDate(date, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (dateBirth != null)
            currentUser.setDateOfBirth(dateBirth);

        return currentUser;

    }

    private void ShowSaveDialog(String title, String msg) {
        Context currentContext = this;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {


                            String curentpwdStr = currentpassword.getText().toString();
                            String newPasswdStr = newpassword.getText().toString();
                            //si on a un new password et pas de current password
                            if (StringUtil.IsNullOrEmpty(curentpwdStr) && !StringUtil.IsNullOrEmpty(newPasswdStr)) {
                                Alert.ShowDialog(currentContext,"Information", "Vous devez renseigner votre mot de passe courant pour mettre un nouveau mot de passe");
                                return;
                            } else if (!StringUtil.IsNullOrEmpty(curentpwdStr) && !StringUtil.IsNullOrEmpty(newPasswdStr)) {
                                if(!Cryptography.Hash(curentpwdStr).equals(currentUser.getPassword())) {
                                    Alert.ShowDialog(currentContext, "Information", "Le mot de passe courant n'est pas bon");
                                    return;
                                }
                                else{
                                    //le mot de passe courant est le bon
                                    newPasswdStr = Cryptography.Hash(newPasswdStr);
                                    currentUser.setPassword(newPasswdStr);
                                }
                            }

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