package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Group extends RealmObject {

    private int Id;
    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
