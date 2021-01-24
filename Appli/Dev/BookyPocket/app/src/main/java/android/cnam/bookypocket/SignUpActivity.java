package android.cnam.bookypocket;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity

    {

        //User inputs for a book
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private String dateOfBirth;

        private Button signup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

            Button inscriptionButton = (Button) findViewById(R.id.signup_button);
            inscriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("SIGN UP", "Inscription demandée");
                    try{
                        Log.i("SIGN UP", "Inscription terminée");

                        goToHome(view);
                    }
                    catch (Exception ex){
                        Log.i("SIGN UP", "Erreur " + ex);
                    }
                }
            });
    }

        /**
         * Go to home activity
         * @param view
         */
        public void goToHome(View view)
        {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            SignUpActivity.this.finish();
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

        /**
         * Sign up
         */
        public void SignUp(){
        }
    }
