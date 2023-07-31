package com.sakura;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sakura.databinding.ActivityMainBinding;
import com.sakura.ui.download.DownloadFragment;
import com.sakura.ui.favourite.FavouriteFragment;
import com.sakura.ui.home.HomeFragment;
import com.sakura.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ViewPager2 mainPager;
    MainPageAdapter mainPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainPager = binding.navHostFragmentActivityMain;
        mainPageAdapter = new MainPageAdapter(this);
        mainPager.setAdapter(mainPageAdapter);
        mainPager.setOffscreenPageLimit(3);
        mainPager.setUserInputEnabled(false);

        BottomNavigationView navView = binding.navView;
        navView.setOnItemSelectedListener(menuItem -> {
            int resId = menuItem.getItemId();

            final int[] ORI_ID = new int[]{
                    R.id.navigation_home,
                    R.id.navigation_fav,
                    R.id.navigation_download,
                    R.id.navigation_settings
            };

            for (int i = 0; i < ORI_ID.length; i++)
                if (resId == ORI_ID[i]) {
                    mainPager.setCurrentItem(i, false);
                    return true;
                }
            return false;
        });
    }

}

class MainPageAdapter extends FragmentStateAdapter {

    Fragment[] pages = new Fragment[]{
            new HomeFragment(),
            new FavouriteFragment(),
            new DownloadFragment(),
            new SettingsFragment()
    };

    public MainPageAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pages[position];
    }

    @Override
    public int getItemCount() {
        return pages.length;
    }

    @Override
    public long getItemId(int position) {
        return pages[position].hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (Fragment f : pages)
            if (f.hashCode() == itemId)
                return true;
        return false;
    }
}