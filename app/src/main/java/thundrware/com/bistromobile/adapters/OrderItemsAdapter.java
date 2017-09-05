package thundrware.com.bistromobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.github.florent37.viewanimator.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ru.whalemare.sheetmenu.SheetMenu;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.OrderItemEditor;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.utils.StringUtils;

public class OrderItemsAdapter extends RealmRecyclerViewAdapter<OrderItem, OrderItemsAdapter.OrderItemViewHolder> {

    private Context mContext;

    public OrderItemsAdapter(Context context, @Nullable OrderedRealmCollection<OrderItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item_view_layout, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        holder.onBind(getData().get(position));
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderItemDetailsConstraintLayout)
        ConstraintLayout mOrderItemDetailsConstraintLayout;

        @BindView(R.id.orderItemNameTextView)
        TextView mOrderItemNameTextView;

        @BindView(R.id.orderItemPriceTextView)
        TextView mOrderItemPriceTextView;

        @BindView(R.id.orderItemQuantityTextView)
        TextView mOrderItemQuantityTextView;

        @BindView(R.id.orderItemDishSwitcherTextView)
        TextView mOrderItemDishSwitcherTextView;

        @BindView(R.id.itemIncrementImageView)
        ImageView mItemIncrementImageView;

        @BindView(R.id.itemSubstractImageView)
        ImageView mItemSubstractImageView;

