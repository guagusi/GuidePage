package net.headup.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Description 自定义TextView，扩展View默认的状态
 * Note
 * Created by 古阿古斯 on 14-5-8.
 */
public class IndicatorCellView extends TextView {

    private static final String TAG = IndicatorCellView.class.getSimpleName();

    //是否是当前正在显示的
    private static final int[] STATE_CURRENT_ITEM = {R.attr.state_current_item};
    private boolean mIsCurrent = false;

    //自定义属性
    private  boolean state_current_item;

    public IndicatorCellView(Context context) {
        this(context, null, 0);
    }

    public IndicatorCellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.IndicatorCellView, 0, 0 );
        try {
            //通过索引或者指定的键都可以获取到
            //state_current_item = typedArray.getBoolean(0, false);
            state_current_item = typedArray.getBoolean(R.styleable.IndicatorCellView_state_current_item, false);
            setStateCurrentItem(state_current_item);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        //扩展View默认的Drawable 状态
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if(mIsCurrent) {
            Log.e(TAG, "onCreateDrawableState" + " " + mIsCurrent);
            mergeDrawableStates(drawableState, STATE_CURRENT_ITEM);
        }

        return drawableState;
    }

    public void setStateCurrentItem(boolean isCurrent) {
        mIsCurrent = isCurrent;
        //强制刷新控件的Drawable 状态
        refreshDrawableState();
    }


}
