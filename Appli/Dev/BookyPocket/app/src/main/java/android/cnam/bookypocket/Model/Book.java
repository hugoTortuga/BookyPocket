package android.cnam.bookypocket.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Book")
public class Book implements Parcelable {

    @DatabaseField(unique = true, id=true)
    private String ISBN;
    @DatabaseField
    private String title;
    @DatabaseField
    private String backCover;
    @DatabaseField
    private int yearPublication;
    @DatabaseField
    private int yearEdition;
    @DatabaseField
    private int nbPages;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "id", foreignAutoCreate = true)
    private Genre genre;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "id", foreignAutoCreate = true)
    public Category category;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackCover() {
        return backCover;
    }

    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }

    public int getYearPublication() {
        return yearPublication;
    }

    public void setYearPublication(int yearPublication) {
        this.yearPublication = yearPublication;
    }

    public int getYearEdition() {
        return yearEdition;
    }

    public void setYearEdition(int yearEdition) {
        this.yearEdition = yearEdition;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Book() {
    }

    public Book(String ISBN, String title, String backCover, int yearPublication, int yearEdition, int nbPages, Genre genre, Category category) {
        this.ISBN = ISBN;
        this.title = title;
        this.backCover = backCover;
        this.yearPublication = yearPublication;
        this.yearEdition = yearEdition;
        this.nbPages = nbPages;
        this.genre = genre;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", backCover='" + backCover + '\'' +
                ", yearPublication=" + yearPublication +
                ", yearEdition=" + yearEdition +
                ", nbPages=" + nbPages +
                ", genre=" + genre +
                ", category=" + category +
                '}';
    }

    // Cette partie sert à rendre parcelable la classe book

    protected Book(Parcel in) {
        ISBN = in.readString();
        title = in.readString();
        backCover = in.readString();
        yearPublication = in.readInt();
        yearEdition = in.readInt();
        nbPages = in.readInt();
    }

    public static final Creator<Book> BOOK = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ISBN);
        dest.writeString(title);
        dest.writeString(backCover);
        dest.writeInt(yearPublication);
        dest.writeInt(yearEdition);
        dest.writeInt(nbPages);
    }
}
