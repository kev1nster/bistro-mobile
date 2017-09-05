package thundrware.com.bistromobile.models;

import java.util.ArrayList;
import java.util.List;

public class OrderDataTransferObject {
    private List<OrderItem> NewItems;
    private Table Table;
    private int WaiterId;

    public OrderDataTransferObject() {
        NewItems = new ArrayList<>();
    }

    public List<OrderItem> getNewItems() {
        return NewItems;
    }

    public void setNewItems(List<OrderItem> newItems) {
        NewItems = newItems;
    }

    public thundrware.com.bistromobile.models.Table getTable() {
        return Table;
    }

    public void setTable(thundrware.com.bistromobile.models.Table table) {
        Table = table;
    }

    public int getWaiterId() {
        return WaiterId;
    }

    public void setWaiterId(int waiterId) {
        WaiterId = waiterId;
    }
}
