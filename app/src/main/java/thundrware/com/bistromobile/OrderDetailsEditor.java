package thundrware.com.bistromobile;

import java.util.List;

import io.realm.Realm;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Table;

public class OrderDetailsEditor {

    private Realm realm;

    private boolean isLoaded = false;
    private Table table;

    private static OrderDetailsEditor editorInstance = null;

    protected OrderDetailsEditor() {
        realm = Realm.getDefaultInstance();
        clearCurrentOrdersIfAny();
    }

    /*
        PUBLIC METHODS
        */


    public static void createNew(Table table) {
        OrderDetailsEditor editor = new OrderDetailsEditor();
        editor.table = table;

        editorInstance = editor;
    }

    public static void existing(Table table, List<OrderItem> orderItemList) {
        OrderDetailsEditor editor = new OrderDetailsEditor();
        editor.isLoaded = true;
        editor.table = table;
        editor.persistLoadedItems(orderItemList);

        editorInstance = editor;
    }

    public static boolean isNew() {
        return !editorInstance.isLoaded;
    }

    public static Table getTable() {
        return editorInstance.table;
    }




    /*
        PRIVATE METHODS
        */


    private void clearCurrentOrdersIfAny() {
        realm.executeTransaction(realm1 -> realm1.where(OrderItem.class).findAll().deleteAllFromRealm());
    }

    private void persistLoadedItems(List<OrderItem> orderItems) {
        realm.executeTransaction(realm1 -> realm1.copyToRealm(orderItems));
    }
}
