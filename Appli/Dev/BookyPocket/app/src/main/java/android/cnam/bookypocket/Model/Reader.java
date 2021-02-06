package android.cnam.bookypocket.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "Reader")
public class Reader extends Person implements Parcelable {

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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }


    protected Reader(Parcel in) {
        super(in);
    }

    public static final Creator<Reader> CREATOR = new Creator<Reader>() {
        @Override
        public Reader createFromParcel(Parcel in) {
            return new Reader(in);
        }

        @Override
        public Reader[] newArray(int size) {
            return new Reader[size];
        }
    };

}
