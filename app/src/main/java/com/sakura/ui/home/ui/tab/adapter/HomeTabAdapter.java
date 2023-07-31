package com.sakura.ui.home.ui.tab.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sakura.request.Request;
import com.sakura.ui.home.ui.tab.list.AnimeListFragment;

public class HomeTabAdapter extends FragmentStateAdapter {
    Fragment[] tabs=new Fragment[]{
            new AnimeListFragment(Request.MODE_JAPAN),
            new AnimeListFragment(Request.MODE_CHINA)
    };

    public HomeTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return tabs[position];
    }

    @Override
    public int getItemCount() {
        return tabs.length;
    }

    @Override
    public long getItemId(int position) {
        return tabs[position].hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return tabs[0].hashCode() == itemId || tabs[1].hashCode() == itemId;
    }
}