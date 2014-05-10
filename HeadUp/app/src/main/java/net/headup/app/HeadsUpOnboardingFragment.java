package net.headup.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description
 * Note
 * Created by 古阿古斯 on 14-5-8.
 */
public class HeadsUpOnboardingFragment extends Fragment implements ViewPager.PageTransformer,
        ViewPager.OnPageChangeListener {

    private static final String TAG = HeadsUpOnboardingFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private ImageView mLogo;
    //private LinePageIndicator mPageIndocator;
    private GuidePagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View localView = inflater.inflate(R.layout.frag_headsup_onboarding, container, false);
        return localView;
    }

    /**
     * Called immediately after {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mLogo = (ImageView) view.findViewById(R.id.heads_up_onboarding_logo);
        //mPageIndocator = (LinePageIndicator) view.findViewById(R.id.view_pager_indicator);


        mPagerAdapter = new GuidePagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        //mPageIndocator.setViewPager(mViewPager);
        mViewPager.setPageTransformer(true, this);
        mViewPager.setOnPageChangeListener(this);
        mPageIndocator_ = (LinePageIndicator_) view.findViewById(R.id.view_pager_indicator_);
        mPageIndocator_.setViewPager(mViewPager);
    }
    LinePageIndicator_ mPageIndocator_;
    @Override
    public void transformPage(View view, float v) {
        ScrollableImage si = (ScrollableImage) view.findViewById(R.id.heads_up_onboarding_bg);
        //si.setTranslationX(-0.5f * v * si.getWidth());
        ((ScrollableImage) view.findViewById(R.id.heads_up_onboarding_bg)).setXTransform(v);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        //mPageIndocator.setCurrentItem(i);
        mPageIndocator_.setCurrentItem(i);
        if(i == 0 || i == 1 && i == 2) {
            return;
        }
        Property alpha_pro = View.ALPHA;
        float[] float_pro = new float[1];
        float f1;

        Property trans_pro = View.TRANSLATION_Y;
        float[] float_pro2 = new float[1];
        float f2;

        ObjectAnimator alphsAnim;
        ObjectAnimator traYAnim;

        //start up 页面
        if(i == 4) {
            f2 = 0.0f;
        } else {
            f2 = -1 * mLogo.getBottom();
        }
        f1 = 1.0f;
        float_pro[0] = f1;
        alphsAnim = ObjectAnimator.ofFloat(mLogo, alpha_pro, float_pro);

        alphsAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mLogo.getVisibility() == View.INVISIBLE && mLogo.getAlpha() != 0.0f) {
                    mLogo.setVisibility(View.VISIBLE);
                } else {
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mLogo.getVisibility() != View.VISIBLE || mLogo.getAlpha() != 0) {

                } else {
                    mLogo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mLogo.getVisibility() != View.VISIBLE || mLogo.getAlpha() != 0) {

                } else {
                    mLogo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //使用硬件加速
        mLogo.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        float_pro2[0] = f2;
        traYAnim = ObjectAnimator.ofFloat(mLogo, trans_pro, float_pro2);
        traYAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLogo.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mLogo.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet alphaAndTranY = new AnimatorSet();
        alphaAndTranY.playTogether(new Animator[] {alphsAnim, traYAnim});
        alphaAndTranY.setDuration(1000);
        alphaAndTranY.setInterpolator(new OvershootInterpolator(1.0f));
        alphaAndTranY.start();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     *
     */
    private class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position < 4) {
                //普通guide page
                view = createGuidePage(container, position);
            } else {
                //start up 页面
                view = createStartUpPage(container, position);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        private View createGuidePage(ViewGroup container, int position) {
            View view = LayoutInflater.from(HeadsUpOnboardingFragment.this.getActivity()).
                    inflate(R.layout.heads_up_onboarding_view, container, false);
            TextView textView1 = (TextView) view.findViewById(R.id.heads_up_onboarding_title);
            textView1.setText((position + 1) + "只草泥马");
            TextView textView2 = (TextView) view.findViewById(R.id.heads_up_onboarding_subtitle);
            textView2.setText("aim...shot...die");
            ScrollableImage scrollableImage = (ScrollableImage) view.findViewById(R.id.heads_up_onboarding_bg);

            int resId = -1;
            switch (position) {
                case 0:
                    resId = R.drawable.headsup_ob_bg_image1;
                    break;
                case 1:
                    resId = R.drawable.headsup_ob_bg_image2;
                    break;
                case 2:
                    resId = R.drawable.headsup_ob_bg_image3;
                    break;
                case 3:
                    resId = R.drawable.headsup_ob_bg_image4;
                    break;
                case 5:
                    resId = R.drawable.headsup_ob_bg_image5;
                    break;
                default:
                    break;
            }
            scrollableImage.setImageResource(resId);

            ImageView deviceImg = ((ImageView) view.findViewById(R.id.heads_up_onboarding_device_image));

            switch (position) {
                case 0:
                    resId = R.drawable.headsup_ob_device1;
                    break;
                case 1:
                    resId = R.drawable.headsup_ob_device2;
                    break;
                case 2:
                    resId = R.drawable.headsup_ob_device3;
                    break;
                case 3:
                    resId = R.drawable.headsup_ob_device4;
                    break;
                default:
                    break;
            }
            deviceImg.setImageResource(resId);
            return view;
        }

        private View createStartUpPage(ViewGroup container, int position) {
            View view = LayoutInflater.from(HeadsUpOnboardingFragment.this.getActivity()).
                    inflate(R.layout.heads_up_onboarding_last_view, container, false);
            ScrollableImage scrollableImage = (ScrollableImage) view.findViewById(R.id.heads_up_onboarding_bg);
            Button button = (Button) view.findViewById(R.id.start);
            button.setOnClickListener(null);
            return view;
        }


    }
}














