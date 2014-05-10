package net.headup.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Note
 * Created by 古阿古斯 on 14-5-8.
 */
public class LinePageIndicator extends LinearLayout {

    private static final String TAG = LinePageIndicator.class.getSimpleName();

    private Context mContext;
    private ViewPager mViewPager;
    private int mTotalItem;
    private int mCurrItem;
    private List<IndicatorCellView> mItemViews = new ArrayList<IndicatorCellView>();

    //自定义的属性
    private float lineWidth;
    private int selectedColor;
    private float strokeWidth;
    private int unselectedColor;

    public LinePageIndicator(Context context) {
        this(context, null, 0);
    }

    public LinePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator, 0, 0);
        try {
            //dp会转为px
            lineWidth = typedArray.getDimension(R.styleable.LinePageIndicator_lineWidth, 0);
            //二进制颜色值转型过来会问题
            selectedColor = typedArray.getColor(R.styleable.LinePageIndicator_selectedColor, 0);
            strokeWidth = typedArray.getDimension(R.styleable.LinePageIndicator_strokeWidth, 0);
            unselectedColor = typedArray.getColor(R.styleable.LinePageIndicator_unselectedColor, 0);
            Log.e(TAG, "TypedArray: " +
                    "lineWidth = " + typedArray.getDimension(0, 0) +
                    "selectedColor = " + typedArray.getColor(1, 0) +
                    "strokeWidth = " + typedArray.getDimension(2, 0) +
                    "unselectedColor = " + typedArray.getColor(3, 0));
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 初始化view
     * @param viewPager
     */
    public void setViewPager(final ViewPager viewPager) {
        if(viewPager == null) {
            throw new NullPointerException();
        }
        Log.e(TAG, "not null");
        mTotalItem = viewPager.getChildCount();
        Log.e(TAG, "not null" + mTotalItem);

        mCurrItem = viewPager.getCurrentItem();
        mItemViews.clear();
        for(int i = 0; i < 5; i ++) {
            IndicatorCellView cellView = new IndicatorCellView(mContext);
            mItemViews.add(cellView);

            final int currIndex = i;
            cellView.setBackgroundResource(R.drawable.selector_indicator_cell);
            cellView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(currIndex);
                    setCurrentItem(currIndex);
                }
            });

            if(mCurrItem == i) {
                cellView.setStateCurrentItem(true);
            } else {
                cellView.setStateCurrentItem(false);
            }
            addView(cellView, new LayoutParams(20, 20));
            addView(new TextView(mContext), new LayoutParams(20, 20));
        }
        requestLayout();
    }

    /**
     * 设置当前选中
     * @param item
     */
    public void setCurrentItem(int item) {
        Log.e(TAG, "current Item" + item);
        if(mItemViews.size() <= 0) {
            return;
        }
        for(int i = 0; i < 5; i ++) {
            IndicatorCellView cellView = mItemViews.get(i);
            if(i == item) {
                cellView.setStateCurrentItem(true);
            } else {
                cellView.setStateCurrentItem(false);
            }
        }
    }
}
