package brian.com.avdemo.fragment;

import android.support.v4.app.Fragment;

import java.util.List;

import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.realm.RealmHelper;
import brian.com.avdemo.utils.Utils;


public class BaseFragment extends Fragment {

    public static final String URL = "http://vinova.sg/mobile-apps/tuvi.json";

    public RealmHelper getRealm() {
        return new RealmHelper();
    }

    public List<ItemModel> getCatList() {
        List<ItemModel> mCatList = Utils.loadJSONFromAsset(getContext(), "category", "category");
        return mCatList;
    }

    public void getAllItem() {
        for (ItemModel item : getCatList()) {
            getRealm().insertItem(item);
        }
        for (int i = 1; i <= getCatList().size(); i++) {
            List<ItemModel> mChildList = Utils.loadJSONFromAsset(getContext(), "jsons", String.valueOf(i));
            if (mChildList != null && mChildList.size() != 0) {
                for (ItemModel item : mChildList) {
                    getRealm().insertItem(item);
                }
            }
        }
    }


}
