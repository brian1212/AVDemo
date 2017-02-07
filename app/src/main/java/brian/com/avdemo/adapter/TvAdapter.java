package brian.com.avdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import brian.com.avdemo.R;
import brian.com.avdemo.model.TvModel;
import butterknife.Bind;
import butterknife.ButterKnife;


public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder>  {

    private Context mContext;
    private List<TvModel> mListTv;
    OnItemClickListener onItemClickListener;

    public TvAdapter(Context mContext, List<TvModel> mListTv) {
        this.mContext = mContext;
        this.mListTv = mListTv;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListTv.size();
    }

    public void setOnTvModelClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnClick(TvModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.imv_item)
        ImageView imvItem;
        @Bind(R.id.tv_item)
        TextView tvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(onItemClickListener != null){
                        onItemClickListener.OnClick(mListTv.get(pos));
                    }
                }
            });
        }

        public void bind(int position) {
            tvItem.setText(mListTv.get(position).getName());
            if(position == 12){
                String url = mListTv.get(position).getThumbnailUrl();
                String newUrl = url.substring(1);
                Picasso.with(mContext).load(newUrl).transform(new RoundedCornersTransform()).into(imvItem);
            }else {
                Picasso.with(mContext).load(mListTv.get(position).getThumbnailUrl()).transform(new RoundedCornersTransform()).into(imvItem);
            }
        }
    }
    public class RoundedCornersTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 8f;
            canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "rounded_corners";
        }
    }
}
