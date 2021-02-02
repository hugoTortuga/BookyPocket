package android.cnam.bookypocket.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static androidx.core.content.ContextCompat.getSystemService;

public class Network {

    public static boolean isNetworkAvailable(Context context) {
        try{
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception ex){
            return false;
        }
    }

}
