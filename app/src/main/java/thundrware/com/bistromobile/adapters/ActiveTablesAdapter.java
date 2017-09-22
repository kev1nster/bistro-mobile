package thundrware.com.bistromobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.OrderDetailsEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.ServerConnectionDetailsManager;
import thundrware.com.bistromobile.WaiterManager;
import thundrware.com.bistromobile.data.repositories.AreasRepository;
import thundrware.com.bistromobile.models.ActiveTable;
import thundrware.com.bistromobile.models.Area;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Table;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;
import thundrware.com.bistromobile.ui.OrderManagementActivity;
import thundrware.com.bistromobile.OrderItemsConverter;

public class ActiveTablesAdapter extends RecyclerView.Adapter<ActiveTablesAdapter.ActiveTableViewHolder> {

    List<ActiveTable> mActiveTables;
    Context mContext;

    public ActiveTablesAdapter(Context context, List<ActiveTable> activeTables) {
        mActiveTables = activeTables;
        mContext = context;
    }

    @Override
    public ActiveTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.active_table_item_view, parent, false);
        return new ActiveTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActiveTableViewHolder holder, int position) {
        holder.onBind(mActiveTables.get(position));
    }

    @Override
    public int getItemCount() {
        return mActiveTables.size();
    }

    public class ActiveTableViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.activeTableAreaNameTextView)
        TextView activeTableAreaNameTextView;

        @BindView(R.id.activeTableNameTextView)
        TextView activeTableNameTextView;

        @BindView(R.id.activeTableProductLoadingProgressBar)
        ProgressBar activeTableItemsLoadingProgressBar;

        Table table;
        View view;


        public ActiveTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void onBind(ActiveTable table) {

            int areaId = table.getTable().getAreaId();
            Area area = new AreasRepository().get(areaId);

            this.table = table.getTable();

            setAreaNameText(area);
            setTableNameText(table.getTable().getTableNumber());
        }

        @OnClick
        public void onActiveTableViewClicked() {

            ServerConnectionDetailsManager serverConnectionDetailsManager = new ServerConnectionDetailsManager();
            DataService service = DataServiceProvider.create(serverConnectionDetailsManager.getConnectionDetails().toString());

            turnLoadingModeOn();

            service.getItemsFor(table.getAreaId(), table.getTableNumber(), new WaiterManager().getCurrentWaiter().getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                List<OrderItem> orderItems = OrderItemsConverter.convert(response.body().string());
                                OrderDetailsEditor.existing(table, orderItems);

                                Intent intent = new Intent(mContext, OrderManagementActivity.class);
                                mContext.startActivity(intent);
                            } catch (Exception jsonEx) {
                                Log.e("ACTIVE_TABLE_ERROR", "onFailure - " + jsonEx.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ACTIVE_TABLE_ERROR", "onFailure - " + t.getMessage());
                        }
                    });

            /*

                Load items into OrderItem table;
                Set up OrderDetailsEditor singleton class;
                Launch OrderManagementActivity.

             */
        }

        private void setTableNameText(Integer tableNumber) {
            activeTableNameTextView.setText("Masa nr. " + (tableNumber + 1));
        }

        private void setAreaNameText(Area area) {
            String areaName = area.getName();
            areaName = areaName.substring(0, 1).toUpperCase() + areaName.substring(1).toLowerCase();
            activeTableAreaNameTextView.setText(areaName);
        }

        private void turnLoadingModeOn() {

            // hide indicators
            activeTableNameTextView.setVisibility(View.GONE);
            activeTableAreaNameTextView.setVisibility(View.GONE);

            // show loading message
            activeTableItemsLoadingProgressBar.setVisibility(View.VISIBLE);

        }
    }
}
