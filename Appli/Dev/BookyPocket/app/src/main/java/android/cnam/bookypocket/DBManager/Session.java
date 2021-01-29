package android.cnam.bookypocket.DBManager;

import android.cnam.bookypocket.Model.Reader;

public class Session {



    private static Reader CurrentUser;

    public static Reader getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(Reader currentUser) {
        CurrentUser = currentUser;
    }

    public static boolean isUserConnectedToInternet() {
        //TODO
        return true;
    }
}
