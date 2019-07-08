package com.example.cars;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SampleClass {
    private DataInterface mListener;

    public SampleClass() {
        super();
    }

    public void getDataForId(final Context context) {
        //get Json file data
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://development.espark.lt/").addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface jsonPlaceHolderAPI = retrofit.create(ApiInterface.class);
        Call<List<Car>> call = jsonPlaceHolderAPI.getCars();
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response!=null && response.body() != null && mListener != null) {
                    //get car data
                    List<Car> cars = response.body();
                    //pass data to litener
                    mListener.responseData(cars);
                }
                else{
                    Toast.makeText(context, "Code is "+ response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setOnDataListener(DataInterface listener) {
        mListener = listener;
    }

    public interface DataInterface {
        void responseData(List<Car> cars);
    }
}
