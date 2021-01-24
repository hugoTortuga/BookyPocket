package android.cnam.bookypocket.Model;

import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Author")
public class Author extends Person {

    public Author() {
    }

    public Author(String lastName, String firstName, Date dateOfBirth, Photo avatar) {
        super(lastName, firstName, dateOfBirth, avatar);
    }
}
