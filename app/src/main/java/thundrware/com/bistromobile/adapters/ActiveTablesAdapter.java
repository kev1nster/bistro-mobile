package thundrware.com.bistromobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.models.Product;

public class ActiveTablesAdapter extends RealmRecyclerViewAdapter<Product, ActiveTablesAdapter.ActiveTableViewHolder> {

    private Context mContext;

    public ActiveTablesAdapter(Context context, OrderedRealmCollection<Product> productList) {
        super(productList, true);
        mContext = context;
    }

    @Override
    public ActiveTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.active_order_layout, parent, false);
        return new ActiveTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActiveTableViewHolder holder, int position) {
        holder.onBind(getData().get(position));
    }

    public class ActiveTableViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productName) TextView productName;
        @BindView(R.id.productPrice) TextView productPrice;

        public ActiveTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(Product product) {
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));
        }
    }
}