        View mView;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }

        public void onBind(OrderItem orderItem) {


            setItemNameText(orderItem.getProduct().getName());
            setItemPriceText(orderItem.getProduct().getPrice());
            setItemQuantityText(orderItem.getQuantity());

            if (orderItem.isNew()) {

                setDishSwitcherText(orderItem.getDishNumber());
                mOrderItemDishSwitcherTextView.setOnClickListener(createOnDishSwitcherClickListener(orderItem));
                mOrderItemDetailsConstraintLayout.setOnClickListener(createOnItemClickListener(orderItem));
                mItemSubstractImageView.setOnLongClickListener(createSubstractImageViewLongClickListener(orderItem));
                mItemSubstractImageView.setOnClickListener(createSusbtractImageViewClickListener(orderItem));
                mItemIncrementImageView.setOnClickListener(createIncrementImageViewClickListener(orderItem));
                mView.setOnLongClickListener(createOnItemViewLongClickListener(orderItem));

                setViewsVisibility(View.VISIBLE, mItemIncrementImageView, mItemSubstractImageView, mOrderItemDishSwitcherTextView);

            } else {

                /*
                    The order is loaded, therefore it means that the waiter isn't allowed to change the dish number, substract, increment or edit the quantity of an item.
                    */
                setViewsVisibility(View.INVISIBLE, mItemIncrementImageView, mItemSubstractImageView, mOrderItemDishSwitcherTextView);
            }
        }

        private void setItemQuantityText(Double quantityText) {
            mOrderItemQuantityTextView.setText(String.valueOf(quantityText));
        }

        private void setItemNameText(String itemName) {
            mOrderItemNameTextView.setText(itemName);
        }

        private void setItemPriceText(Double itemPrice) {
            mOrderItemPriceTextView.setText(String.valueOf(itemPrice));
        }

        private void setDishSwitcherText(int dishNumber) {

            String dishText = "";

            switch(dishNumber) {
                case 0:
                    dishText = "FELUL 1";
                    break;
                case 1:
                    dishText = "FELUL 2";
                    break;
                case 2:
                    dishText = "DESERT";
                    break;
            }

            mOrderItemDishSwitcherTextView.setText(dishText);
        }

        private View.OnClickListener createOnItemClickListener(final OrderItem orderItem) {
            return view -> new MaterialDialog.Builder(mContext)
                    .title("Editare cantitate")
                    .content("Introdu noua cantitate dorită")
                    .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input(null, null, (dialog, input) -> {

                        if (!StringUtils.isNullOrEmpty(input.toString())) {
                            OrderItemEditor.edit(orderItem)
                                    .newQuantity(Double.parseDouble(input.toString()));

                            // TODO Exception in case the waiter has input some bad shit

                            onQuantityChangedHandler(orderItem);
                        }
                    })
                    .positiveText("MODIFICĂ")
                    .show();
        }

        private View.OnClickListener createOnDishSwitcherClickListener(final OrderItem orderItem) {
            return view -> {
                TextView dishTextView = (TextView) view;
                String textViewValue = dishTextView.getText().toString();

                switch(textViewValue)  {
                    case "FELUL 1":
                        OrderItemEditor.edit(orderItem)
                                .switchDish(1);

                        dishTextView.setText("FELUL 2");
                        break;
                    case "FELUL 2":
                        OrderItemEditor.edit(orderItem)
                                .switchDish(2);

                        dishTextView.setText("DESERT");
                        break;
                    case "DESERT":
                        OrderItemEditor.edit(orderItem)
                                .switchDish(0);

                        dishTextView.setText("FELUL 1");
                        break;
                }

                ViewAnimator.animate(dishTextView)
                                .bounce()
                                .duration(2000)
                                .start();


            };
        }

        private View.OnLongClickListener createSubstractImageViewLongClickListener(final OrderItem orderItem) {
            return view -> {
                OrderItemEditor.edit(orderItem)
                        .delete();
                return true;
            };
        }

        private View.OnClickListener createSusbtractImageViewClickListener(final OrderItem orderItem) {
            return view -> {
                try {
                    OrderItemEditor.edit(orderItem)
                            .substractQuantity();
                } catch (Exception ex) {
                    AlertMessage.showMessage((Activity)mContext, "Eroare", "Cantitatea va fi nula, mai bine stergeti-l");
                    // TODO
                }

                onQuantityChangedHandler(orderItem);
            };
        }

        private View.OnClickListener createIncrementImageViewClickListener(final OrderItem orderItem) {
            return view -> {
                OrderItemEditor.edit(orderItem)
                        .incrementQuantity();

                onQuantityChangedHandler(orderItem);
            };
        }

        private View.OnLongClickListener createOnItemViewLongClickListener(final OrderItem orderItem) {
            return itemView -> {
                SheetMenu.with(mContext)
                        .setTitle("Opțiune")
                        .setMenu(R.menu.order_item_long_click_menu)
                        .setClick(menuItem -> {
                            String condensedTitle = menuItem.getTitleCondensed().toString();
                            switch (condensedTitle) {
                                case "setMessage":
                                    // messageEditTextPopup
                                    new MaterialDialog.Builder(mContext)
                                            .title("Adăugare mențiune")
                                            .content("Introdu mesajul pentru bucătărie")
                                            .inputType(InputType.TYPE_CLASS_TEXT)
                                            .positiveText("ADAUGĂ")
                                            .input(null, null, (dialog, input) -> {
                                                if (!StringUtils.isNullOrEmpty(input.toString())) {
                                                    OrderItemEditor.edit(orderItem)
                                                            .setMessage(input.toString());
                                                } else {
                                                    dialog.setContent("Mesajul pentru bucătărie nu poate fi gol");
                                                }
                                            })
                                            .show();
                                    break;
                                case "changeClient":
                                    /* List<String> clientsBesideTheCurrentOne = new ArrayList<>();
                                    Integer[] clientNumbers = new Integer[] { 1, 2, 3, 4};

                                    for (Integer clientNumber : clientNumbers) {
                                        if (clientNumber != orderItem.getClient()) {
                                            clientsBesideTheCurrentOne.add("Client " + clientNumber);
                                        }
                                    }


                                    new MaterialDialog.Builder(mContext)
                                            .title("Schimbă clientul")
                                            .items(clientsBesideTheCurrentOne)
                                            .itemsCallbackSingleChoice(-1, (dialog, itemView1, which, text) -> {
                                                int clientNumber = Integer.parseInt(text.toString().replace("Client ", ""));
                                                mOrderManager.setClientNumber(orderItem, clientNumber);
                                                return false;
                                            })
                                            .show();

                                    break; */
                            }
                            return true;
                        })
                        .show();
                return true;
            };
        }

        private void setViewsVisibility(int visibility, View... views) {
            for (View view : views) {
                view.setVisibility(visibility);
            }
        }

        private void onQuantityChangedHandler(OrderItem orderItem) {
            setItemQuantityText(orderItem.getQuantity());
        }
    }
}
