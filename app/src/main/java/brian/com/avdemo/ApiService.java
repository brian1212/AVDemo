package brian.com.avdemo;

import brian.com.avdemo.model.AppsList;
import brian.com.avdemo.model.TvModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Brian  on 06/02/2017.
 */
public interface ApiService {
    @GET("tuvi.json")
    Call<AppsList> getMyJSON();
}
