package android.cnam.bookypocket;

import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.utils.Alert;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.cnam.bookypocket.DBManager.*;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText dateOfBirth;

    private Button signup;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.i("SIGNUPTAG", "Lancement de l'activity");
        Button inscriptionButton = (Button) findViewById(R.id.signup_button);
        inscriptionClick(inscriptionButton, this);

        email = (EditText) findViewById(R.id.signup_email);
        firstName = (EditText) findViewById(R.id.signup_firstname);
        lastName = (EditText) findViewById(R.id.signup_lastname);
        password = (EditText) findViewById(R.id.signup_password);
        dateOfBirth = (EditText) findViewById(R.id.signup_DOB);
    }

    private void inscriptionClick(Button btn, Context ctx){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SIGNUPTAG", "Inscription demandée");

                //Test des champs
                String emailStr = email.getText().toString();
                String pwdStr = password.getText().toString();
                String lastNameStr = lastName.getText().toString();
                String firstNameStr = firstName.getText().toString();

                //On test les valeurs d'entrée
                if(emailStr == null || emailStr.trim().isEmpty()){
                    Alert.ShowDialog(ctx, "Paramètre non renseigné", "Veuillez remplir l'adresse email");
                    return;
                }
                if(pwdStr == null || pwdStr.trim().isEmpty()){
                    Alert.ShowDialog(ctx, "Paramètre non renseigné", "Veuillez remplir le mot de passe");
                    return;
                }
                if(lastNameStr == null || lastNameStr.trim().isEmpty()){
                    Alert.ShowDialog(ctx, "Paramètre non renseigné", "Veuillez remplir le nom de famille");
                    return;
                }
                if(firstNameStr == null || firstNameStr.trim().isEmpty()){
                    Alert.ShowDialog(ctx, "Paramètre non renseigné", "Veuillez remplir le prénom");
                    return;
                }

                try {

                    //On hash le password
                    //TODO appel à l'API

                    //On instancie un nouveau reader
                    Reader reader = new Reader(emailStr, pwdStr, lastNameStr, firstNameStr, null, null);

                    //On insère notre reader
                    ORMSQLiteManager db = new ORMSQLiteManager(ctx);
                    db.insertObjectInDB(reader, Reader.class);
                    db.close();

                    Alert.ShowDialog(ctx, "Succès", "Inscription terminée");

                    //On retourne à l'écran de connexion
                    goToHome();

                } catch (Exception ex) {
                    if(ex.getClass() == java.sql.SQLException.class)
                        Alert.ShowDialog(ctx, "Erreur", "L'adresse email est déjà utilisé");
                    else
                        Alert.ShowDialog(ctx, "Erreur", ex.getMessage());

                }
            }
        });
    }

    public void goToHome() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        SignUpActivity.this.finish();
    }

    /**
     * Go to sign up activity
     *
     * @param view
     */
    public void goTosignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * Sign up
     */
    public void SignUp() {
    }
}
