package thundrware.com.bistromobile.ui.views;

import thundrware.com.bistromobile.models.Table;

public interface OrderManagementActivityView {
    void changeTotalAmount(Double value);
    void reloadProductsRecyclerView(int categoryId);
    void changeTableDetails(Table table);
}
