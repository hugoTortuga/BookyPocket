package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Photo;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.CheckForm;
import android.cnam.bookypocket.Utils.Cryptography;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activité comportant la gestion du compte, mise à jour des infos ou suppression
 */
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

    /**
     * Initialise les données du formulaire
     */
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
        userPhoto = (ImageView) findViewById(R.id.imageUserView);
        try {

            dateOfBirth = (EditText) findViewById(R.id.editTextDate);
            if (currentUser.getDateOfBirth() != null) {
                Date date = currentUser.getDateOfBirth();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                dateOfBirth.setText(dateFormat.format(date));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            lastName.setText(currentUser.getLastName());
            firstName.setText(currentUser.getFirstName());
            email.setText(currentUser.getEmailAddress());


            // print photo from base
            if(currentUser.getAvatar() != null)
                if(currentUser.getAvatar().getImage() != null)
                    userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(currentUser.getAvatar().getImage(), 0, currentUser.getAvatar().getImage().length));
        } catch (Exception ex) {
            Alert.ShowError(this, "Erreur lors de l'affichage des données", "" + ex);
        }
    }

    final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Permet d'ouvrir l'appareil photo
     * @param view
     */
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception ex) {
            Alert.ShowDialog(this,"Erreur", ""+ex);
        }
    }

    /**
     * Permet de récupérer une photo prise
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPhoto.setImageBitmap(imageBitmap);

            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Photo p = new Photo();
                p.setImage(byteArray);
                currentUser.setAvatar(p);

            } catch (Exception ex) {
                Alert.ShowDialog(this,"Erreur", "" + ex);
            }

        }
    }


    /**
     * Bouton retour click
     * @param view
     */
    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    /**
     * Bouton sauvegarder les modifs
     * @param view
     */
    public void save(View view) {
        ShowSaveDialog("Confirmation", "Etes-vous sur de vouloir sauvegarder ?");
    }

    /**
     * bouton supprimer compte click
     * @param view
     */
    public void deleteAccount(View view) {
        ShowSupprimDialog(Session.getCurrentUser());
    }

    /**
     * Vérifie le format des données du formulaire et renvoi un user avec des données validées
     * @return
     * @throws Exception
     */
    private Reader checkInfoReaderBeforeSave() throws Exception {

        if (currentUser == null)
            throw new Exception("L'utilisateur courant est null");

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

    /**
     * Affiche la fenetre de sauvegarde
     * @param title
     * @param msg
     */
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
                                Alert.ShowDialog(currentContext, "Information", "Vous devez renseigner votre mot de passe courant pour mettre un nouveau mot de passe");
                                return;
                            } else if (!StringUtil.IsNullOrEmpty(curentpwdStr) && !StringUtil.IsNullOrEmpty(newPasswdStr)) {
                                if (!Cryptography.Hash(curentpwdStr).equals(currentUser.getPassword())) {
                                    Alert.ShowDialog(currentContext, "Information", "Le mot de passe courant n'est pas bon");
                                    return;
                                } else {
                                    //le mot de passe courant est le bon
                                    newPasswdStr = Cryptography.Hash(newPasswdStr);
                                    currentUser.setPassword(newPasswdStr);
                                }
                            }
                            Photo photoAlreadyInBase = DataBaseSingleton.GetDataBaseSingleton(currentContext).getPhotoFromUser(currentUser.getId());

                            DataBaseSingleton.GetDataBaseSingleton(currentContext).updateReaderInfo(checkInfoReaderBeforeSave());
                            //il n'y a pas de photo donc on insère la photo
                            try{
                                if(photoAlreadyInBase == null && currentUser.getAvatar() != null){
                                    Photo newPhoto = new Photo();
                                    newPhoto.setImage(currentUser.getAvatar().getImage());
                                    DataBaseSingleton.GetDataBaseSingleton(currentContext).insertObjectInDB(newPhoto, Photo.class);
                                    DataBaseSingleton.GetDataBaseSingleton(currentContext).updateReaderPhoto(newPhoto.getId() ,currentUser.getId());
                                }
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }
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

    /**
     * Affiche la fenetre de suppression du compte
     * @param r
     */
    private void ShowSupprimDialog(Reader r) {
        Context currentContext = this;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Confirmation ");
        alertDialog.setMessage("Confirmez-vous la suppression de votre compte ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DataBaseSingleton.GetDataBaseSingleton(currentContext).deleteObjectFromDB(r, Reader.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        ChangeActivity.ChangeActivity(currentContext, LoginActivity.class);
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