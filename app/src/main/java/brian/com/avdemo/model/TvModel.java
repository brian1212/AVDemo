package brian.com.avdemo.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class TvModel extends RealmObject {

    @SerializedName("name")
    private String name;
    @SerializedName("thumbnail_url")
    private String thumbnail_url;
    @SerializedName("app_store_url")
    private String app_store_url;
    @SerializedName("play_store_url")
    private String play_store_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getApp_store_url() {
        return app_store_url;
    }

    public void setApp_store_url(String app_store_url) {
        this.app_store_url = app_store_url;
    }

    public String getPlay_store_url() {
        return play_store_url;
    }

    public void setPlay_store_url(String play_store_url) {
        this.play_store_url = play_store_url;
    }

}
