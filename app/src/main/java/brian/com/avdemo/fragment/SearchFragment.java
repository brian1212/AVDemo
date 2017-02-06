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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                itemAdapter = new ItemAdapter(getContext(), search(newText));

//                itemAdapter.notifyDataSetChanged();
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
    public void onEvent(String event){
        if(event.equals("remove")){
            itemAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.btn_search)
    public void onClickClear() {
        txtSearch.setText("");
//        itemAdapter.updateAdapter(search(""));
//        mRecyclerView.setAdapter(itemAdapter);
    }

    public boolean filter(String search, String text) {
        List<String> tokensText = new ArrayList<>();
        List<String> tokensSearch = new ArrayList<>();

        String newText = text.substring(0, text.length() - 1);
        StringTokenizer stText = new StringTokenizer(newText);
        //("---- Split by space ------");
        while (stText.hasMoreElements()) {
            tokensText.add(stText.nextElement().toString());
        }

        StringTokenizer stSearch = new StringTokenizer(search);
        //("---- Split by space ------");
        while (stSearch.hasMoreElements()) {
            tokensSearch.add(stSearch.nextElement().toString());
        }

        if (tokensSearch.size() < 2) {
            for (int i = 0; i < tokensText.size(); i++) {
                if (tokensText.get(i).equals(search)) {
                    return true;
                }
            }
        } else {
            if(newText.contains(search)){
                return true;
            }
        }

        return false;
    }

    public List<ItemModel> search(String keyWord) {
//        List<String> tokens = new ArrayList<>();
//        StringTokenizer st = new StringTokenizer(keyWord);
//        while (st.hasMoreElements()) {
//            tokens.add(st.nextElement().toString());
//        }

        List<ItemModel> mListItem = new ArrayList<>();

        for (ItemModel item : getRealm().fetchAllChildItem()) {
//            if (tokens.size() < 2) {
            if (filter(keyWord.toLowerCase(), item.getEnglish().toLowerCase()) ||
                    filter(keyWord.toLowerCase(), Utils.convertString(item.getVietnam().toLowerCase()))) {
//                if (!filterSearch(mListItem, item)) {
                    mListItem.add(item);
//                }
            }
//            } else {


//                for (String s : tokens) {
//                    if (filter(s.toLowerCase(), item.getEnglish().toLowerCase()) ||
//                            filter(s.toLowerCase(), Utils.convertString(item.getVietnam().toLowerCase()))) {
//                        if (!filterSearch(mListItem, item)) {
//                            mListItem.add(item);
//                        }
//                    }
//                }
//            }
        }
//        for (ItemModel itemModel : mListItem) {
//            if(getRealm().getItemById(itemModel.getId()).isClicked()){
//                getRealm().isClicked(itemModel.getId(),false);
//            }
//        }
        return mListItem;
    }

//    public boolean filterSearch(List<ItemModel> list, ItemModel item) {
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getId() == item.getId()) {
//                return true;
//            }
//        }
//        return false;
//    }

}
