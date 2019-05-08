package com.vart.varttabstrip;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class SlideStripPagerActivity extends FragmentActivity{
	private ViewPager pager;
	private VartSlideStripView ssv1;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_slide_strip_pager);
		pager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = this.getSupportFragmentManager();
		MyFragmentAdapter fmAdapter = new MyFragmentAdapter(fm);
		pager.setAdapter(fmAdapter);
		ssv1 = (VartSlideStripView) findViewById(R.id.ssv1);
		ssv1.setViewPager(pager);
	}
	
	private class MyFragmentAdapter extends FragmentPagerAdapter {
		private final String[] TITLE = new String[] {"1", "2", "3", "4", "5", "6"};

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
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
