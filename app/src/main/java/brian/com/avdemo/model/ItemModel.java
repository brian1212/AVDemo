package brian.com.avdemo.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ItemModel extends RealmObject {

    @SerializedName("id")
    private int id;
    @SerializedName("cat_id")
    private String cat_id;
    @SerializedName("e")
    private String english;
    @SerializedName("v")
    private String vietnam;

    private boolean isChecked;


    private boolean isClicked;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnam() {
        return vietnam;
    }

    public void setVietnam(String vietnam) {
        this.vietnam = vietnam;
    }
}
