package thundrware.com.bistromobile.models;

public class Waiter {

    private Integer Id;
    private String Name;
    private String Password;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

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

    public boolean hasEmptyOrNullElements() {
        return (Id == null || Name == null || Password == null);
    }
}
