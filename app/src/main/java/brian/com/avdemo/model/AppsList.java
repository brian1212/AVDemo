package brian.com.avdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian  on 06/02/2017.
 */

public class AppsList {

    @SerializedName("apps")
    private List<TvModel> apps = new ArrayList<>();

    public List<TvModel> getApps() {
        return apps;
    }

    public void setApps(List<TvModel> apps) {
        this.apps = apps;
    }
}
