package brian.com.avdemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.realm.RealmHelper;
import brian.com.avdemo.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    RealmHelper mRealmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mRealmHelper = new RealmHelper();
        new LoadDataTask().execute();
    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<ItemModel>> {
        List<ItemModel> mList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setContentView(R.layout.activity_splash);
        }

        @Override
        protected List<ItemModel> doInBackground(Void... voids) {
            //insert list category
            List<ItemModel> mCatList = Utils.loadJSONFromAsset(getBaseContext(), "category", "category");
            for (ItemModel item : mCatList) {
                mList.add(item);
            }
            //insert list child
            for (int i = 1; i <= mCatList.size(); i++) {
                List<ItemModel> mChildList = Utils.loadJSONFromAsset(getBaseContext(), "jsons", String.valueOf(i));
                for (ItemModel item : mChildList) {
                    mList.add(item);
                }
            }

            return mList;
        }

        @Override
        protected void onPostExecute(List<ItemModel> mList) {
            if (mRealmHelper.fetchAllItem().size() == 0) {
                for (ItemModel item : mList) {
                    mRealmHelper.insertItem(item);
                }
            }
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
