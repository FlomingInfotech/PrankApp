package com.prank.sexygirlcallprank.activity;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.fragment.AddContactsFragment;
import com.prank.sexygirlcallprank.fragment.MyContactsFragment;
import com.prank.sexygirlcallprank.fragment.SettingFragment;
import com.prank.sexygirlcallprank.helper.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewpagerMain;
    private TabLayout tabsMain;
    private ViewPagerAdapter adapter;
    private AdView adView;
    private LinearLayout bannerContainer;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor();
        tabsMain = findViewById(R.id.tabLayoutMain);
        bannerContainer = findViewById(R.id.bannerContainer);
        viewpagerMain = findViewById(R.id.viewpagerMain);
        findViewById(R.id.layoutTab);
        setupViewPager(viewpagerMain);
        setUpMainScreen();
        showBannerAd();

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyContactsFragment());
        adapter.addFragment(new AddContactsFragment());

        adapter.addFragment(new SettingFragment());
        viewPager.setAdapter(adapter);
        tabsMain.setupWithViewPager(viewPager);

    }


    private void setUpMainScreen() {
        final int[] arrayIcons = {R.drawable.ic_call_black_24dp, R.drawable.ic_add_box_black_24dp, R.drawable.ic_person_black_24dp};
        for (int i = 0; i < arrayIcons.length; i++) {

            LinearLayout tabLayout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab_main, tabsMain, false);
            ImageView imageView = tabLayout.findViewById(R.id.imageTabIcon);
            imageView.setImageResource(arrayIcons[i]);

            Objects.requireNonNull(tabsMain.getTabAt(i)).setCustomView(tabLayout);

        }

        final int tabIconColorSelected = ContextCompat.getColor(this, R.color.colorBlue);
        final int tabIconColorDeselected = ContextCompat.getColor(this, R.color.colorGrY);

        tabsMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpagerMain) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);

                if (tab.getCustomView() != null) {
                    changeTabColor(tabIconColorSelected, tab.getCustomView());
                }
                if (tab.getPosition() == 0) {
                    MyContactsFragment myContactsFragment = (MyContactsFragment) adapter.mFragmentList.get(0);
                    myContactsFragment.setAdapter();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                if (tab.getCustomView() != null) {
                    changeTabColor(tabIconColorDeselected, tab.getCustomView());
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
        int currentTab = 0;
        TabLayout.Tab tab = tabsMain.getTabAt(currentTab);
        if (tab != null) {
            if (tab.getCustomView() != null) {
                changeTabColor(tabIconColorSelected, tab.getCustomView());
            }
        }

    }


    private void changeTabColor(int color, View view) {
        ImageView imageView = view.findViewById(R.id.imageTabIcon);
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        try {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                if (viewpagerMain.getCurrentItem() == 0) {
                    if (doubleBackToExitPressedOnce) {

                        super.onBackPressed();
                        return;
                    }
                    showDialog();
                } else {
                    viewpagerMain.setCurrentItem(0);
                }
            } else {
                if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() + 4).getTag().equals(viewpagerMain.getCurrentItem() + "")) {
                    if (doubleBackToExitPressedOnce) {

                        super.onBackPressed();
                        return;
                    }
                    showDialog();
                } else {
                    viewpagerMain.setCurrentItem(Integer.parseInt(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() + 4).getTag()));
                }
            }
        } catch (Exception e) {
            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();
                return;
            }
            showDialog();
        }
    }


    private void rateUsApp() {

        try {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find PlayStore app", Toast.LENGTH_LONG).show();
        }
    }
    public void showDialog(){
        doubleBackToExitPressedOnce = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Rate Us");
        alertBuilder.setMessage(R.string.txt_alrt);
        alertBuilder.setPositiveButton(R.string.txt_rate, (dialog, which) -> rateUsApp());
        alertBuilder.setNegativeButton(R.string.txt_later, (dialogInterface, i) -> {

            finish();
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


    }


    private void showBannerAd() {
        adView = new AdView(this, Prefs.getString(ConstantMethod.BANNER_ID, getString(R.string.Ad_Banner)), AdSize.BANNER_HEIGHT_50);
        bannerContainer.addView(adView);
        adView.loadAd(adView.buildLoadAdConfig().build());

    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        super.onDestroy();
    }

}
