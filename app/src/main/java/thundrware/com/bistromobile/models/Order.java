package thundrware.com.bistromobile.models;

import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thundrware.com.bistromobile.interfaces.OrderSendingHandler;
import thundrware.com.bistromobile.networking.DataService;
import thundrware.com.bistromobile.networking.DataServiceProvider;

public class Order {

    DataService dataService;
    OrderDataTransferObject orderDataObject;

    public Order(OrderDataTransferObject object) {
        orderDataObject = object;
        dataService = DataServiceProvider.getDefault();
    }

    public void send(OrderSendingHandler handler)
    {
        String objectAsJson = new Gson().toJson(orderDataObject);

        dataService.sendItems(orderDataObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess();
                } else {
                    handler.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.onFailure(t);
            }
        });
    }
}
