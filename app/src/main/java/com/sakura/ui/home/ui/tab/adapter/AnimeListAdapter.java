package com.sakura.ui.home.ui.tab.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakura.R;
import com.sakura.databinding.LayoutItemAnimeBinding;
import com.sakura.request.ImageLoader;
import com.sakura.types.AnimeItem;

import java.util.ArrayList;
import java.util.List;

public class AnimeListAdapter extends BaseAdapter {

    private final List<AnimeItem> animeList = new ArrayList<>();
    private final Context context;

    public AnimeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return animeList.size();
    }

    @Override
    public Object getItem(int i) {
        return animeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if (view == null)
            v = LayoutInflater.from(context).inflate(R.layout.layout_item_anime, null);
        else v = view;
        LayoutItemAnimeBinding binding=LayoutItemAnimeBinding.bind(v);

        final AnimeItem animeItem = animeList.get(i);

        final ImageView itemImage = binding.animeItemImage;
        final TextView itemTitle = binding.animeItemTitle;
        final TextView itemDescription = binding.animeItemDescription;

        ImageLoader.invokeTop(itemImage.getContext(), itemImage, animeItem.img);
        itemTitle.setText(animeItem.title);
        itemDescription.setText(animeItem.state);

        return binding.getRoot();

    }

    public void addAllAnime(List<AnimeItem> animeList) {
        if (animeList == null) return;
        this.animeList.addAll(animeList);
        notifyDataSetChanged();
    }

    public void dumpAllAnime(){
        this.animeList.clear();
    }

}
