package net.headup.app;

import android.view.animation.Interpolator;

/**
 * Description
 * Note
 * Created by 古阿古斯 on 14-5-9.
 */
public class QuartOutInterpolator implements Interpolator {

    /**
     * Maps a value representing the elapsed fraction of an animation to a value that represents
     * the interpolated fraction. This interpolated value is then multiplied by the change in
     * value of an animation to derive the animated value at the current elapsed animation time.
     *
     * @param input A value between 0 and 1.0 indicating our current point
     *              in the animation where 0 represents the start and 1.0 represents
     *              the end
     * @return The interpolation value. This value can be more than 1.0 for
     * interpolators which overshoot their targets, or less than 0 for
     * interpolators that undershoot their targets.
     */
    @Override
    public float getInterpolation(float input) {
        return 0;
    }
}
