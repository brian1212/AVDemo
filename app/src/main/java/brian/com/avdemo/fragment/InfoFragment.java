package brian.com.avdemo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.adapter.TvAdapter;
import brian.com.avdemo.model.TvModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoFragment extends BaseFragment {

    @Bind(R.id.tv_label_info)
    TextView tvInfo;
    @Bind(R.id.recycler_view_info)
    RecyclerView mRecyclerView;
    List<TvModel> mItemList = new ArrayList<>();
    TvAdapter adapter;

    public InfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, v);
//        getRealm().deleteAllTv();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, URL,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("apps");
                            Gson gson = new Gson();
                            mItemList = Arrays.asList(gson.fromJson(array.toString(), TvModel[].class));
                            if (getRealm().fetchAllTv().size() == 0) {
                                for (TvModel model : mItemList) {
                                    getRealm().insertTv(model);
                                }
                            }
                            fillData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(obreq);
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
                if (!TextUtils.isEmpty(model.getPlay_store_url())) {
                    Uri uri = Uri.parse(model.getPlay_store_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri = Uri.parse(model.getApp_store_url());
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
