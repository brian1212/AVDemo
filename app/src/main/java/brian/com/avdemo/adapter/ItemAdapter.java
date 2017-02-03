package brian.com.avdemo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.model.ItemModel;
import brian.com.avdemo.realm.RealmHelper;
import butterknife.Bind;
import butterknife.ButterKnife;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    OnItemClickListener itemClickListener;
    RealmHelper mRealmHelper = new RealmHelper();
    private Context mContext;
    private List<ItemModel> mCatList;
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

    public ItemAdapter(Context mContext, List<ItemModel> mCatList) {
        this.mContext = mContext;
        //mCatlist truyen vao lay tu Realm nen minh copy ra 1 ban de lam
//        this.mCatList = Realm.getDefaultInstance().copyFromRealm(mCatList);
        this.mCatList = mCatList;
        for (ItemModel itemModel : mCatList) {
            if (mRealmHelper.getItemById(itemModel.getId()).isClicked()) {
                mRealmHelper.isClicked(itemModel.getId(), false);
            }
        }

    }

//    public void updateAdapter(List<ItemModel> mCatList) {
//        this.mCatList = Realm.getDefaultInstance().
//        this.mCatList = mCatList;
//    }

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
        return mCatList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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

//        RealmHelper mRealmHelper;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mRealmHelper = new RealmHelper();

//            for (int i = 0; i < mRealmHelper.fetchAllItemClicked().size(); i++) {
//                mRealmHelper.isClicked(mRealmHelper.fetchAllItemClicked().get(i).getId(), false);
//            }
//            for (ItemModel itemModel : mCatList) {
//                if(mRealmHelper.getItemById(itemModel.getId()).isClicked()){
//                    mRealmHelper.isClicked(itemModel.getId(),false);
//                }
//            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    if (itemClickListener != null) {
                        itemClickListener.OnClick(mCatList.get(pos));
                    }


//                    for (ItemModel model : mCatList) {
//                        model.setClicked(false);
//                    }
//                    mCatList.get(pos).setClicked(true);

//                    if (mRealmHelper.fetchAllItemClicked().size() != 0) {
//                        for (int i = 0; i < mCatList.size(); i++) {
//                            if (mCatList.get(i).getId() == mRealmHelper.fetchAllItemClicked().get(0).getId()) {
//                                mRealmHelper.isClicked(mCatList.get(i).getId(), false);
//                                break;
//                            }
//                        }
//                    }
//                    for (ItemModel itemModel : mCatList) {
//                        if(mRealmHelper.getItemById(itemModel.getId()).isClicked()){
//                            mRealmHelper.isClicked(itemModel.getId(),false);
//                        }
//                    }

                    for (int i = 0; i < mRealmHelper.fetchAllItemClicked().size(); i++) {
                        mRealmHelper.isClicked(mRealmHelper.fetchAllItemClicked().get(i).getId(), false);
                    }
                    mRealmHelper.isClicked(mCatList.get(pos).getId(), true);

                    notifyDataSetChanged();
                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mRealmHelper.getItemById(mCatList.get(getAdapterPosition()).getId()).isChecked()) {
                        mRealmHelper.isChecked(mCatList.get(getAdapterPosition()).getId(), true);
                        btnFavorite.setImageResource(R.drawable.remove_selector);
                        if (mRealmHelper.getItemById(mCatList.get(getAdapterPosition()).getId()).isClicked()) {
                            mRealmHelper.isClicked(mCatList.get(getAdapterPosition()).getId(), false);
                        }
                        EventBus.getDefault().post("add");
                    } else {
                        mRealmHelper.isChecked(mCatList.get(getAdapterPosition()).getId(), false);
                        btnFavorite.setImageResource(R.drawable.button_selector);
                        EventBus.getDefault().post("remove");
//                        notifyDataSetChanged();
                    }
                }
            });
        }

        public void bind(int position) {
            if (TextUtils.isEmpty(mCatList.get(position).getCat_id())) {
                imv.setImageResource(resouseId[position]);
                tvEng.setText(mCatList.get(position).getEnglish());
                tvVie.setText(mCatList.get(position).getVietnam());
            } else {
                if (mRealmHelper.getItemById(mCatList.get(position).getId()).isChecked()) {
                    btnFavorite.setImageResource(R.drawable.remove_selector);
                } else {
                    btnFavorite.setImageResource(R.drawable.button_selector);
                }
                btnFavorite.setVisibility(View.VISIBLE);
                tvEng.setText(mCatList.get(position).getVietnam());
                tvVie.setText(mCatList.get(position).getEnglish());
            }

            if (mRealmHelper.getItemById(mCatList.get(position).getId()).isClicked()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            }


        }
    }
}
