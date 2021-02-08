package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe de jointure entre un reader et un livre
 * un livre peut avoire plrs reader
 * et un reader peut lire plusieurs livres
 */
@DatabaseTable(tableName = "ReaderBook")
public class ReaderBook {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", uniqueCombo = true)
    private Reader reader;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "ISBN", uniqueCombo = true)
    private Book book;

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ReaderBook() {
    }

    public ReaderBook(Reader reader, Book book) {
        this.reader = reader;
        this.book = book;
    }

    @Override
    public String toString() {
        return "ReaderBook{" +
                "reader=" + reader +
                ", book=" + book +
                '}';
    }
}
