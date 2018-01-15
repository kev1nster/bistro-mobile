package thundrware.com.bistromobile;

import io.realm.Realm;
import thundrware.com.bistromobile.exceptions.NegativeQuantityException;
import thundrware.com.bistromobile.models.OrderItem;

public class OrderItemEditor {

    private OrderItem orderItem;
    private Realm realm;

    protected OrderItemEditor(OrderItem orderItem) {
        realm = Realm.getDefaultInstance();
        this.orderItem = orderItem;
    }

    // The static call
    public static OrderItemEditor edit(OrderItem orderItem) {
        return new OrderItemEditor(orderItem);
    }

    public void newQuantity(Double quantity) {
        realm.executeTransaction(realm1 -> orderItem.setQuantity(quantity));
    }

    public void incrementQuantity() {
        realm.executeTransaction(realm1 -> orderItem.increaseQuantity());
    }

    public void changeClient(int clientNumber) {
        realm.executeTransaction(realm1 -> orderItem.setClientNumber(clientNumber));
    }

    public void substractQuantity()  {
        if ((orderItem.getQuantity() - 1) > 0) {
            realm.executeTransaction(realm1 -> orderItem.decreaseQuantity());
        } else if ((orderItem.getQuantity() - 1) <= 0) {
            // TODO
            realm.executeTransaction(realm1 -> {
                OrderItem item = realm1.where(OrderItem.class)
                        .equalTo("Id", orderItem.getId()).findFirst();
                if (item != null) {
                    item.deleteFromRealm();
                }
            });
        }
    }

    public void delete() {
        realm.executeTransaction(realm1 -> orderItem.deleteFromRealm());
    }

    public void switchDish(int dishNumber) {
        realm.executeTransaction(realm1 -> orderItem.setDishNumber(dishNumber));
    }

    public void setMessage(String message) {
        realm.executeTransaction(realm1 -> orderItem.setMessage(message));
    }

    public static void create(OrderItem orderItem) {
        final Realm staticRealm = Realm.getDefaultInstance();

        int maxOrderId = 0;
        Number realmMaxOrderItemNumber = staticRealm.where(OrderItem.class).max("Id");

        if (realmMaxOrderItemNumber != null) {
            maxOrderId = realmMaxOrderItemNumber.intValue();
        }

        orderItem.setId(maxOrderId + 1);


        staticRealm.executeTransaction(realm1 -> realm1.copyToRealm(orderItem));

    }

    // save order into a json file or do something around Realm



}
