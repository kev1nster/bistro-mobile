package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Waiter extends RealmObject {

    private Integer Id;
    private String Name;
    private String Password;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
