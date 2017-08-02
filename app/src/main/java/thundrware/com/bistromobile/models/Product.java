package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class Product extends RealmObject {

    private Integer Id;
    private String Name;
    private double Price;
    private Integer GroupId;

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

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public Integer getGroupId() {
        return GroupId;
    }

    public void setGroupId(Integer groupId) {
        GroupId = groupId;
    }



}
