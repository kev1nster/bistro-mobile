package thundrware.com.bistromobile.ui.presenters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import thundrware.com.bistromobile.models.ActiveTable;
import thundrware.com.bistromobile.models.Waiter;
import thundrware.com.bistromobile.ui.views.MainActivityView;

public class MainActivityPresenter {

    MainActivityView view;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }

    public void activeTablesDataReceived(List<ActiveTable> activeTableList) {
        view.fillRecyclerView(activeTableList);
    }

    public void launchActivityButtonPressed(Class activity) {
        view.launchOtherActivity(activity);
    }
}
