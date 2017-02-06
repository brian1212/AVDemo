package brian.com.avdemo.fragment;

import android.support.v4.app.Fragment;

import java.util.List;

import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.realm.RealmHelper;
import brian.com.avdemo.utils.Utils;


public class BaseFragment extends Fragment {


    public RealmHelper getRealm() {
        return new RealmHelper();
    }

}
