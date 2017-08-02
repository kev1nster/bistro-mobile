package thundrware.com.bistromobile.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Category extends RealmObject {

    private Integer id;
    private String name;
    private RealmList<Group> Groups;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
