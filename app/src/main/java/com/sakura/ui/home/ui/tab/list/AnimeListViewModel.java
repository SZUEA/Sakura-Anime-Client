/**
 * JapanViewModel is a ViewModel class that manages data and business logic for the Japan anime list page.
 */
package com.sakura.ui.home.ui.tab.list;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sakura.request.Request;
import com.sakura.types.AnimeItem;

import java.util.List;

public class AnimeListViewModel extends ViewModel {

    /**
     * mAnimeList is a MutableLiveData object that stores the Japan anime list data.
     */
    private final MutableLiveData<List<AnimeItem>> mAnimeList;

    /**
     * Constructor for initializing mAnimeList.
     */
    public AnimeListViewModel() {
        mAnimeList = new MutableLiveData<>();
    }

    /**
     * getAnimeList method returns the mAnimeList object.
     *
     * @return Returns a LiveData<List<AnimeItem>> object representing the Japan anime list data.
     */
    public LiveData<List<AnimeItem>> getAnimeList() {
        return mAnimeList;
    }

    /**
     * fetch method is used to retrieve the Japan anime list data from the server and store it in mAnimeList.
     *
     * @param mode     The request mode.
     * @param position The starting position of the request.
     * @param context  The context object used to display error messages.
     */
    public void fetch(int mode, int position, Context context) {
        new Thread(() -> {
            List<AnimeItem> animeItems = Request.getAnimeList(mode, position);
            if (animeItems.size()==0)
                Request.badResponse(context);
            mAnimeList.postValue(animeItems);
        }).start();
    }

    /**
     * fetch method is used to retrieve the Japan anime list data from the server and store it in mAnimeList.
     * The onFinish method will be executed after the data is retrieved.
     *
     * @param mode     The request mode.
     * @param position The starting position of the request.
     * @param context  The context object used to display error messages.
     * @param onFinish The callback method to be executed after data retrieval is completed.
     */
    public void fetch(int mode, int position, Context context, Runnable onFinish){
        new Thread(() -> {
            List<AnimeItem> animeItems = Request.getAnimeList(mode, position);
            if (animeItems.size()==0)
                Request.badResponse(context);
            mAnimeList.postValue(animeItems);
            onFinish.run();
        }).start();
    }
}