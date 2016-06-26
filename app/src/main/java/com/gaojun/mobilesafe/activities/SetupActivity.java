package com.gaojun.mobilesafe.activities;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gaojun.mobilesafe.R;
import com.gaojun.mobilesafe.fragments.Setup1Fragment;
import com.gaojun.mobilesafe.fragments.Setup2Fragment;
import com.gaojun.mobilesafe.fragments.Setup3Fragment;
import com.gaojun.mobilesafe.fragments.Setup4Fragment;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    private ViewPager vp_setup_view;
    private FrameLayout fl_content;
    private FragmentManager fm;
    private ViewPagerAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private ImageView iv_point1,iv_point2,iv_point3,iv_point4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        fl_content = (FrameLayout) findViewById(R.id.fl_setup_content);
        vp_setup_view = (ViewPager) findViewById(R.id.vp_setup_view);
        iv_point1 = (ImageView) findViewById(R.id.iv_point1);
        iv_point2 = (ImageView) findViewById(R.id.iv_point2);
        iv_point3 = (ImageView) findViewById(R.id.iv_point3);
        iv_point4 = (ImageView) findViewById(R.id.iv_point4);
        initFragment();
        fm = getSupportFragmentManager();
        mAdapter = new ViewPagerAdapter(fm,fragments);
        vp_setup_view.setAdapter(mAdapter);

        vp_setup_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    switch (position){
                        case 0:
                            iv_point1.setImageResource(android.R.drawable.presence_online);
                            iv_point2.setImageResource(android.R.drawable.presence_invisible);
                            iv_point3.setImageResource(android.R.drawable.presence_invisible);
                            iv_point4.setImageResource(android.R.drawable.presence_invisible);
                            break;
                        case 1:
                            iv_point1.setImageResource(android.R.drawable.presence_invisible);
                            iv_point2.setImageResource(android.R.drawable.presence_online);
                            iv_point3.setImageResource(android.R.drawable.presence_invisible);
                            iv_point4.setImageResource(android.R.drawable.presence_invisible);
                            break;
                        case 2:
                            iv_point1.setImageResource(android.R.drawable.presence_invisible);
                            iv_point2.setImageResource(android.R.drawable.presence_invisible);
                            iv_point3.setImageResource(android.R.drawable.presence_online);
                            iv_point4.setImageResource(android.R.drawable.presence_invisible);
                            break;
                        case 3:
                            iv_point1.setImageResource(android.R.drawable.presence_invisible);
                            iv_point2.setImageResource(android.R.drawable.presence_invisible);
                            iv_point3.setImageResource(android.R.drawable.presence_invisible);
                            iv_point4.setImageResource(android.R.drawable.presence_online);
                            break;
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_setup_view.setCurrentItem(0);
        vp_setup_view.setOffscreenPageLimit(2);
    }

    //初始化fragment
    private void initFragment() {
        Setup1Fragment setup1Fragment = new Setup1Fragment();
        Setup2Fragment setup2Fragment = new Setup2Fragment();
        Setup3Fragment setup3Fragment = new Setup3Fragment();
        Setup4Fragment setup4Fragment = new Setup4Fragment(this);
        fragments.add(setup1Fragment);
        fragments.add(setup2Fragment);
        fragments.add(setup3Fragment);
        fragments.add(setup4Fragment);
    }


    /**
     * viewpager的adapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        public ViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
