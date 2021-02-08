package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe de jointure entre une photo et un livre
 * un livre peut avoir plusieurs photos
 * une photo est soit relative à une personne soit à un livre d'ou le fait d'avoir une classe de jointure
 */
@DatabaseTable(tableName = "PhotoBook")
public class PhotoBook {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", uniqueCombo = true)
    private Photo photo;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "ISBN", uniqueCombo = true)
    private Book book;


    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public PhotoBook() {
    }

    public PhotoBook(Photo photo, Book book) {
        this.photo = photo;
        this.book = book;
    }

    @Override
    public String toString() {
        return "PhotoBook{" +
                "photo=" + photo +
                ", book=" + book +
                '}';
    }
}
