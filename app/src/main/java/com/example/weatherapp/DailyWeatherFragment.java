package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherResult;
import com.example.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.weatherapp.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyWeatherFragment extends Fragment {

    ImageView imgWeather;
    TextView tvCity, tvHumidity, tvSunrise, tvSunset, tvPressure, tvTemperature, tvDescription, tvDateTime,tvGeoCoord;
    LinearLayout weatherLayout;
    ProgressBar progressBar;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap weatherMap;

    static DailyWeatherFragment instance;

    public static DailyWeatherFragment getInstance() {
        if (instance == null)
            instance = new DailyWeatherFragment();
        return instance;
    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DailyWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        weatherMap = retrofit.create(IOpenWeatherMap.class);
    }


    public static DailyWeatherFragment newInstance(String param1, String param2) {
        DailyWeatherFragment fragment = new DailyWeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        imgWeather = itemView.findViewById(R.id.weatherIv);
        tvCity = itemView.findViewById(R.id.cityTv);
        tvDateTime = itemView.findViewById(R.id.date_timeTv);
        tvDescription = itemView.findViewById(R.id.descTv);
        tvGeoCoord = itemView.findViewById(R.id.geocordsTv);
        tvHumidity = itemView.findViewById(R.id.humidityTv);
        tvSunrise = itemView.findViewById(R.id.sunriseTv);
        tvSunset = itemView.findViewById(R.id.sunsetTv);
        tvTemperature = itemView.findViewById(R.id.tempTv);
        tvPressure = itemView.findViewById(R.id.pressureTV);








        weatherLayout = (LinearLayout)itemView.findViewById(R.id.weatherPanel);
        progressBar = (ProgressBar)itemView.findViewById(R.id.pg);

        getWeatherInformation();


        return itemView;


    }

    private void getWeatherInformation() {
        compositeDisposable.add(weatherMap.getWeatherByLatLng(String.valueOf(Common.currentLocation.getLatitude()),
                String.valueOf(Common.currentLocation.getLongitude()),
                Common.APP_ID,
                "metric")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<WeatherResult>() {
            @Override
            public void accept(WeatherResult weatherResult) throws Exception {

                Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                        .append(weatherResult.getWeather().get(0).getIcon())
                        .append(".png").toString()).into(imgWeather);

                        tvCity.setText(weatherResult.getName());
                        tvDescription.setText(new StringBuilder("Weather in ")
                        .append(weatherResult.getName()).toString());
                        tvTemperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                        tvDateTime.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        tvPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                        tvHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                        tvSunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        tvSunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        tvGeoCoord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());





                        weatherLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        );
    }



}

