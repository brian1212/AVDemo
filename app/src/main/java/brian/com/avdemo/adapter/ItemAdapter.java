package brian.com.avdemo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.realm.RealmHelper;
import brian.com.avdemo.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {
    OnItemClickListener itemClickListener;
    RealmHelper mRealmHelper = new RealmHelper();
    private Context mContext;
    private List<ItemModel> mItemModelList;
    private List<ItemModel> mFilterList;

    private int[] resouseId = {R.drawable.topic_1, R.drawable.topic_2, R.drawable.topic_3, R.drawable.topic_4, R.drawable.topic_5,
            R.drawable.topic_6, R.drawable.topic_7, R.drawable.topic_8, R.drawable.topic_9, R.drawable.topic_10,
            R.drawable.topic_11, R.drawable.topic_12, R.drawable.topic_13, R.drawable.topic_14, R.drawable.topic_15,
            R.drawable.topic_16, R.drawable.topic_17, R.drawable.topic_18, R.drawable.topic_19, R.drawable.topic_20,
            R.drawable.topic_21, R.drawable.topic_22, R.drawable.topic_23, R.drawable.topic_24, R.drawable.topic_25,
            R.drawable.topic_26, R.drawable.topic_27, R.drawable.topic_28, R.drawable.topic_29, R.drawable.topic_30,
            R.drawable.topic_31, R.drawable.topic_32, R.drawable.topic_33, R.drawable.topic_34, R.drawable.topic_35,
            R.drawable.topic_36, R.drawable.topic_37, R.drawable.topic_38, R.drawable.topic_39, R.drawable.topic_40,
            R.drawable.topic_41, R.drawable.topic_42, R.drawable.topic_43, R.drawable.topic_44, R.drawable.topic_45,
            R.drawable.topic_46, R.drawable.topic_47, R.drawable.topic_48, R.drawable.topic_49, R.drawable.topic_50,
            R.drawable.topic_51, R.drawable.topic_52, R.drawable.topic_53, R.drawable.topic_54, R.drawable.topic_55,
            R.drawable.topic_56, R.drawable.topic_57, R.drawable.topic_58, R.drawable.topic_59, R.drawable.topic_60};

    public ItemAdapter(Context mContext, List<ItemModel> mItemModelList) {
        this.mContext = mContext;
        this.mItemModelList = mItemModelList;
        this.mFilterList = mItemModelList;
        //set false all item isClicked
        for (ItemModel itemModel : mItemModelList) {
            if (mRealmHelper.getItemById(itemModel.getId()).isClicked()) {
                mRealmHelper.isClicked(itemModel.getId(), false);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mItemModelList == null) {
            return 0;
        } else {
            return mItemModelList.size();
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    //no search, so just return all the data
                    filterResults.count = mFilterList.size();
                    filterResults.values = mFilterList;
                } else {
                    List<ItemModel> resultData = new ArrayList<>();
                    //search in mFilterList
                    for (ItemModel item : mFilterList) {
                        if (Utils.filter(charSequence.toString().toLowerCase(), item.getEnglish().toLowerCase()) ||
                                Utils.filter(charSequence.toString().toLowerCase(), Utils.convertString(item.getVietnam().toLowerCase()))) {
                            resultData.add(item);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //mItemModelList: show up list
                mItemModelList = (List<ItemModel>) filterResults.values;
                for (ItemModel itemModel : mItemModelList) {
                    if (mRealmHelper.getItemById(itemModel.getId()).isClicked()) {
                        mRealmHelper.isClicked(itemModel.getId(), false);
                    }
                }
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface OnItemClickListener {
        void OnClick(ItemModel catModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imv)
        ImageView imv;
        @Bind(R.id.tv_english)
        TextView tvEng;
        @Bind(R.id.tv_vietnam)
        TextView tvVie;
        @Bind(R.id.imv_favorite)
        ImageView btnFavorite;
        @Bind(R.id.card_view)
        RelativeLayout cardView;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    if (itemClickListener != null) {
                        itemClickListener.OnClick(mItemModelList.get(pos));
                    }

                    for (int i = 0; i < mRealmHelper.fetchAllItemClicked().size(); i++) {
                        mRealmHelper.isClicked(mRealmHelper.fetchAllItemClicked().get(i).getId(), false);
                    }
                    mRealmHelper.isClicked(mItemModelList.get(pos).getId(), true);

                    notifyDataSetChanged();
                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mRealmHelper.getItemById(mItemModelList.get(getAdapterPosition()).getId()).isChecked()) {
                        mRealmHelper.isChecked(mItemModelList.get(getAdapterPosition()).getId(), true);
                        btnFavorite.setImageResource(R.drawable.remove_selector);
                        if (mRealmHelper.getItemById(mItemModelList.get(getAdapterPosition()).getId()).isClicked()) {
                            mRealmHelper.isClicked(mItemModelList.get(getAdapterPosition()).getId(), false);
                        }
                        EventBus.getDefault().post("add");
                    } else {
                        mRealmHelper.isChecked(mItemModelList.get(getAdapterPosition()).getId(), false);
                        btnFavorite.setImageResource(R.drawable.button_selector);
                        EventBus.getDefault().post("remove");
//                        notifyDataSetChanged();
                    }
                }
            });
        }

        public void bind(int position) {
            if (TextUtils.isEmpty(mItemModelList.get(position).getCatId())) {
                imv.setImageResource(resouseId[position]);
                tvEng.setText(mItemModelList.get(position).getEnglish());
                tvVie.setText(mItemModelList.get(position).getVietnam());
            } else {
                if (mRealmHelper.getItemById(mItemModelList.get(position).getId()).isChecked()) {
                    btnFavorite.setImageResource(R.drawable.remove_selector);
                } else {
                    btnFavorite.setImageResource(R.drawable.button_selector);
                }
                btnFavorite.setVisibility(View.VISIBLE);
                tvEng.setText(mItemModelList.get(position).getVietnam());
                tvVie.setText(mItemModelList.get(position).getEnglish());
            }

            if (mRealmHelper.getItemById(mItemModelList.get(position).getId()).isClicked()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            }


        }
    }
}
