 package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherapp.Adapter.ForecastAdapter;
import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherForecastResult;
import com.example.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.weatherapp.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap weatherMap;

    TextView cityTextView, geoCoordTextView;
    RecyclerView re_forecast;

    static ForecastFragment instance;




    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }


    public ForecastFragment() {
       compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        weatherMap = retrofit.create(IOpenWeatherMap.class);
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View itemView = inflater.inflate(R.layout.fragment_forecast,container,false);

       cityTextView = (TextView)itemView.findViewById(R.id.city_tv);
       geoCoordTextView = (TextView) itemView.findViewById(R.id.geocoord_tv);

       re_forecast = (RecyclerView)itemView.findViewById(R.id.recycler_forecast);
       re_forecast.setHasFixedSize(true);
       re_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

       getForecastWeatherInformation();

       return itemView;
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(weatherMap.getForecastByLatLng(
                String.valueOf(Common.currentLocation.getLatitude()),
                String.valueOf(Common.currentLocation.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", ""+throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        cityTextView.setText(new StringBuilder(weatherForecastResult.city.name));
        geoCoordTextView.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));


        ForecastAdapter adapter = new ForecastAdapter(getContext(),weatherForecastResult);
        re_forecast.setAdapter(adapter);
    }
}