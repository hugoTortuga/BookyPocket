package android.cnam.bookypocket.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {

    /**
     * Affiche un message avec un contexte, un titre, un message
     * @param ctx
     * @param title
     * @param msg
     */
    public static void ShowDialog(Context ctx, String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * Affiche un message d'erreur avec un contexte, un titre, un message
     * @param ctx
     * @param title
     * @param msg
     */
    public static void ShowError(Context ctx, String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage("Erreur" + msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



}
