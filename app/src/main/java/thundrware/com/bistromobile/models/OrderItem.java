package thundrware.com.bistromobile.models;

import io.realm.RealmObject;

public class OrderItem extends RealmObject {

    private int Id;
    private Product product;
    private int ProductId;
    private int DishNumber = 0;
    private int CategoryId;
    private double Quantity = 0;
    private String Message;
    private boolean IsNew;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void increaseQuantity() {
        Quantity++;
    }

    public void decreaseQuantity() {
        Quantity--;
    }

    public boolean isNew() {
        return IsNew;
    }

    public void setQuantity(double itemQuantity) {
        Quantity = itemQuantity;
    }

    public double getQuantity() {
        return Quantity;
    }

    public int getDishNumber() {
        return DishNumber;
    }

    public void setDishNumber(int dishNumber) {
        this.DishNumber = dishNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.CategoryId = categoryId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setNew(boolean aNew) {
        IsNew = aNew;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }
}
