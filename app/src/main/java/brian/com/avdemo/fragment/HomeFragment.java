package brian.com.avdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.adapter.ItemAdapter;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.child_recycler_view)
    RecyclerView mChildRecyclerView;
    @Bind(R.id.tv_label_cat)
    TextView tvLabelCat;
    @Bind(R.id.tv_label_child)
    TextView tvLabelChild;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.imv_previous)
    ImageView imvPrevious;

    private ItemAdapter itemAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //load all data in first time
        if (getRealm().fetchAllItem().size() == 0) {
            List<ItemModel> mCatList = Utils.loadJSONFromAsset(getContext(), "category", "category");
            for (ItemModel item : mCatList) {
                getRealm().insertItem(item);
            }
            for (int i = 1; i <= mCatList.size(); i++) {
                List<ItemModel> mChildList = Utils.loadJSONFromAsset(getContext(), "jsons", String.valueOf(i));
                if (mChildList != null && mChildList.size() != 0) {
                    for (ItemModel item : mChildList) {
                        getRealm().insertItem(item);
                    }
                }
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvLabelCat.setText(R.string.lbl_topic);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mChildRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        itemAdapter = new ItemAdapter(getContext(), getRealm().fetchAllItemByCat(null));
        mRecyclerView.setAdapter(itemAdapter);

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void OnClick(final ItemModel catModel) {
                EventBus.getDefault().post("drawer");

                tvLabelChild.setText(catModel.getVietnam());

                List<ItemModel> mChildList = getRealm().fetchAllItemByCat(String.valueOf(catModel.getId()));

                //animation drawer open
                mDrawerLayout.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(mDrawerLayout, new Slide(Gravity.END));

                itemAdapter = new ItemAdapter(getContext(), mChildList);
                mChildRecyclerView.setAdapter(itemAdapter);
                itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(ItemModel childModel) {
                        Utils.playSound(getContext(), childModel.getId());
                    }
                });
            }
        });
        imvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //animation drawer close
                TransitionManager.beginDelayedTransition(mDrawerLayout, new Slide(Gravity.END));
                mDrawerLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals("back")) {
            if (mDrawerLayout != null && mDrawerLayout.getVisibility() == View.VISIBLE) {
                //animation drawer close
                TransitionManager.beginDelayedTransition(mDrawerLayout, new Slide(Gravity.END));
                mDrawerLayout.setVisibility(View.INVISIBLE);
            }
        } else if (event.equals("remove")) {
            itemAdapter.notifyDataSetChanged();
        }
    }
}
