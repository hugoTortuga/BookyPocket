package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ReaderFriend")
public class ReaderFriend {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", uniqueCombo = true)
    private Reader reader;
    @DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "id", uniqueCombo = true)
    private Reader friend;

    public ReaderFriend(Reader reader, Reader friend) {
        this.reader = reader;
        this.friend = friend;
    }

    public ReaderFriend() {
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getFriend() {
        return friend;
    }

    public void setFriend(Reader friend) {
        this.friend = friend;
    }
}
