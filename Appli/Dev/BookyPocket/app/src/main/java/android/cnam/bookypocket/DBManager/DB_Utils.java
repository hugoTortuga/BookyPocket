package android.cnam.bookypocket.DBManager;

import android.cnam.bookypocket.Model.*;
import android.util.Log;

import java.util.Date;

public class DB_Utils {

    private static ORMSQLiteManager DBManager;

    public static void fillDataBaseWithTests() {
        try{
            //On insère des catégories
            DBManager.insertObjectInDB(new Category("2-4ans", false), Category.class);
            DBManager.insertObjectInDB(new Category("5-8ans", false), Category.class);
            DBManager.insertObjectInDB(new Category("9-12ans", false), Category.class);
            DBManager.insertObjectInDB(new Category("18+ans", true), Category.class);

            //On insère des genres
            DBManager.insertObjectInDB(new Genre("Romantique"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Tragédie"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Comédie"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Tragicomédie"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Farce"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Essai"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Roman"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Biographie"), Genre.class);
            DBManager.insertObjectInDB(new Genre("Autobiographie"), Genre.class);
            Genre roman_policier = new Genre("Roman policier");

            //On insère 3 auteurs
            Category category = new Category("12-16ans", false);
            Author author = new Author("Agatha", "Christie", new Date(1890, 9, 15), null);
            Book book = new Book("9782010009273", "Le crime de l'Orient-Express",
                    "Le train est aussi dangereux que le paquebot \", dit Hercule Poirot... Le lendemain, dans un wagon de l'Orient-Express bloqué par les neiges yougoslaves, " +
                            "on découvre le cadavre d'un Américain lardé de douze coups de couteau. L'assassin n'a pu venir de l'extérieur : voici donc un huis dos, le plus fameux, " +
                            "peut-être, de toute la littérature policière, où, pour mener son enquête, le petit détective belge a le choix entre une princesse russe, une Américaine fantasque, " +
                            "le secrétaire de la victime, un couple de Hongrois distingués, l'inévitable colonel de retour des Indes, les domestiques de ce beau monde et le contrôleur du wagon. " +
                            "Un meurtre incompréhensible, à moins qu'on ne puisse établir que tous ces voyageurs sont moins étrangers les uns aux autres qu'ils ne veulent bien le prétendre...",
                    1934, 2014, 416, roman_policier, category);

            DBManager.insertObjectInDB(author, Author.class);
            DBManager.insertObjectInDB(book, Book.class);

            AuthorBook at = new AuthorBook(author, book);
            DBManager.insertObjectInDB(at, AuthorBook.class);

        } catch (Exception ex) {
            Log.e("DATABASE", "Erreur lors des insertions de test : " + ex.getMessage());
        }
    }

}
