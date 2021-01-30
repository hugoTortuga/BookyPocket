package android.cnam.bookypocket.DBManager;

import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;

import java.util.List;

public class Session {

    private static List<Book> Books;

    public static List<Book> getBooks() {
        return Books;
    }

    public static void setBooks(List<Book> books) {
        Books = books;
    }

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
