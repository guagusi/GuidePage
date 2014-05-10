package net.headup.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Description
 * Note
 * Created by 古阿古斯 on 14-5-10.
 */
public class LinePageIndicator_ extends View {

    private static final String TAG = LinePageIndicator_.class.getSimpleName();
    private Context mContext;
    private ViewPager mViewPager;
    private int mCurrItem;

    private Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    //paint 的大小
    private float mStrokeWidth;
    private int mSelectedColor;
    private int mUnselectedColor;
    //属性
    private boolean mCentered;
    private float mLineWidth;
    private float mGapWidth;

    private static final int INVALID_POINTER = -1;
    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;
    private float mLastMotionX = -1;

    public LinePageIndicator_(Context context) {
        this(context, null, 0);
    }

    public LinePageIndicator_(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     * @param context
     * @param attrs
     * @param defStyle
     */
    public LinePageIndicator_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        /*TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.LinePageIndicator_, 0, 0);
        try {
            mCentered = typedArray.getBoolean();
            mLineWidth = typedArray.getDimension();
            mGapWidth = typedArray.getDimension();
            mStrokeWidth = typedArray.getDimension();
            mSelectedColor = typedArray.getColor();
            mUnselectedColor = typedArray.getColor();
        } finally {
            typedArray.recycle();
        }*/
        mCentered = true;
        mLineWidth = 20;
        mGapWidth = 25;
        mStrokeWidth = 5;
        mSelectedColor = Color.parseColor("#ff3434");
        mUnselectedColor = Color.parseColor("#454545");

        //设置Paint
        setStrokeWidth(mStrokeWidth);
        mPaintSelected.setColor(mSelectedColor);
        mPaintUnselected.setColor(mUnselectedColor);

        ViewConfiguration config = ViewConfiguration.get(mContext);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(config);
    }

    /**
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mViewPager == null) {
            return;
        }
        //只能通过adapter获取view的数量，ViewPager.getChildCount() 为0
        int itemCount = mViewPager.getAdapter().getCount();
        if(itemCount == 0) {
            return;
        }
        //一个item跟一个间隔
        float lineWidthAndGap = mLineWidth + mGapWidth;
        //first item 到 last item的大小
        float indicatorWidth = lineWidthAndGap * itemCount - mGapWidth;
        float centerX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2.0f;
        float centerY = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2.0f;
        float horizontalOffset = getPaddingLeft();
        //是否水平居中
        if(mCentered) {
            //根据getWidth() 和 indicatorWidth，算出要居中的话应该从哪里开始布局(X 轴)
            horizontalOffset = centerX - indicatorWidth / 2.0f;
        }
        for(int i = 0; i < itemCount; i ++) {
            float dx1 = horizontalOffset + i * lineWidthAndGap;
            float dx2 = dx1 + mLineWidth;
            if(i == mCurrItem) {
                canvas.drawLine(dx1, centerY, dx2, centerY, mPaintSelected);
            } else {
                canvas.drawLine(dx1, centerY, dx2, centerY, mPaintUnselected);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 计算width
     * @param measureWidth
     * @return
     */
    private int measureWidth(int measureWidth) {
        float result;
        int specMode = MeasureSpec.getMode(measureWidth);
        int specSize = MeasureSpec.getSize(measureWidth);

        //有parent指定
        if((specMode == MeasureSpec.EXACTLY) || mViewPager == null) {
            result = specSize;
        } else {
            final int itemCount = mViewPager.getAdapter().getCount();
            result = getPaddingLeft() + getPaddingRight() + (mLineWidth + mGapWidth) * itemCount - mGapWidth;
            //在parent给定的约束下
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) FloatMath.ceil(result);
    }

    /**
     * 计算height
     * @param measureHeight
     * @return
     */
    private int measureHeight(int measureHeight) {
        float result;
        int specMode = MeasureSpec.getMode(measureHeight);
        int specSize = MeasureSpec.getSize(measureHeight);

        //有parent指定
        if((specMode == MeasureSpec.EXACTLY) || mViewPager == null) {
            result = specSize;
        } else {
            result = getPaddingTop() + getPaddingBottom() + mStrokeWidth;
            //在parent给定的约束下
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) FloatMath.ceil(result);
    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(super.onTouchEvent(event)) {
            return true;
        }
        if(mViewPager == null || mViewPager.getAdapter().getCount() == 0) {
            return false;
        }
        //多点触控
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mLastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //根据pointer id 找索引
                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                final float x = MotionEventCompat.getX(event, activePointerIndex);
                float deltaX = x - mLastMotionX;

                //与ViewPager 联动。在dragging时，ViewPager 跟随fake滑动
                if(!mIsDragging) {
                    if(Math.abs(deltaX) > mTouchSlop) {
                        mIsDragging = true;
                    }
                }
                if(mIsDragging) {
                    mLastMotionX = x;
                    if(mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                        //ViewPager 会根据自己的策略决定滑到下一个View还是回滚
                        mViewPager.fakeDragBy(deltaX);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //完成联动
                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if(mViewPager.isFakeDragging()) {
                    mViewPager.endFakeDrag();
                }
                mCurrItem = mViewPager.getCurrentItem();
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(event);
                mLastMotionX = MotionEventCompat.getX(event, index);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                if(pointerId == mActivePointerId) {
                    //第一个Pointer 为0
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(event, MotionEventCompat.findPointerIndex(event, mActivePointerId));
                mCurrItem = mViewPager.getCurrentItem();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void setViewPager(ViewPager viewPager) {
        if(viewPager == null) {
            return;
        }
        if(viewPager.getAdapter() == null) {
            return;
        }
        mViewPager = viewPager;
        mCurrItem = viewPager.getCurrentItem();
        invalidate();
    }

    public void setCurrentItem(int currPos) {
        mCurrItem = currPos;
        invalidate();
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaintSelected.setStrokeWidth(mStrokeWidth);
        mPaintUnselected.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        invalidate();
    }

    public int getUnselectedColor() {
        return mUnselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        mUnselectedColor = unselectedColor;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    public void setGapWidth(float gapWidth) {
        mGapWidth = gapWidth;
        invalidate();
    }
}
