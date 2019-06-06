package retrofit;

import model.MyWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    String BASE_URL = "https://api.apixu.com/v1/";

    @GET("forecast.json")
    Call<MyWeather> getWeatherData(@Query("key") String token, @Query("q") String city,
                              @Query("days") String days);
}