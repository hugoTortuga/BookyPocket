package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Reader")
public class Reader extends Person{

    @DatabaseField(columnName = "emailAddress", canBeNull = false, unique = true)
    private String emailAddress;

    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Reader() {
    }

    public Reader(String emailAddress, String password, String lastName, String firstName, Date dateOfBirth, Photo avatar) {
        super(lastName, firstName, dateOfBirth, avatar);
        this.emailAddress = emailAddress;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Reader{ " +

                super.toString() +
                "\n emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
