package android.cnam.bookypocket;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    //User inputs for a book
    private EditText email;
    private EditText password;

    private Button login;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button connexion = (Button) findViewById(R.id.login_connexion);
        email = (EditText) findViewById(R.id.login_adrMail);
        password = (EditText) findViewById(R.id.login_password);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection();
            }
        });
    }

    public void connection(){
        //Test des champs
        if(email == null || password == null)
        {
            //get string from values/string, possible ?
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir les champs");
            return;
        }

        String emailStr = email.getText().toString();
        String pwdStr = password.getText().toString();

        //On test les valeurs d'entrée
        if(emailStr == null || emailStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir l'adresse email");
            return;
        }
        if(pwdStr == null || pwdStr.trim().isEmpty()){
            Alert.ShowDialog(this, "Paramètre non renseigné", "Veuillez remplir le mot de passe");
            return;
        }

        ORMSQLiteManager db = new ORMSQLiteManager(this);
        try {
            //On hash le password
            //TODO appel à l'API

            Reader reader = db.getUserByCreditential(emailStr, pwdStr);

            if(reader == null)
                Alert.ShowDialog(this, "Identifiants", "Identifiants non reconnu");
            else{
                Session.setCurrentUser(reader);

                //On redirige à l'accueil
                ChangeActivity.ChangeActivity(this, MainActivity.class);
            }

        } catch (Exception ex) {
                Alert.ShowDialog(this, "Erreur", "Erreur lors de la connexion.\nVeuillez réessayer plus tard");
        }
        finally {
            db.close();
        }
    }

    /**
     * Go to sign up activity
     * @param view
     */
    public void goTosignUp(View view)
    {
        ChangeActivity.ChangeActivity(this, SignUpActivity.class);
    }


}
