package com.example.fetchcode.utills;

import com.example.fetchcode.models.Item;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
    public interface ApiCallback{
        void onSuccess(List<Item> myItems);
        void onFailure(String errorMessage);
    }

    private final OkHttpClient client;
    private final Gson gson;

    public ApiService() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void fetchDataFromApi(ApiCallback callback) {
        String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json"; // Replace with your API endpoint URL

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Item[] myItemArray = gson.fromJson(responseBody, Item[].class);
                    List<Item> my_Items = Arrays.asList(myItemArray);

                    callback.onSuccess(my_Items);

//                    Item myItem = gson.fromJson(responseBody, Item.class);
//                    callback.onSuccess(myItem);
                } else {
                    callback.onFailure("Failed to fetch data from API. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed to fetch data from API. Error: " + e.getMessage());
            }
        });
    }

}
