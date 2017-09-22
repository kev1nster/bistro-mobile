package thundrware.com.bistromobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.github.florent37.viewanimator.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
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

        @BindView(R.id.orderItemAmountTextView)
        TextView productAmountTextView;

        @BindView(R.id.orderItemProductNameTextView)
        TextView productNameTextView;

        @BindView(R.id.orderItemSubstractImageView)
        ImageView substractImageView;

        @BindView(R.id.orderItemIncrementImageView)
        ImageView incrementImageView;

        @BindView(R.id.orderItemDishNumberTextView)
        TextView dishNumberTextView;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(OrderItem orderItem) {

            // Setting the item amount
            productAmountTextView.setText(String.valueOf(orderItem.getQuantity()));

            // Setting the product name
            productNameTextView.setText(ellipsize(orderItem.getProduct().getName(), 30));

            if (orderItem.isNew()) {
                // Setting dish switcher text
                setDishNumberText(orderItem.getDishNumber());

                // Setting dish switcher click listener
                dishNumberTextView.setOnClickListener(createOnDishSwitcherClickListener(orderItem));

                // Setting increment imageView click listener
                incrementImageView.setOnClickListener(createIncrementImageViewClickListener(orderItem));

                // Setting substract imageView click listener & longClick listener
                substractImageView.setOnClickListener(createSusbtractImageViewClickListener(orderItem));
                substractImageView.setOnLongClickListener(createSubstractImageViewLongClickListener(orderItem));

            } else {
                /*
                    The order is loaded, therefore it means that the waiter isn't allowed to change the dish number, substract, increment or edit the quantity of an item.
                    */
                setViewsVisibility(View.GONE, dishNumberTextView, incrementImageView, substractImageView);

            }
        }



        private void setDishNumberText(int dishNumber) {
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

            dishNumberTextView.setText(dishText);
        }



        /*
            Start of ellipsize-related stuff
         */

        private final String NON_THIN = "[^iIl1\\.,']";

        private int textWidth(String str) {
            return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
        }

        public String ellipsize(String text, int max) {

            if (textWidth(text) <= max)
                return text;

            // Start by chopping off at the word before max
            // This is an over-approximation due to thin-characters...
            int end = text.lastIndexOf(' ', max - 3);

            // Just one long word. Chop it off.
            if (end == -1)
                return text.substring(0, max-3) + "...";

            // Step forward as long as textWidth allows.
            int newEnd = end;
            do {
                end = newEnd;
                newEnd = text.indexOf(' ', end + 1);

                // No more spaces.
                if (newEnd == -1)
                    newEnd = text.length();

            } while (textWidth(text.substring(0, newEnd) + "...") < max);

            return text.substring(0, end) + "...";
        }

        /*
            End of ellipsize-related stuff
         */

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

                if (orderItem.getQuantity() -1 <= 0) {
                    new MaterialDialog.Builder(mContext)
                            .title("Alertă")
                            .content("Cantitatea va fi negativă. Doriți să ștergeți?")
                            .positiveText("DA")
                            .negativeText("NU")
                            .onPositive((dialog, which) -> {
                                OrderItemEditor.edit(orderItem)
                                        .substractQuantity();

                            }).show();
                } else {
                    OrderItemEditor.edit(orderItem)
                            .substractQuantity();
                }

            };
        }

        private View.OnClickListener createIncrementImageViewClickListener(final OrderItem orderItem) {
            return view -> {
                OrderItemEditor.edit(orderItem)
                        .incrementQuantity();
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
                                            .input(null, orderItem.getMessage(), (dialog, input) -> {
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
    }
}
