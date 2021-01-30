package android.cnam.bookypocket.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Category")
public class Category implements Parcelable {

    //RQ : attribut columnName référence le nom de la colonne en base
    //pas obligatoire si les noms sont identiques

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private boolean ageLimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(boolean ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Category() {
    }

    public Category(String name, boolean ageLimit) {
        this.name = name;
        this.ageLimit = ageLimit;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ageLimit=" + ageLimit +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (ageLimit ? 1 : 0));
    }

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ageLimit = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

}
