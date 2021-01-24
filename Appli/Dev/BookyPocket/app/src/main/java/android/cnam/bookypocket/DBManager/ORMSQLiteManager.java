package android.cnam.bookypocket.DBManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import android.cnam.bookypocket.Model.*;

public class ORMSQLiteManager extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "bookypocket.db";
    private static final int DB_VERSION = 1;

    public ORMSQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Appelé lors du premier lancement de l'appli
     * Rq : les paramètres sont renseignés par l'api d'ormlite
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Genre.class);
            TableUtils.createTableIfNotExists(connectionSource, Photo.class);
            TableUtils.createTableIfNotExists(connectionSource, Book.class);
            TableUtils.createTableIfNotExists(connectionSource, Person.class);
            TableUtils.createTableIfNotExists(connectionSource, Library.class);
            TableUtils.createTableIfNotExists(connectionSource, Reader.class);
            TableUtils.createTableIfNotExists(connectionSource, Author.class);
            TableUtils.createTableIfNotExists(connectionSource, Language.class);
            TableUtils.createTableIfNotExists(connectionSource, AuthorBook.class);
            TableUtils.createTableIfNotExists(connectionSource, LibraryBook.class);
            TableUtils.createTableIfNotExists(connectionSource, ReaderBook.class);
            TableUtils.createTableIfNotExists(connectionSource, PhotoBook.class);
            Log.i("DATABASE", "Création de la base de données réussie");
        } catch (Exception ex) {
            Log.e("DATABASE", "Erreur lors de la création de la base" + ex.getMessage());
        }
    }

    /**
     * Appelé lorsque le numéro de version trouvé est plus petit que le DB_VERSION ci-dessus
     * Rq : les paramètres sont renseignés par l'api d'ormlite
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //Un peu extrême à changer plus tard
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, Genre.class, true);
            TableUtils.dropTable(connectionSource, Photo.class, true);
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Person.class, true);
            TableUtils.dropTable(connectionSource, Library.class, true);
            TableUtils.dropTable(connectionSource, Reader.class, true);
            TableUtils.dropTable(connectionSource, Author.class, true);
            TableUtils.dropTable(connectionSource, Language.class, true);
            TableUtils.dropTable(connectionSource, AuthorBook.class, true);
            TableUtils.dropTable(connectionSource, LibraryBook.class, true);
            TableUtils.dropTable(connectionSource, ReaderBook.class, true);
            TableUtils.dropTable(connectionSource, PhotoBook.class, true);

            onCreate(database, connectionSource);
        } catch (Exception ex) {
            Log.e("DATABASE", "Erreur lors de la mise à jour de la base" + ex.getMessage());
        }
        Log.i("DATABASE", "Upgrade from " + oldVersion + " to " + newVersion);
    }

    /**
     * Permet d'insérer dans la bd un objet qui appartient à la couche modèle (Genre, Personne, Photo...)
     *
     * @param object l'object à insérer
     * @param classe la classe de l'objet exemple : Library.class
     */
    public <typeObject> void insertObjectInDB(Object object, Class classe) throws SQLException {
        Dao<typeObject, Integer> dao = getDao(classe);
        dao.create((typeObject) object);
        Log.i("DATABASE", "Object " + object + " créé en base");
    }

    /**
     * Requete tous les objets d'un type donné exemple : tous les livres
     *
     * @param classe
     * @param <typeObject>
     * @return
     */
    public <typeObject> List<typeObject> getAllObjects(Class classe) {
        List<typeObject> objects = null;
        try {
            Dao<typeObject, Integer> dao = getDao(classe);
            objects = dao.queryForAll();
            if (objects != null)
                Log.i("DATABASE", classe.getName() + " requêté(e): " + objects.size() + " objets.");
            else
                Log.i("DATABASE", classe.getName() + "Objects requêtées : aucun objet");
        } catch (SQLException ex) {
            Log.e("DATABASE", "Erreur lors de la récupération des bokks");
        }
        return objects;
    }

    /**
     * Supprime un objet de la base
     *
     * @param object objet à supprimer
     * @param classe classe de l'objet à supprimer
     */
    public <typeObject> boolean deleteObjectFromDB(Object object, Class classe) {
        try {
            Dao<typeObject, Integer> dao = getDao(classe);
            dao.delete((typeObject) object);
            Log.i("DATABASE", "Suppression de " + object);
            return true;
        } catch (SQLException ex) {
            Log.e("DATABASE", "Erreur lors de la suppression de " + object);
            return false;
        }
    }

    public Genre getGenreByName(String nomGenre) throws SQLException {
        Genre genre = null;
        Dao<Genre, Integer> dao = getDao(Genre.class);
        genre = (Genre) dao.queryBuilder().where().eq("name", nomGenre).queryForFirst();
        return genre;
    }

    public Reader getUserByCreditential(String emailStr, String pwdStr) throws SQLException {
        Reader rdr = null;
        Dao<Reader, Integer> dao = getDao(Reader.class);

        //TODO hasher le pwd

        rdr = (Reader) dao.queryBuilder().where().eq("emailAddress", emailStr).and().eq("password",pwdStr).queryForFirst();
        return rdr;
    }
}
