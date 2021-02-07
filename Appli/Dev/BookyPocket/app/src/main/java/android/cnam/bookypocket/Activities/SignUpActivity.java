package android.cnam.bookypocket.Activities;

import android.app.Activity;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.Utils.CheckForm;
import android.cnam.bookypocket.Utils.Cryptography;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activité pour s'inscrire
 */
public class SignUpActivity extends AppCompatActivity {

    //Attributs
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirmPassword;
    private EditText dateOfBirth;
    final Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeView();
    }

    /**
     * Initialise les composants de la vue
     */
    private void initializeView(){
        Button inscriptionButton = (Button) findViewById(R.id.signup_button);
        inscriptionClick(inscriptionButton, this);
        email = (EditText) findViewById(R.id.signup_email);
        firstName = (EditText) findViewById(R.id.signup_firstname);
        lastName = (EditText) findViewById(R.id.signup_lastname);
        password = (EditText) findViewById(R.id.signup_password);
        confirmPassword = (EditText) findViewById(R.id.signup_confirmPassword);
        dateOfBirth = (EditText) findViewById(R.id.signup_DOB);
    }

    /**
     * Récupération du click du bouton inscription
     * @param btn
     * @param ctx
     */
    private void inscriptionClick(Button btn, Context ctx){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SIGNUPTAG", "Inscription demandée");

                Reader reader = checkValeur();
                if(reader == null)
                    return;
                inscription(reader);

            }
        });
    }

    /**
     * vérifie les valeurs du formulaire
     * @return
     */
    private Reader checkValeur() {
        //Test des champs
        String emailStr = email.getText().toString();
        String pwdStr = password.getText().toString();
        String pwdConfirmStr = confirmPassword.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String firstNameStr = firstName.getText().toString();
        String dob = dateOfBirth.getText().toString();

        //On test les valeurs d'entrée
        if(emailStr == null || emailStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir l'adresse email");
            return null;
        }
        if(pwdStr == null || pwdStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir le mot de passe");
            return null;
        }
        if(lastNameStr == null || lastNameStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir le nom de famille");
            return null;
        }
        if(firstNameStr == null || firstNameStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir le prénom");
            return null;
        }
        if(pwdConfirmStr == null || pwdConfirmStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir la confirmation de mot de passe");
            return null;
        }
        if(!pwdConfirmStr.equals(pwdStr))
        {
            Alert.ShowDialog(this, "Paramètres invalides", "Les mots de passes renseignés ne concordent pas");
            return null;
        }
        if(pwdStr.length() < 8)
        {
            Alert.ShowDialog(this, "Paramètres invalides", "Le mot de passe doit faire au moins 8 caractères");
            return null;
        }
        pwdStr = Cryptography.Hash(pwdStr);

        if(StringUtil.IsNullOrEmpty(dob)){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir votre date de naissance au format AAAA/MM/JJ");
            return null;
        }
        Date dateBirth = null;
        try{
            dateBirth = CheckForm.CheckAndCastStringToDate(dob,this);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        String regexEmail = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(emailStr);
        if(!matcher.find()){
            Alert.ShowDialog(this, "Paramètre invalide", "L'adresse email n'a pas un format valide");
            return null;
        }

        //On hash le mot de passe
        //pwdStr = DigestUtils.sha256Hex(pwdStr);

        Reader reader = new Reader(emailStr.trim(), pwdStr.trim(), lastNameStr.trim(), firstNameStr.trim(), dateBirth, null);
        return reader;
    }

    /**
     * Ajoute un user en base
     * @param reader
     */
    private void inscription(Reader reader){
        try {

            //On hash le password
            //TODO appel à l'API

            //On insère notre reader
            DataBaseSingleton.GetDataBaseSingleton(this).insertObjectInDB(reader, Reader.class);

            Alert.ShowDialog(this, "Succès", "Inscription terminée");

            //On retourne à l'écran de connexion
            ChangeActivity.ChangeActivity(context, LoginActivity.class);

        } catch (Exception ex) {
            if(ex.getClass() == java.sql.SQLException.class)
                Alert.ShowDialog(this, "Erreur", "L'adresse email est déjà utilisé");
            else
                Alert.ShowDialog(this, "Erreur", ex.getMessage());

        }
    }

}
