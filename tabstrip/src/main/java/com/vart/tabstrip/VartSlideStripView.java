package com.vart.tabstrip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VartSlideStripView extends LinearLayout implements OnPageChangeListener{
	private int curTabIndex = 0;//当前正在哪个tab
	private float nextPositionOffset = 0f;
	//private int nextTabIndex = 0;//将要去哪个tab
	//private int[] tabsWidth;//每一个tab的宽度
	private Paint stripPaint;//带子的paint
	private Paint dividerPaint;//tab间的分割线
	private ViewPager pager;//绑定的view pager
	private boolean showSeparator;//是否显示分割线
	private int stripHeight = 2;//带子的高度
	private int separatorWidth = 1;//分割线的宽度
	private int separatorPadding = 10;//分割线的padding
	private int defaultStripColor = 0xFF666666;
	private int stripColor = 0xFF666666;//带子的颜色
	private int stripMarginTop = 0;//带子距离上方文字的距离
	private int separatorColor = 0x1A000000;//分割线的颜色
	private boolean scaleTextSize = false;
	private int commonTextSize, maxTextSize;
	private float stripWidthRatio = 1f;//0 to 1,strip的宽度和view的宽度的比值
	private boolean stripWidthEqualWidthText = false;//strip的宽度是否要和文字的宽度一致
	private boolean alphaText = false;//是否对文字进行透明度的改变
	private float initAlpha = 0.5f;
	private float finalAlpha = 1.0f;
	private boolean changeTextColor = false;//是否改变文字的颜色
	private int commonTextColor = 0xFF666666;
	private int highlightTextColor = 0xFF000000;


	public VartSlideStripView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	public VartSlideStripView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public VartSlideStripView(Context context) {
		super(context);
		init();
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VartSlideStripView);
		try {
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int defaultStripHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);//dip to px
			stripHeight = a.getDimensionPixelSize(R.styleable.VartSlideStripView_stripHeight, defaultStripHeight);
			stripColor = a.getColor(R.styleable.VartSlideStripView_stripColor, defaultStripColor);
			stripMarginTop = a.getDimensionPixelSize(R.styleable.VartSlideStripView_stripMarginTop, 0);
			scaleTextSize = a.getBoolean(R.styleable.VartSlideStripView_scaleTextSize, false);
			commonTextSize = a.getDimensionPixelSize(R.styleable.VartSlideStripView_commonTextSize, 0);
			maxTextSize = a.getDimensionPixelSize(R.styleable.VartSlideStripView_maxTextSize, 0);
			stripWidthRatio = a.getFloat(R.styleable.VartSlideStripView_stripWidthRatio, 1f);
			if (stripWidthRatio > 1f || stripWidthRatio <= 0f) {
				stripWidthRatio = 1f;
			}
			stripWidthEqualWidthText = a.getBoolean(R.styleable.VartSlideStripView_stripWidthEqualText, false);
			alphaText = a.getBoolean(R.styleable.VartSlideStripView_alphaText, false);
			initAlpha = a.getFloat(R.styleable.VartSlideStripView_initAlpha, 0.5f);
			finalAlpha = a.getFloat(R.styleable.VartSlideStripView_finalAlpha, 1.0f);

			changeTextColor = a.getBoolean(R.styleable.VartSlideStripView_changeTextColor, false);
			commonTextColor = a.getColor(R.styleable.VartSlideStripView_commonTextColor, commonTextColor);
			highlightTextColor = a.getColor(R.styleable.VartSlideStripView_highlightTextColor, highlightTextColor);
			init();
		} finally {
			a.recycle();
		}
	}
	
	private void init() {
		this.setWillNotDraw(false);//设置成false，viewgroup才会去调用ondraw这个方法
		stripPaint = new Paint();
		stripPaint.setStyle(Style.FILL);//填充rect
		stripPaint.setAntiAlias(true);
		stripPaint.setColor(stripColor);
		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(separatorWidth);
//		DisplayMetrics dm = getResources().getDisplayMetrics();
//		stripHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, stripHeight, dm);//dip to px
//		separatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, separatorWidth, dm);
		showSeparator = false;

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (alphaText) {
			for (int i = 0; i < this.getChildCount(); i++) {
				View tab = this.getChildAt(i);
				tab.setAlpha(initAlpha);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initTabsWidth();
		final View currentTab = this.getChildAt(curTabIndex);
		final int currentTabWidth =  currentTab.getWidth();

		int currentStripWidth;
		if (stripWidthEqualWidthText && currentTab instanceof TextView) {
			currentStripWidth = getTextWidth((TextView) currentTab);
		} else {
			currentStripWidth = (int) (currentTabWidth * stripWidthRatio);//左边的strip的宽度
		}
		int start = currentTab.getLeft() + (currentTabWidth - currentStripWidth) / 2;//当前strip的起始位置
		int end, nextStripWidth;
		View nextTab = null;
		if (curTabIndex < this.getChildCount() - 1) {
			nextTab = this.getChildAt(curTabIndex + 1);
			final int nextTabWidth = nextTab.getWidth();
			if (stripWidthEqualWidthText && nextTab instanceof TextView) {
				nextStripWidth = getTextWidth((TextView) nextTab);
			} else {
				nextStripWidth = (int) (nextTabWidth * stripWidthRatio);
			}
			end = nextTab.getLeft() + (nextTabWidth - nextStripWidth) / 2;
		} else {//如果当前的strip是最后一个
			nextStripWidth = currentStripWidth;
			end = start + currentStripWidth;
		}

		int s = end - start;//strip在滑动过程中要移动的总距离
		int left = start + (int) (s * nextPositionOffset);
		int right = left + currentStripWidth + (int) ((nextStripWidth - currentStripWidth) * nextPositionOffset);
		int height = this.getHeight();
		int top = height - stripHeight;
		int bottom = height;
//		Log.d(">>>>", left + " " + top + " " + right + " " + bottom + " " + offset);
		canvas.drawRect(left, top, right, bottom, stripPaint);
		if (showSeparator) {
			dividerPaint.setColor(separatorColor);
			for (int i = 0; i < this.getChildCount() - 1; i++) {
				View tab = this.getChildAt(i);
				canvas.drawLine(tab.getRight(), separatorPadding, tab.getRight(), height - separatorPadding, dividerPaint);
			} 
		}
		if (currentTab instanceof TextView) {
			TextView tvCur = (TextView) currentTab;
			if (scaleTextSize) {
				int currentTextSize = Math.round(maxTextSize - (maxTextSize - commonTextSize) * nextPositionOffset);
				if (tvCur.getTextSize() != currentTextSize) {
					tvCur.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);
				}
			}
//			if (changeTextColor) { //todo 有bug，暂时注释掉
//				if (nextPositionOffset < 0.5f) {
//					tvCur.setTextColor(highlightTextColor);
//				} else {
//					tvCur.setTextColor(commonTextColor);
//				}
//			}
			if (nextTab instanceof TextView) {
				TextView tvNext = (TextView) nextTab;
				if (scaleTextSize) {
					int nextTextSize = Math.round((maxTextSize - commonTextSize) * nextPositionOffset + commonTextSize);
					if (tvNext.getTextSize() != nextTextSize) {
						tvNext.setTextSize(TypedValue.COMPLEX_UNIT_PX, nextTextSize);
					}
				}
//				if (changeTextColor) { //todo 有bug，暂时注释掉
//					if (nextPositionOffset < 0.5f) {
//						tvNext.setTextColor(commonTextColor);
//					} else {
//						tvNext.setTextColor(highlightTextColor);
//					}
//				}
			}
		}
		if (alphaText) {
			currentTab.setAlpha(finalAlpha - (finalAlpha - initAlpha) * nextPositionOffset);
			if (nextTab != null) {
				nextTab.setAlpha(initAlpha + (finalAlpha - initAlpha) * nextPositionOffset);
			}
		}

	}
	
	private void initTabsWidth() {
		//if (tabsWidth == null) {
			int count = this.getChildCount();
			//tabsWidth = new int[count];
		if (this.pager == null) return;
			for (int i = 0; i < count; i++) {
				final int position = i;
				View tab = this.getChildAt(i);
				//tabsWidth[i] = tab.getWidth();
				tab.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Log.d(">>>>", position + "");
						VartSlideStripView.this.pager.setCurrentItem(position);
					}
					
				});
			}
		//}
	}

	public void setCurrentTab(int position) {
		this.curTabIndex = position;
	}
	
	public void setViewPager(ViewPager viewPager) {
		this.pager = viewPager;
		pager.addOnPageChangeListener(this);
		final PagerAdapter adapter = pager.getAdapter();
		if (adapter != null) {
			if (adapter.getCount() != this.getChildCount()) {
				throw new IllegalStateException("the count of pager must be the same as strips'");
			}
		}
	}

	public void setViewPager(ViewPager viewPager, int index) {
	    setViewPager(viewPager);
	    onPageSelected(index);
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
//		Log.d(">>>>", "onPageScrollStateChanged " + arg0);
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		curTabIndex = arg0;//arg0永远是左边的那个
		nextPositionOffset = arg1;//arg1是右边那个view的宽度与它的容器宽度的比
//		if (arg1 == 0 && arg2 == 0) {
//		    onPageSelected(0);
//        }
		invalidate();
	}

	@Override
	public void onPageSelected(int arg0) {
		if (changeTextColor) {
			int count = this.getChildCount();
			for (int i = 0; i < count; i++) {
				View tab = this.getChildAt(i);
				if (tab instanceof TextView) {
					TextView tvTab = (TextView) tab;
					if (i == arg0) {
						tvTab.setTextColor(highlightTextColor);
					} else {
						tvTab.setTextColor(commonTextColor);
					}
				}
			}
		}
	}
	/**
	 * ���������Ǹ��ߵĸ߶ȣ���λpx
	 */
	public void setStripHeight(int height) {
		this.stripHeight = height;
	}
	/**
	 * ���÷ָ��߿�ȣ���λpx
	 */
	public void setSeparatorWidth(int separatorWidth) {
		this.separatorWidth = separatorWidth;
	}
	/**
	 * ���÷ָ�������padding����λpx
	 */
	public void setSeparatorPadding(int separatorPadding) {
		this.separatorPadding = separatorPadding;
	}
	/**
	 * ���������Ǹ��ߵ���ɫ����λpx
	 */
	public void setStripColor(int stripColor) {
		this.stripColor = stripColor;
	}
	/**
	 * ���÷ָ��ߵ���ɫ����λpx
	 */
	public void setSeparatorColor(int separatorColor) {
		this.separatorColor = separatorColor;
	}
	/**
	 * �Ƿ���ʾ�ָ���
	 */
	public void setShowSeparator(boolean showSeparator) {
		this.showSeparator = showSeparator;
	}

	private int getTextWidth(TextView textView) {
		Rect bounds = new Rect();
		textView.getPaint().getTextBounds(textView.getText().toString(), 0 , textView.getText().length(), bounds);
		return bounds.width();
	}
	
}
