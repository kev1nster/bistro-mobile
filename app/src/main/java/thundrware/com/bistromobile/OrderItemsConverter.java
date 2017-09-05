package thundrware.com.bistromobile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thundrware.com.bistromobile.data.repositories.ProductsRepository;
import thundrware.com.bistromobile.models.OrderItem;
import thundrware.com.bistromobile.models.Product;

public class OrderItemsConverter {

    public static List<OrderItem> convert(String jsonData) {

        List<OrderItem> orderItems = new ArrayList<>();
        try {

            JSONArray orderItemsJsonArray = new JSONArray(jsonData);

            for (int i=0; i < orderItemsJsonArray.length(); i++) {
                JSONObject orderItemJson = orderItemsJsonArray.getJSONObject(i);

                OrderItem orderItem = new OrderItem();
                orderItem.setCategoryId(orderItemJson.getInt("CategoryId"));
                orderItem.setQuantity(orderItemJson.getDouble("Quantity"));
                orderItem.setId(orderItemJson.getInt("Id"));

                ProductsRepository productsRepository = new ProductsRepository();
                Product product = productsRepository.get(orderItemJson.getInt("ProductId"));
                orderItem.setProduct(product);
                orderItem.setNew(false);

                orderItems.add(orderItem);
            }

        } catch (Exception ex) {
            // TODO exUnused
        }

        return orderItems;

    }
}
