package com.example.m_ployer;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.m_ployer.Adapter.ViewPagerAdapter;
import com.example.m_ployer.fragment.Search;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.example.m_ployer.utils.Constants.PREF_DIRECTORY;
import static com.example.m_ployer.utils.Constants.PREF_NAME;

public class MainActivity extends AppCompatActivity implements Search.OnDataPass {

    public static String USER_ID;
    public static boolean IS_SEARCHED_USER = false;
    ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
   // private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        addtabs();

    }


    private void init() {

        viewPager = findViewById(R.id.viewPager1);
        tabLayout = findViewById(R.id.tabLayout);

    }

    private void addtabs() {
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));

        SharedPreferences Preferences=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        String directory=Preferences.getString(PREF_DIRECTORY,"");

        Bitmap bitmap=loadProfileImage(directory);
        Drawable drawable=new BitmapDrawable(getResources(),bitmap);


        tabLayout.addTab(tabLayout.newTab().setIcon(drawable));


        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()){

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart_fill);
                        break;
                //    case 4:
                 //       tabLayout.getTabAt(4).setIcon(android.R.drawable.ic_menu_help);
                 //       break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart);
                        break;
                 //   case 4:
                 //       tabLayout.getTabAt(4).setIcon(R.drawable.ic_heart_fill);
                 //       break;

                }


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add);
                        break;

                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_heart_fill);
                        break;

                   // case 4:
                      //  tabLayout.getTabAt(4).setIcon(android.R.drawable.ic_menu_help);
                      //  break;

                }
            }
        });


    }
    private Bitmap loadProfileImage(String directory){
        try{
            File file=new File(directory,"profile.png");

            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onChange(String uid) {
        USER_ID=uid;
        IS_SEARCHED_USER=true;
        viewPager.setCurrentItem(4);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 4){
            viewPager.setCurrentItem(0);
            IS_SEARCHED_USER = false;
        }else
        super.onBackPressed();

    }
}