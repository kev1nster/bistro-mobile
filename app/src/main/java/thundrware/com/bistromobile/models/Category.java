package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Category extends RealmObject {

    private Integer Id;
    private String Name;
    private String GroupsList;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getGroupsList() {
        return GroupsList;
    }

    public void setGroupsList(String groupsList) {
        this.GroupsList = groupsList;
    }


}
