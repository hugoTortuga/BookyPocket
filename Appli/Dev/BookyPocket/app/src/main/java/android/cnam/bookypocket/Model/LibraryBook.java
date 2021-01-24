package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe permettant de faire la jointure entre une librairie et un livre
 * un livre peut être présent dans plusieurs bibliothèques
 * et une bibliothèque a plusieurs livres
 */
@DatabaseTable(tableName = "LibraryBook")
public class LibraryBook {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", uniqueCombo = true)
    private Library library;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "ISBN", uniqueCombo = true)
    private Book book;

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryBook() {
    }

    public LibraryBook(Library library, Book book) {
        this.library = library;
        this.book = book;
    }

    @Override
    public String toString() {
        return "LibraryBook{" +
                "library=" + library +
                ", book=" + book +
                '}';
    }
}
