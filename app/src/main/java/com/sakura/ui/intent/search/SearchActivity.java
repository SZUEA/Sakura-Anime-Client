package com.sakura.ui.intent.search;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sakura.databinding.IntentSearchBinding;
import com.sakura.ui.home.ui.tab.adapter.AnimeListAdapter;

public class SearchActivity extends FragmentActivity {
    SearchViewModel searchViewModel;
    AnimeListAdapter animeListAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentSearchBinding binding=IntentSearchBinding.inflate(getLayoutInflater());
        EditText searchBox=binding.searchBox;
        searchBox.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchBox.requestFocus();

        animeListAdapter = new AnimeListAdapter(getBaseContext());
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getAnimeList().observeForever(animeListAdapter::addAllAnime);

        SwipeRefreshLayout swipe=binding.swipeView;
        GridView gridView  = binding.animeGridView;
        gridView.setAdapter(animeListAdapter);

        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                swipe.setRefreshing(true);
                animeListAdapter.dumpAllAnime();
                String searchText=searchBox.getText().toString();
                searchViewModel.search(searchText,swipe.getContext(),()->{
                    swipe.setRefreshing(false);
                });
//                new Thread(() -> {
//                    final InformationGetter informationGetter = new InformationGetter().search();
//                    itemView.post(() -> manga.setAdapter(new LoadingAdapter(itemView.getContext(), informationGetter.getImg(), informationGetter.getHref(), informationGetter.getTitle())));
//                    refresh.setRefreshing(false);
//                }).start();
            }
            return true;
        });
//        SearchViewModel mViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(SearchViewModel.class);
        setContentView(binding.getRoot());
    }
}