package thundrware.com.bistromobile.ui.presenters;

import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.ui.OrderManagementActivity;
import thundrware.com.bistromobile.ui.views.OrderManagementActivityView;

public class OrderManagementActivityPresenter {

    OrderManagementActivityView view;

    public OrderManagementActivityPresenter(OrderManagementActivityView view) {
        this.view = view;
    }

    public void productsRecyclerViewReloaded(int categoryId) {
        view.reloadProductsRecyclerView(categoryId);
    }

    public void totalAmountChanged(Double value) {
        view.changeTotalAmount(value);
    }

    public void tableDetailsLoaded(Table table) {
        view.changeTableDetails(table);
    }
}
