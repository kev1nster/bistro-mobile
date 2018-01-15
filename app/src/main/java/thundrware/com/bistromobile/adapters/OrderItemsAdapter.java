package thundrware.com.bistromobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import ru.whalemare.sheetmenu.SheetMenu;
import thundrware.com.bistromobile.AlertMessage;
import thundrware.com.bistromobile.OrderItemEditor;
import thundrware.com.bistromobile.QuantityChangeErrorListener;
import thundrware.com.bistromobile.R;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.utils.StringUtils;

public class OrderItemsAdapter extends RealmRecyclerViewAdapter<OrderItem, OrderItemsAdapter.OrderItemViewHolder> {

    private Context mContext;
    private QuantityChangeErrorListener quantityChangeErrorListener;

    public OrderItemsAdapter(Context context, @Nullable OrderedRealmCollection<OrderItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
        mContext = context;
        quantityChangeErrorListener = (QuantityChangeErrorListener) context;
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

        @BindView(R.id.orderItemProductNameTextView)
        TextView productNameTextView;

        @BindView(R.id.orderItemDishNumberTextView)
        TextView dishNumberTextView;

        @BindView(R.id.incrementImageView)
        ImageView incrementImageView;

        @BindView(R.id.decrementImageView)
        ImageView decrementImageView;

        @BindView(R.id.itemQuantityText)
        TextView itemQuantityText;

        @BindView(R.id.messageTextView)
        TextView messageTextView;

        @BindView(R.id.dishNumberLabel)
        TextView dishNumberLabel;

        @BindView(R.id.bottomLayout)
        ConstraintLayout bottomLayout;

        @BindView(R.id.topLayout)
        ConstraintLayout topLayout;

        @BindView(R.id.clientNumberTextView)
        TextView clientNumberTextView;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(OrderItem orderItem) {

            // Setting the item amount
            Double quantity = orderItem.getQuantity();
            itemQuantityText.setText(String.format("%.2f", quantity));



            // Setting the product name
            productNameTextView.setText(orderItem.getProduct().getName());

            if (orderItem.isNew()) {
                // Setting dish switcher text
                setDishNumberText(orderItem.getDishNumber());

                setClientNumberText(orderItem.getClientNumber());
                clientNumberTextView.setOnClickListener(createOnClientNumberTextViewClickListener(orderItem));

                // Setting dish switcher click listener
                dishNumberTextView.setOnClickListener(createOnDishSwitcherClickListener(orderItem));

                // Setting increment imageView click listener
                incrementImageView.setOnClickListener(createIncrementImageViewClickListener(orderItem));

                // Setting substract imageView click listener & longClick listener
                decrementImageView.setOnClickListener(createSusbtractImageViewClickListener(orderItem));
                decrementImageView.setOnLongClickListener(createSubstractImageViewLongClickListener(orderItem));

                messageTextView.setOnClickListener(createOnMessageTextViewClickListener(orderItem));
                itemQuantityText.setOnClickListener(createOnItemQuantityTextClickListener(orderItem));

            } else {
                /*
                    The order is loaded, therefore it means that the waiter isn't allowed to change the dish number, substract, increment or edit the quantity of an item.
                    */
                setViewsVisibility(View.GONE, clientNumberTextView, dishNumberTextView, incrementImageView, decrementImageView, messageTextView, dishNumberTextView, dishNumberLabel);
                setBottomLayoutConstraintToParentTop();

            }
        }

        private void setClientNumberText(int clientNumber) {
            clientNumberTextView.setText(String.format("CLIENT %d", clientNumber));
        }

        private void setBottomLayoutConstraintToParentTop() {

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)bottomLayout.getLayoutParams();
            params.topToTop = topLayout.getId();
            itemQuantityText.setPadding(2, 2, 2, 2);

            final float scale = mContext.getResources().getDisplayMetrics().density;
            params.height = (int) (35 * scale);
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

        private View.OnClickListener createOnItemQuantityTextClickListener(final OrderItem orderItem) {
            return view -> {
              new MaterialDialog.Builder(mContext)
                      .title("Schimbare cantitate")
                      .content("Introdu o nouă cantitate pentru produsul selectat")
                      .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED)
                      .positiveText("GATA")
                      .input("Introdu cantitatea dorită", String.valueOf(orderItem.getQuantity()), (dialog, input) -> {
                          if (!StringUtils.isNullOrEmpty(input.toString())) {

                              Double quantityInput = Double.parseDouble(input.toString());
                              if (quantityInput > 0) {
                                  // then it's ok
                                  OrderItemEditor.edit(orderItem)
                                          .newQuantity(quantityInput);
                              } else {
                                  // u wot m8
                                  quantityChangeErrorListener.onQuantityError("Cantitatea nu poate fi negativă!");
                              }

                          } else {
                              quantityChangeErrorListener.onQuantityError("Trebuie să introduceți o valoare pentru a schimba cantitatea!");
                          }
                      })
                      .show();
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

        private View.OnClickListener createOnMessageTextViewClickListener(final OrderItem orderItem) {
            return view ->
                    new MaterialDialog.Builder(mContext)
                            .title("Adăugare mesaj")
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

        }

        private View.OnClickListener createOnClientNumberTextViewClickListener(final OrderItem orderItem) {
            return view -> {
                final int currentClientNumber = orderItem.getClientNumber();
                int nextClientNumber = 0;

                switch(currentClientNumber) {
                    case 1:
                        nextClientNumber = 2;
                        break;
                    case 2:
                        nextClientNumber = 3;
                        break;
                    case 3:
                        nextClientNumber = 4;
                        break;
                    case 4:
                        nextClientNumber = 1;
                        break;
                }

                OrderItemEditor.edit(orderItem).changeClient(nextClientNumber);
                setClientNumberText(nextClientNumber);

            };
        }

        private void setViewsVisibility(int visibility, View... views) {
            for (View view : views) {
                view.setVisibility(visibility);
            }
        }
    }
}
