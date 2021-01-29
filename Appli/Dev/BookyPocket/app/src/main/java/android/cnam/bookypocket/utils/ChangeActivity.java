package android.cnam.bookypocket.utils;

import android.app.Activity;
import android.cnam.bookypocket.LoginActivity;
import android.cnam.bookypocket.MainActivity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeActivity {

    public static void ChangeActivity(Context currentContext, Class wantedContext){
        try{
            Intent intent = new Intent(currentContext, wantedContext);
            currentContext.startActivity(intent);
        }
        catch (Exception ex){
            Alert.ShowDialog(currentContext, "Erreur lors du changement de page", "" + ex);
        }

    }

}
