package thundrware.com.bistromobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import thundrware.com.bistromobile.OrderItemEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Product;

public class ProductsAdapter extends ExpandableRecyclerViewAdapter<ProductsAdapter.ProductParentViewHolder, ProductsAdapter.ProductViewHolder> {

    private Context mContext;
    private int categoryId;

    public ProductsAdapter(Context context, int categoryId, List<? extends ExpandableGroup> groups) {
        super(groups);
        mContext = context;
        this.categoryId = categoryId;
    }

    @Override
    public ProductParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_group_view_layout, parent, false);
        return new ProductParentViewHolder(view);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_view_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Product product = (Product) group.getItems().get(childIndex);
        holder.onBind(product);
    }

    @Override
    public void onBindGroupViewHolder(ProductParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.onBind((ProductGroup)group);
    }

    // INNER CLASSES
    public class ProductParentViewHolder extends GroupViewHolder {

        @BindView(R.id.productGroupNameTextView)
        TextView productGroupNameTextView;

        @BindView(R.id.groupProductsAmountTextView)
        TextView groupProductsAmountTextView;

        public ProductParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(ProductGroup group) {
            setProductGroupName(group.getTitle());
            setGroupProductsAmountText(group.getItems().size());
        }

        private void setProductGroupName(String productGroupName) {
            productGroupNameTextView.setText(productGroupName);
        }

        private void setGroupProductsAmountText(int amount) {
            groupProductsAmountTextView.setText(String.valueOf(amount) + " ARTICOLE");
        }
    }



    public class ProductViewHolder extends ChildViewHolder {

        @BindView(R.id.productNameTextView)
        TextView productNameTextView;

        @BindView(R.id.productPriceTextView)
        TextView productPriceTextView;

        Product productObject;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(Product product) {
            setProductName(product.getName());
            setProductPrice(product.getPrice());
            productObject = product;
        }

        @OnClick
        public void onViewClickedHandler() {
            // it means the user wants to add the product to the order

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productObject);
            orderItem.setQuantity(1);
            orderItem.setCategoryId(categoryId);
            orderItem.setNew(true);
            orderItem.setProductId(productObject.getId());

            OrderItemEditor.create(orderItem);
        }

        private void setProductName(String productName) {
            productNameTextView.setText(productName);
        }

        private void setProductPrice(Double productPrice) {
            productPriceTextView.setText("Preț: " + String.valueOf(productPrice) + " lei");
        }

    }



}
