package android.cnam.bookypocket.Utils;

import android.content.Context;
import android.content.Intent;

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
