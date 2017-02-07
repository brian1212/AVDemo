package brian.com.avdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import brian.com.avdemo.ApiService;
import brian.com.avdemo.R;
import brian.com.avdemo.RetroClient;
import brian.com.avdemo.adapter.TvAdapter;
import brian.com.avdemo.model.AppsList;
import brian.com.avdemo.model.TvModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;


public class InfoFragment extends BaseFragment {

    @Bind(R.id.tv_label_info)
    TextView tvInfo;
    @Bind(R.id.recycler_view_info)
    RecyclerView mRecyclerView;
    List<TvModel> mItemList = new ArrayList<>();
    private TvAdapter adapter;

    //use volley
//    public static final String URL = "http://vinova.sg/mobile-apps/tuvi.json";

    public InfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, v);
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, URL,
//                new Response.Listener<JSONObject>() {
//                    // Takes the response from the JSON request
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray array = response.getJSONArray("apps");
//                            Gson gson = new Gson();
//                            mItemList = Arrays.asList(gson.fromJson(array.toString(), TvModel[].class));
//                            if (getRealm().fetchAllTv().size() == 0) {
//                                for (TvModel model : mItemList) {
//                                    getRealm().insertTv(model);
//                                }
//                            }
//                            fillData();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley", "Error");
//                    }
//                }
//        );
//        requestQueue.add(obreq);

        //Creating an object of our api interface
        ApiService api = RetroClient.getApiService();
        Call<AppsList> call = api.getMyJSON();
        call.enqueue(new Callback<AppsList>() {
            @Override
            public void onResponse(Call<AppsList> call, retrofit2.Response<AppsList> response) {
                mItemList = response.body().getApps();
                if (getRealm().fetchAllTv().size() == 0) {
                    for (TvModel model : mItemList) {
                        getRealm().insertTv(model);
                    }
                }
                fillData();
            }

            @Override
            public void onFailure(Call<AppsList> call, Throwable t) {

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvInfo.setText(getString(R.string.lbl_owner));
        fillData();
    }

    public void fillData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new TvAdapter(getContext(), getRealm().fetchAllTv());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnTvModelClickListener(new TvAdapter.OnItemClickListener() {
            @Override
            public void OnClick(TvModel model) {
                if (!TextUtils.isEmpty(model.getPlayStoreUrl())) {
                    Uri uri = Uri.parse(model.getPlayStoreUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri = Uri.parse(model.getAppStoreUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.tv_website)
    public void clickWebsite() {
        Uri uri = Uri.parse("http://vinova.sg/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.tv_mail)
    public void clickEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "info@vinova.sg", null));
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    @OnClick(R.id.btn_rate)
    public void clickRate() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=sg.vinova.evdialog");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
