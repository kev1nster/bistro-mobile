package thundrware.com.bistromobile.data.datalayer;

import java.util.ArrayList;
import java.util.List;

import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.Category;
import thundrware.com.bistromobile.models.Group;
import thundrware.com.bistromobile.models.Product;
import thundrware.com.bistromobile.models.Waiter;

public class DataAdderEngine {

    private List<Executable> mCommandsToExecute = null;

    public DataAdderEngine() {
        mCommandsToExecute = new ArrayList<>();
    }

    public DataAdderEngine addProducts(List<Product> productList) {
        mCommandsToExecute.add(new ProductsAdder(productList));
        return this;
    }

    public DataAdderEngine addAreas(List<Area> areaList) {
        mCommandsToExecute.add(new AreasAdder(areaList));
        return this;
    }

    public DataAdderEngine addGroups(List<Group> groupList) {
        mCommandsToExecute.add(new GroupsAdder(groupList));
        return this;
    }

    public DataAdderEngine addWaiters(List<Waiter> waiterList) {
        mCommandsToExecute.add(new WaitersAdder(waiterList));
        return this;
    }

    public DataAdderEngine addCategories(List<Category> categoryList) {
        mCommandsToExecute.add(new CategoriesAdder(categoryList));
        return this;
    }

    public void execute() {

        for (int i=0; i < mCommandsToExecute.size(); i++) {
            mCommandsToExecute.get(i).execute();
        }
    }

    // throwables in case you already added them

}
