package net.headup.app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Description 向外部提供接口实现滚动效果。这里只支持横向
 * Note
 * Created by 古阿古斯 on 14-5-8.
 */
public class ScrollableImage extends ImageView {

    private static final String TAG = ScrollableImage.class.getSimpleName();

    private float mX;

    public ScrollableImage(Context context) {
        super(context);
    }

    public ScrollableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.save();
        canvas.translate(.5f * mX * -getWidth(), 0f);
        super.onDraw(canvas);
        //canvas.restore();
    }

    public void setXTransform(float x) {
        mX = x;
        Log.e(TAG, "ScrollableImage scroll x " + x);
        //通知重绘
        invalidate();
    }
}
