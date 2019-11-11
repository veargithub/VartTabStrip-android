package com.vart.varttabstrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.vart.tabstrip.VartSlideStripView;

/**
 * 作者: vart chen
 * 日期: 2019/5/8 13:28
 * 邮箱: 421790096@qq.com
 */
public class DemoActivity3 extends FragmentActivity {

    private ViewPager pager;
    private VartSlideStripView ssv1;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.demo3);
        pager = findViewById(R.id.pager);
        FragmentManager fm = this.getSupportFragmentManager();
        DemoActivity3.MyFragmentAdapter fmAdapter = new DemoActivity3.MyFragmentAdapter(fm);
        pager.setAdapter(fmAdapter);
        ssv1 = findViewById(R.id.ssv1);
        ssv1.setViewPager(pager);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private final String[] TITLE = new String[] {"1", "2", "3", "4", "5", "6", "7"};

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
