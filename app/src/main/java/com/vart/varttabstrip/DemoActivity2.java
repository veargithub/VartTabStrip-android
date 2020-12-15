package com.vart.varttabstrip;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.vart.tabstrip.VartSlideStripView;

public class DemoActivity2 extends AppCompatActivity {

    private ViewPager pager;
    private VartSlideStripView ssv1;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.demo2);
        pager = findViewById(R.id.pager);
        FragmentManager fm = this.getSupportFragmentManager();
        DemoActivity2.MyFragmentAdapter fmAdapter = new DemoActivity2.MyFragmentAdapter(fm);
        pager.setAdapter(fmAdapter);
        ssv1 = findViewById(R.id.ssv1);
        ssv1.setViewPager(pager, 2);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private final String[] TITLE = new String[] {"1", "2", "3", "4", "5"};

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return PagerFragment.newInstance(arg0);
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

    }
}
