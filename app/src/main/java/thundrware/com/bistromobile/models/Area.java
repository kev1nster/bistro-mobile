package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Area extends RealmObject {

    private int id;
    private String name;
    private int numberOfTables;

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

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }
}
