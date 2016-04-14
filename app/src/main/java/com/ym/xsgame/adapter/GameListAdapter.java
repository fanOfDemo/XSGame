package com.ym.xsgame.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ym.xsgame.R;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.common.AppUtils;
import com.ym.xsgame.widget.AutoScrollViewPager.AutoScrollViewPager;
import com.ym.xsgame.widget.CirclePageIndicator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    private ArrayList<Bander.ReturnDataEntity.BannerGame> mBannerGames;
    public ArrayList<Result.ReturnDataEntity.GameData> data;

    public GameListAdapter(ArrayList<Result.ReturnDataEntity.GameData> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == Header) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_header, parent, false);
//            return new VHHeader(v);
//        } else
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_data_item, parent, false);
            return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            holder.itemView.setPadding(0, 0, 0, 20);
            final Result.ReturnDataEntity.GameData dt;
            dt = data.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(dt.getSicon())
                    .fitCenter()
//                    .placeholder(R.drawable.image_loding)
                    .override(Target.SIZE_ORIGINAL, 200)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(((ViewHolder) holder).icon);

            ((ViewHolder) holder).name.setText(dt.getSname());
            ((ViewHolder) holder).description.setText(dt.getSintroduction());
            ((ViewHolder) holder).gift.setText(dt.getIisgift() + "个礼包");
            final String packageName = dt.getSpackage();
            if (dt.getItype() == 1) {//H5游戏
                ((ViewHolder) holder).aciton.setText("go");
            } else if (dt.getItype() == 2) {//需要下载的手游
                if (AppUtils.checkInstalled(holder.itemView.getContext(), packageName)) {
                    ((ViewHolder) holder).aciton.setText("打开");
                } else {
                    ((ViewHolder) holder).aciton.setText("下载");
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    "/api/syxs/sgame/begin.php?gameid=200243&amp;ty=0"
                    if (dt.getItype() == 1) {//H5游戏
                        ((ViewHolder) holder).aciton.setText("go");
                        mGameClickListener.onPageGameClick(dt);
                    } else if (dt.getItype() == 2) {//需要下载的手游
                        if (AppUtils.checkInstalled(holder.itemView.getContext(), packageName)) {
                            mGameClickListener.onApkGameOpenClick(dt);
//                            url="javascript:gameList.openGame('"+spackage+"',"+gameid+");";
//                            startTag="打开";
                            ((ViewHolder) holder).aciton.setText("打开");
                        } else {
                            mGameClickListener.onApkGameDownLoadClick(dt);
                            ((ViewHolder) holder).aciton.setText("下载");
//                            //url=surl;
//                            url="javascript:gameList.downGame('"+gameid+"','1','"+surl+"');";
//                            startTag="下载";
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Result.ReturnDataEntity.GameData string) {
        insert(string, data.size());
    }

    public void insert(Result.ReturnDataEntity.GameData string, int position) {
        data.add(position, string);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

//    public void setBanner(List<Bander.ReturnDataEntity.BannerGame> strings) {
//        mBannerGames = (ArrayList<Bander.ReturnDataEntity.BannerGame>) strings;
//        notifyDataSetChanged();
//    }

    public void addAll(List<Result.ReturnDataEntity.GameData> strings) {
        int startIndex = data.size();
        data.addAll(startIndex, strings);
        notifyItemRangeInserted(startIndex, strings.size());
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;
        protected ImageView icon;
        protected TextView gift;
        private TextView aciton;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.goods_name);
            this.description = (TextView) view.findViewById(R.id.goods_des);
            this.icon = (ImageView) view.findViewById(R.id.goods_img);
            this.gift = (TextView) view.findViewById(R.id.goods_price);
            this.aciton = (TextView) view.findViewById(R.id.aciton);
        }
    }

    public void setGameClickListener(OnGameClickListener gameClickListener) {
        mGameClickListener = gameClickListener;
    }

    OnGameClickListener mGameClickListener;

    public interface OnGameClickListener {
        void onPageGameClick(Result.ReturnDataEntity.GameData gameData);

        void onApkGameOpenClick(Result.ReturnDataEntity.GameData gameData);

        void onApkGameDownLoadClick(Result.ReturnDataEntity.GameData gameData);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file
     * 'home_header.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers
     *         (http://github.com/avast)
     */
    static class VHHeader extends RecyclerView.ViewHolder {
        AutoScrollViewPager autoScrollViewPager;
        FrameLayout bannerLayout;
        CirclePageIndicator indicator;

        VHHeader(View view) {
            super(view);


            autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
            bannerLayout = (FrameLayout) view.findViewById(R.id.banner_layout);
            indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        }
    }
}
