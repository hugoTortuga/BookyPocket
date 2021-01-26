package android.cnam.bookypocket;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.utils.Alert;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
        setContentView(R.layout.activity_login2);

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
                Alert.ShowDialog(this, "Identifiants", "Bienvenue " + reader.getFirstName());
                Session.setCurrentUser(reader);
                goToHome();
            }

        } catch (Exception ex) {
                Alert.ShowDialog(this, "Erreur", "Erreur lors de la connexion.\nVeuillez réessayer plus tard");
        }
        finally {
            db.close();
        }
    }

    /**
     * Go to home activity
     */
    public void goToHome()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /**
     * Go to sign up activity
     * @param view
     */
    public void goTosignUp(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }


}
