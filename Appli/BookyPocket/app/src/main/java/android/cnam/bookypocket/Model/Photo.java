package android.cnam.bookypocket.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;

@DatabaseTable(tableName = "Photo")
public class Photo implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Photo() {
    }

    public Photo(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", image=" + Arrays.toString(image) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
    }


    protected Photo(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
