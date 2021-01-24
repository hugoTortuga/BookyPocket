package android.cnam.bookypocket.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Category")
public class Category {

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
}
