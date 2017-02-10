package brian.com.avdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import brian.com.avdemo.R;
import brian.com.avdemo.adapter.ItemAdapter;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchFragment extends BaseFragment {

    @Bind(R.id.tv_label_search)
    TextView tvSearch;
    @Bind(R.id.recyler_view_search)
    RecyclerView mRecyclerView;
    @Bind(R.id.search_view)
    SearchView searchView;

    ItemAdapter itemAdapter;
    TextView txtSearch;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSearch.setText(R.string.lbl_search);
        txtSearch = ((TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        itemAdapter = new ItemAdapter(getContext(),getRealm().fetchAllChildItem());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(Utils.convertString(newText));

                mRecyclerView.setAdapter(itemAdapter);
                itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(ItemModel catModel) {
                        Utils.playSound(getContext(), catModel.getId());
                    }
                });
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals("remove")) {
            itemAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.btn_search)
    public void onClickClear() {
        txtSearch.setText("");
        itemAdapter.getFilter().filter("");
    }

}
