package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe servant de jointure entre un livre et un auteur
 * Rq : un livre peut avoir plusieurs auteurs
 * et un auteur peut écrire plusieurs livres
 */
@DatabaseTable(tableName = "AuthorBook")
public class AuthorBook {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true, uniqueCombo=true)
    private Author author;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "ISBN", foreignAutoCreate = true, uniqueCombo=true)
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public AuthorBook() {
    }

    public AuthorBook(Author author, Book book) {
        this.author = author;
        this.book = book;
    }

    @Override
    public String toString() {
        return "AuthorBook{" +
                "author=" + author +
                ", book=" + book +
                '}';
    }
}
