package com.example.weatherapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherForecastResult;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResult weatherForecastResult;

    public ForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Load icon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.imgWeather);

        holder.dateTextView.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(position).dt)));

        holder.descTextView.setText(new StringBuilder(weatherForecastResult.list.get(position).weather.get(0).getDescription()));

        holder.temperatureTextView.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C"));


    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView, descTextView, temperatureTextView;
        ImageView imgWeather;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgWeather = itemView.findViewById(R.id.weatherIv);
            dateTextView = itemView.findViewById(R.id.dateTv);
            temperatureTextView = itemView.findViewById(R.id.tempTv);
            descTextView = itemView.findViewById(R.id.descriptionTV);
        }
    }
}
