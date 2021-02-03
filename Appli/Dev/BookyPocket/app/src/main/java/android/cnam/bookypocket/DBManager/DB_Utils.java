package android.cnam.bookypocket.DBManager;

import android.cnam.bookypocket.Model.*;
import android.content.Context;
import android.util.Log;

import java.util.Date;

public class DB_Utils {


    public static void fillDataBaseWithTests(Context c) {
        try{
            //On insère des catégories
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(new Category("2-4ans", false), Category.class);
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(new Category("5-8ans", false), Category.class);
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(new Category("9-12ans", false), Category.class);
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(new Category("18+ans", true), Category.class);


            //On insère 3 auteurs
            Category category = new Category("12-16ans", false);
            Author author = new Author("Agatha", "Christie", new Date(1890, 9, 15), null);
            Book book = new Book("9782010009273", "Le crime de l'Orient-Express",
                    "Le train est aussi dangereux que le paquebot \", dit Hercule Poirot... Le lendemain, dans un wagon de l'Orient-Express bloqué par les neiges yougoslaves, " +
                            "on découvre le cadavre d'un Américain lardé de douze coups de couteau. L'assassin n'a pu venir de l'extérieur : voici donc un huis dos, le plus fameux, " +
                            "peut-être, de toute la littérature policière, où, pour mener son enquête, le petit détective belge a le choix entre une princesse russe, une Américaine fantasque, " +
                            "le secrétaire de la victime, un couple de Hongrois distingués, l'inévitable colonel de retour des Indes, les domestiques de ce beau monde et le contrôleur du wagon. " +
                            "Un meurtre incompréhensible, à moins qu'on ne puisse établir que tous ces voyageurs sont moins étrangers les uns aux autres qu'ils ne veulent bien le prétendre...",
                    1934, 2014, "previewLink", 416, category);

            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(author, Author.class);
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(book, Book.class);

            AuthorBook at = new AuthorBook(author, book);
            DataBaseSingleton.GetDataBaseSingleton(c).insertObjectInDB(at, AuthorBook.class);

        } catch (Exception ex) {
            Log.e("DATABASE", "Erreur lors des insertions de test : " + ex.getMessage());
        }
    }

}
