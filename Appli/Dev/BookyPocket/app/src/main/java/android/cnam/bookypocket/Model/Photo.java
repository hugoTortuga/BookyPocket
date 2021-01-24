package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;

@DatabaseTable(tableName = "Photo")
public class Photo {

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

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
