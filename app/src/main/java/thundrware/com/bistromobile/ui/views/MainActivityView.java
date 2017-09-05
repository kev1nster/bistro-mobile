package thundrware.com.bistromobile.ui.views;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import thundrware.com.bistromobile.models.ActiveTable;

public interface MainActivityView {
    void launchOtherActivity(Class clazz);
    void fillRecyclerView(List<ActiveTable> activeTableList);
}
