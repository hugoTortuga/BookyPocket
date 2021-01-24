package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Person")
public class Person {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String lastName;
    @DatabaseField
    private String firstName;
    @DatabaseField
    private Date dateOfBirth;
    @DatabaseField(canBeNull = true, foreign = true, foreignColumnName = "id", foreignAutoCreate = true)
    private Photo avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Photo getAvatar() {
        return avatar;
    }

    public void setAvatar(Photo avatar) {
        this.avatar = avatar;
    }

    public Person() {
    }

    public Person(String lastName, String firstName, Date dateOfBirth, Photo avatar) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", avatar=" + avatar +
                '}';
    }
}
