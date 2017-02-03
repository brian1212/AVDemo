package brian.com.avdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.adapter.ItemAdapter;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class FavoriteFragment extends BaseFragment {

    @Bind(R.id.tv_favorite)
    TextView tvFavorite;
    @Bind(R.id.recycler_view_favorite)
    RecyclerView mRecyclerView;

    List<ItemModel> mList = new ArrayList<>();
    ItemAdapter adapter;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getRealm().fetchAllItemChecked();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFavorite.setText(getString(R.string.lbl_favorite));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        for (ItemModel itemModel : mList) {
//            if (getRealm().getItemById(itemModel.getId()).isClicked()) {
//                getRealm().isClicked(itemModel.getId(), false);
//            }
//        }
        adapter = new ItemAdapter(getContext(), mList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void OnClick(ItemModel catModel) {
                Utils.playSound(getContext(), catModel.getId());
            }
        });
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals("remove")) {
            adapter.notifyDataSetChanged();
        } else if (event.equals("add")) {
            mList = getRealm().fetchAllItemChecked();
            adapter.notifyDataSetChanged();
        }
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
}
