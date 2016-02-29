package com.ym.xsgame.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ym.xsgame.R;
import com.ym.xsgame.po.Bander;
import com.ym.xsgame.po.Result;
import com.ym.xsgame.util.IntentUtils;
import com.ym.xsgame.widget.AutoScrollViewPager.AutoScrollViewPager;
import com.ym.xsgame.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int Header = 0;
    private static final int ITEM = 1;

    private ArrayList<Bander.ReturnDataEntity.DataEntity> bander;
    public ArrayList<Result.ReturnDataEntity.DataEntity> data;

    public GameListAdapter(ArrayList<Bander.ReturnDataEntity.DataEntity> bander, ArrayList<Result.ReturnDataEntity.DataEntity> data) {
        this.bander = bander;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Header) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_header, parent, false);
            return new VHHeader(v);
        } else if (viewType == ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_data_item, parent, false);
            return new ViewHolder(v);
        } else
            throw new RuntimeException("Could not inflate layout");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            if (bander == null) {
                ((VHHeader) holder).bannerLayout.setVisibility(View.GONE);
                return;
            } else {
                ((VHHeader) holder).bannerLayout.setVisibility(View.VISIBLE);
            }
            ((VHHeader) holder).autoScrollViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return bander.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View view = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_viewpager_item, null);
                    final ImageView photoView = (ImageView) view.findViewById(R.id.photo_viewpager_item_image);
                    Glide.with(container.getContext())
                            .load(bander.get(position).getSimg())
                            .fitCenter()
                            .override(Target.SIZE_ORIGINAL, 300)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .crossFade()
                            .into(photoView);
                    container.addView(view);
                    return view;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });
            if (bander.size() > 1) {
                ((VHHeader) holder).indicator.setViewPager(((VHHeader) holder).autoScrollViewPager);
                ((VHHeader) holder).indicator.setSnap(true);
                ((VHHeader) holder).autoScrollViewPager.setScrollFactgor(5);
                ((VHHeader) holder).autoScrollViewPager.setOffscreenPageLimit(4);
                ((VHHeader) holder).autoScrollViewPager.startAutoScroll(4000);
                ((VHHeader) holder).autoScrollViewPager.startAutoScroll();

                ((VHHeader) holder).autoScrollViewPager.setOnPageClickListener(new AutoScrollViewPager.OnPageClickListener() {
                    @Override
                    public void onPageClick(AutoScrollViewPager autoScrollPager, int position) {
//                        IntentUtils.enterWebViewActivity(container.ge, ads.get(position).getTitle(), ads.get(position).getUrl());
                    }
                });
            }

        } else if (holder instanceof ViewHolder) {
            holder.itemView.setPadding(0, 0, 0, 20);
            final Result.ReturnDataEntity.DataEntity dt;
            if (data != null && data.size() > 0) {
                dt = data.get(position - 1);
            } else {
                dt = data.get(position);
            }
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
            ((ViewHolder) holder).gift.setText(dt.getIisgift()+"个礼包");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    "/api/syxs/sgame/begin.php?gameid=200243&amp;ty=0"
                    String url= "http://xs.qidian.com/api/syxs/sgame/begin.php?gameid="+ dt.getIgameid() + "&ty=0";
                    if(dt.getItype()==2){
//                        if(androidqd.checkInstalled(spackage)){
//                            url="javascript:gameList.openGame('"+spackage+"',"+gameid+");";
//                            startTag="打开";
//                        }else{
//                            //url=surl;
//                            url="javascript:gameList.downGame('"+gameid+"','1','"+surl+"');";
//                            startTag="下载";
//                        }
                    }else{
//                        startTag="进入游戏";
                    }


                    if(dt.getItype()==1){//H5游戏

                        IntentUtils.enterWebViewActivity(holder.itemView.getContext(),dt.getSname(),url);
                    }else if(dt.getItype()==2){//需要下载的手游

                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (bander != null && bander.size() > 0) {
            return data == null ? 1 : data.size() + 1;
        } else {
            return data == null ? 0 : data.size();
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (bander != null && bander.size() > 0) {
            if (position == 0) {
                return Header;
            } else {
                return ITEM;
            }
        } else {
            return ITEM;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Result.ReturnDataEntity.DataEntity string) {
        insert(string, data.size());
    }

    public void insert(Result.ReturnDataEntity.DataEntity string, int position) {
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

    public void setBanner(List<Bander.ReturnDataEntity.DataEntity> strings) {
        bander = (ArrayList<Bander.ReturnDataEntity.DataEntity>) strings;
        notifyDataSetChanged();
    }

    public void addAll(List<Result.ReturnDataEntity.DataEntity> strings) {
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

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.goods_name);
            this.description = (TextView) view.findViewById(R.id.goods_des);
            this.icon = (ImageView) view.findViewById(R.id.goods_img);
            this.gift = (TextView) view.findViewById(R.id.goods_price);
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'home_header.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class VHHeader extends RecyclerView.ViewHolder

    {
        @Bind(R.id.viewPager)
        AutoScrollViewPager autoScrollViewPager;
        @Bind(R.id.banner_layout)
        FrameLayout bannerLayout;

        @Bind(R.id.indicator)
        CirclePageIndicator indicator;

        VHHeader(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
