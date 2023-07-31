package com.sakura.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sakura.R;
import com.sakura.databinding.FragmentHomeBinding;
import com.sakura.ui.home.ui.tab.adapter.HomeTabAdapter;
import com.sakura.ui.intent.search.SearchActivity;

import java.lang.reflect.Field;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HomeTabAdapter sectionsPagerAdapter = new HomeTabAdapter(this);
        ViewPager2 viewPager = binding.viewPager2;

        try {
            Field recyclerViewField = ViewPager2.class.getDeclaredField("mRecyclerView");
            recyclerViewField.setAccessible(true);
            RecyclerView r = (RecyclerView) recyclerViewField.get(viewPager);
            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
            touchSlopField.setAccessible(true);
            Integer touchSlop = (Integer) Objects.requireNonNull(touchSlopField.get(r));
            touchSlopField.set(r, touchSlop * 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 防止切换选项卡后重新回退到顶部
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, false, true,
                (tab, position) -> tab.setText(position == 0 ? R.string.tab_text_1 : R.string.tab_text_2)).attach();

        FloatingActionButton invokeSearch=binding.invokeSearch;
        invokeSearch.setOnClickListener(view -> {
            Intent searchIntent=new Intent();
            searchIntent.setClass(getContext(), SearchActivity.class);
            searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            invokeSearch.getContext().startActivity(searchIntent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}