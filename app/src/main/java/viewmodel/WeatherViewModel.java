package viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.pratibha.myweathercheck.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import model.MyWeather;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.Constants;

public class WeatherViewModel extends ViewModel {

    //fetched asynchronously
    private MutableLiveData<MyWeather> weatherMutableLiveData;

    //get the data
    public LiveData<MyWeather> getWeatherData() {
        //null checks
        if (weatherMutableLiveData == null) {
            weatherMutableLiveData = new MutableLiveData<MyWeather>();
            callWeatherInformation();
        }
        return weatherMutableLiveData;
    }


    //get the JSON data from URL
    private void callWeatherInformation() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                // .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<MyWeather> call = apiService.getWeatherData(Constants.key, Constants.cityLat, Constants.days);


        call.enqueue(new Callback<MyWeather>() {
            @Override
            public void onResponse(Call<MyWeather> call, Response<MyWeather> response) {

                if (response.body() == null)
                    return;
                if (response.isSuccessful())
                    //set list to our MutableLiveData
                    weatherMutableLiveData.setValue(response.body());

            }

            @Override
            public void onFailure(Call<MyWeather> call, Throwable t) {
                t.printStackTrace();
                weatherMutableLiveData.setValue(null);
            }
        });
    }


    private Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request originalRequest = chain.request(); //Current Request

            okhttp3.Response response = chain.proceed(
                    originalRequest); //Get response of the request

            if (BuildConfig.DEBUG) {
                //logging the response
                String bodyString = response.body().string();
                Log.e("RETROFIT", "Sending request %s with headers " +
                        "\n%s\nInput %s" +
                        "\nResponse HTTP %s %s \n" + "Body %s\n" + originalRequest.url()
                        + originalRequest.headers() + bodyToString(originalRequest) +
                        response.code() + response.message() + bodyString);
                response = response.newBuilder().body(
                        ResponseBody.create(response.body().contentType(), bodyString)).build();
            }

            return response;
        }
    };

    private String bodyToString(final Request request) {
        String body = "";
        try {

            Buffer buffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(buffer);
                body = buffer.readUtf8();
            }
            return body;
        } catch (final IOException e) {
            return "Not Working";
        }
    }



}