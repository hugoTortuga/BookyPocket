package android.cnam.bookypocket;

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
    private String email;
    private String password;
    private String dateOfBirth;

    private Button login;

    private TextView signUp;

    //https://stackoverflow.com/questions/39189827/launch-login-activity-instead-of-mainactivity-if-app-is-on-its-first-run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Button connexion = (Button) findViewById(R.id.login_connexion);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome(view);
            }
        });
    }

    /**
     * Go to home activity
     * @param view
     */
    public void goToHome(View view)
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
