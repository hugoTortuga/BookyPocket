package android.cnam.bookypocket.DBManager;

import android.cnam.bookypocket.Utils.Alert;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.cnam.bookypocket.Model.*;

public class ORMSQLiteManager extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "bookypocket.db";
    private static final int DB_VERSION = 6;

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
            TableUtils.createTableIfNotExists(connectionSource, ReaderFriend.class);
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
            TableUtils.dropTable(connectionSource, ReaderFriend.class,true);


            onCreate(database, connectionSource);
        } catch (Exception ex) {
            Log.e("DATABASE", "Erreur lors de la mise à jour de la base" + ex.getMessage());
        }
        Log.i("DATABASE", "Upgrade from " + oldVersion + " to " + newVersion);
    }

    /**
     * Permet d'insérer dans la bd un objet qui appartient à la couche modèle (Personne, Photo...)
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

    public Category getCategoryByName(String nomCategory) throws SQLException {
        Category category = null;
        Dao<Category, Integer> dao = getDao(Category.class);
        category = (Category) dao.queryBuilder().where().eq("name", nomCategory).queryForFirst();
        return category;
    }

    public Reader getUserByCreditential(String emailStr, String pwdStr) throws SQLException {
        Reader rdr = null;
        Dao<Reader, Integer> dao = getDao(Reader.class);

        //TODO hasher le pwd

        rdr = (Reader) dao.queryBuilder().where().eq("emailAddress", emailStr).and().eq("password",pwdStr).queryForFirst();
        return rdr;
    }

    public boolean updateReaderInfo(Reader r){
        try{
            Dao<Reader, Integer> dao = getDao(Reader.class);
            int nbRow = dao.update(r);
            if(nbRow == 0)
                return false;
            else if(nbRow > 1)
                throw new Exception("More than one rows was update during update account process !");
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }



    public List<Author> getAuthorsFromBook(int book_id) throws SQLException {
        List<AuthorBook> authorBooks = new ArrayList<>();
        Dao<AuthorBook, Integer> dao = getDao(AuthorBook.class);
        authorBooks = (List<AuthorBook>) dao.queryBuilder().where().in("book_id", book_id).query();

        List<Author> authors = new ArrayList<>();
        for (AuthorBook rb: authorBooks) {
            authors.add(rb.getAuthor());
        }
        return authors;
    }


    public boolean doesBookExistInDB(Book book){

        // TODO
        return true;
    }

    public Author getAuthorFromName(String artistName) throws SQLException {
        Author author = null;
        Dao<Author, Integer> dao = getDao(Author.class);
        author = (Author) dao.queryBuilder().where().eq("artistName", artistName).queryForFirst();
        return author;
    }

    public int getIdAuthorByArtistName(String artistName) throws SQLException {
        Dao<Author, Integer> dao = getDao(Author.class);
        Author a = (Author) dao.queryBuilder().where().eq("artistName", artistName).queryForFirst();
        return a.getId();
    }

    public Book getBookByISBN(String ISBN) throws SQLException {
        Dao<Book, Integer> dao = getDao(Book.class);
        return (Book) dao.queryBuilder().where().eq("ISBN", ISBN).queryForFirst();
    }

    public List<Book> getBooksByKeyWord(String[] keyword) throws SQLException {
        List<Book> booksFound = new ArrayList<>();

        Dao<Book, Integer> dao = getDao(Book.class);
        booksFound = (List<Book>) dao.queryBuilder().where().in("ISBN", keyword)
                .or().eq("title",keyword)
                .or().eq("backCover",keyword).query();

        return booksFound;
    }

    public List<Book> getListBookFromIdUser(int reader_id) throws SQLException {
        List<ReaderBook> booksReaderFound = new ArrayList<>();
        Dao<ReaderBook, Integer> dao = getDao(ReaderBook.class);
        booksReaderFound = (List<ReaderBook>) dao.queryBuilder().where().in("reader_id", reader_id).query();

        List<Book> booksFound = new ArrayList<>();
        for (ReaderBook rb: booksReaderFound) {
            booksFound.add(rb.getBook());
        }
        return booksFound;
    }

    public List<Book> getBooksByAuthorArtistName(String authorParam) throws SQLException {
        List<AuthorBook> autBook = new ArrayList<>();
        Dao<AuthorBook, Integer> dao = getDao(AuthorBook.class);
        autBook = (List<AuthorBook>) dao.queryBuilder().where().in("author_id", getIdAuthorByArtistName(authorParam)).query();

        List<Book> booksFound = new ArrayList<>();
        for (AuthorBook rb: autBook) {
            booksFound.add(rb.getBook());
        }
        return booksFound;
    }

    public List<Reader> GetFriendsByReader(int idPerson) throws SQLException {
        List<Reader> friends = new ArrayList<>();
        List<ReaderFriend> rbs = new ArrayList<>();
        Dao<ReaderFriend, Integer> dao = getDao(ReaderFriend.class);
        rbs =(List<ReaderFriend>) dao.queryBuilder().where().eq("reader_id",idPerson).query();
        for (ReaderFriend rbook : rbs){
            friends.add(rbook.getFriend());
        }
        return friends;
    }

    public List<Reader> getUserByKeyword(String keyword) throws SQLException {
        Dao<Reader, Integer> dao = getDao(Reader.class);
        return (List<Reader>) dao.queryBuilder().where().like("firstName",keyword)
                .or().like("lastName",keyword).and().notIn("id",Session.getCurrentUser().getId()).query();
    }

    public void DeleteAccount(Reader rdr) throws SQLException {
        Dao<Reader, Integer> dao = getDao(Reader.class);
        dao.delete(rdr);
    }

}
