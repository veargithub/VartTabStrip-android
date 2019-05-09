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
public class DemoActivity1 extends FragmentActivity {

    private ViewPager pager;
    private VartSlideStripView ssv1;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.demo1);
        pager = findViewById(R.id.pager);
        FragmentManager fm = this.getSupportFragmentManager();
        DemoActivity1.MyFragmentAdapter fmAdapter = new DemoActivity1.MyFragmentAdapter(fm);
        pager.setAdapter(fmAdapter);
        ssv1 = findViewById(R.id.ssv1);
        ssv1.setViewPager(pager);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private final String[] TITLE = new String[] {"1", "2", "3", "4"};

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
