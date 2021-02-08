package android.cnam.bookypocket.DBManager;

import android.content.Context;

/**
 * Singleton de connexion
 */
public class DataBaseSingleton {

    private DataBaseSingleton() {
    }

    private static ORMSQLiteManager Singleton;

    /**
     * méthode unique de récupération de l'objet de connexion
     * @param context
     * @return
     */
    public static ORMSQLiteManager GetDataBaseSingleton(Context context) {

        if (Singleton == null) {
            return new ORMSQLiteManager(context);
        } else if (!Singleton.isOpen()) {
            Singleton.close();
            return new ORMSQLiteManager(context);
        } else {
            Singleton = new ORMSQLiteManager(context);
            return Singleton;
        }


    }

}
