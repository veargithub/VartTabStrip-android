package com.vart.varttabstrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class PagerFragment extends Fragment{
	private static final String FRAGMENT_POSITION = "position";
	private int position;

	public static PagerFragment newInstance(int position) {
		PagerFragment fragment = new PagerFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(FRAGMENT_POSITION, position);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(FRAGMENT_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, 
				getActivity().getResources().getDisplayMetrics());
		lp.setMargins(margin, margin, margin, margin);
		FrameLayout fl = new FrameLayout(getActivity());
		TextView tv = new TextView(getActivity());
		
		fl.setLayoutParams(lp);
		tv.setLayoutParams(lp);
		tv.setGravity(Gravity.CENTER);
		tv.setText(this.position + "");
		fl.addView(tv);
		return fl;
	}
	
}
