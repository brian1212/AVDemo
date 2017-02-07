package brian.com.avdemo.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class TvModel extends RealmObject {

    @SerializedName("name")
    private String name;
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;
    @SerializedName("app_store_url")
    private String appStoreUrl;
    @SerializedName("play_store_url")
    private String playStoreUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAppStoreUrl() {
        return appStoreUrl;
    }

    public void setAppStoreUrl(String appStoreUrl) {
        this.appStoreUrl = appStoreUrl;
    }

    public String getPlayStoreUrl() {
        return playStoreUrl;
    }

    public void setPlayStoreUrl(String playStoreUrl) {
        this.playStoreUrl = playStoreUrl;
    }

}
