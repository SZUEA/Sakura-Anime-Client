package com.sakura.ui.home.ui.tab.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sakura.R;
import com.sakura.databinding.FragmentAnimelistBinding;
import com.sakura.ui.home.ui.tab.adapter.AnimeListAdapter;

public class AnimeListFragment extends Fragment {

    private final int mode;

    public AnimeListFragment(int mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        FragmentAnimelistBinding binding = FragmentAnimelistBinding.inflate(inflater, container, false);
        SwipeRefreshLayout swipe_view = binding.swipeView;
        GridView anime_gridview = binding.animeGridView1;
        swipe_view.setRefreshing(true);

        swipe_view.setColorSchemeResources(R.color.pink_700);


        AnimeListAdapter animeListAdapter = new AnimeListAdapter(getContext());


        AnimeListViewModel mViewModel = new ViewModelProvider(this).get(AnimeListViewModel.class);
        mViewModel.getAnimeList().observe(getViewLifecycleOwner(), animeListAdapter::addAllAnime);
        mViewModel.fetch(mode, 1, getContext(), () -> swipe_view.setRefreshing(false));
        anime_gridview.setAdapter(animeListAdapter);

        anime_gridview.setOnScrollListener(new GridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            int ci2 = 0;

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (ci2 != i2 && i2 - i <= 11) {
                    swipe_view.setRefreshing(true);
                    mViewModel.fetch(
                            mode, 1 + i2 / 15, getContext()
                            , () -> swipe_view.setRefreshing(false)
                    );
                    ci2 = i2;
                }
            }
        });

        swipe_view.setOnRefreshListener(() -> {
            swipe_view.setRefreshing(true);
            animeListAdapter.dumpAllAnime();
            mViewModel.fetch(mode, 1, getContext(), () -> swipe_view.setRefreshing(false));
        });


        return binding.getRoot();
    }


}