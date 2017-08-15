package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Area extends RealmObject {

    private int Id;
    private String Name;
    private int NumberOfTables;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getNumberOfTables() {
        return NumberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.NumberOfTables = numberOfTables;
    }
}
