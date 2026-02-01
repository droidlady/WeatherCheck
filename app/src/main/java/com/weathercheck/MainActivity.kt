package com.weathercheck;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratibha.myweathercheck.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.WeatherAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.Forecastday;
import model.MyWeather;
import util.Utility;
import viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;
    WeatherAdapter adapter;
    ArrayList<Forecastday> forecastdayArrayList = new ArrayList<>();
    WeatherViewModel model;
    LinearLayout noData;

    @BindView(R.id.cityText)
    TextView tvCityName;
    @BindView(R.id.tempText)
    TextView tvTemp;
    @BindView(R.id.condition)
    TextView tvCondition;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind the view using butterknife
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

      //  collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int resId = R.anim.layout_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);

        noData = findViewById(R.id.noData);

        model = ViewModelProviders.of(this).get(WeatherViewModel.class);

        if (Utility.isNetworkAvailable(this)) {
            myWeatherModel();
        } else {
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Snackbar
                    .make(coordinatorLayout, getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();


        }

    }



    public void myWeatherModel() {

        model.getWeatherData().observe(this, new Observer<MyWeather>() {
            @Override
            public void onChanged(@Nullable MyWeather weatherData) {


                collapsingToolbarLayout.setTitle(weatherData.getLocation().getName());
                tvCityName.setText(weatherData.getLocation().getName());
                tvTemp.setText(Math.round(weatherData.getCurrent().getTempC()) + getString(R.string.mydegree));
                tvCondition.setText(weatherData.getCurrent().getCondition().getText());
                Picasso.with(MainActivity.this).load("http:" + weatherData.getCurrent().getCondition().getIcon())
                        .error(R.drawable.ic_launcher_foreground).resize(64, 64).centerCrop()
                        .into(image);

                List<Forecastday> forecastData = weatherData.getForecast().getForecastday();
                forecastdayArrayList.addAll(forecastData);

                if (adapter == null) {
                    adapter = new WeatherAdapter(MainActivity.this, forecastdayArrayList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(true);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}

