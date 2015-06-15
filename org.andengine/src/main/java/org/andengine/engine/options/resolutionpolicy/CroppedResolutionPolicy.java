package org.andengine.engine.options.resolutionpolicy;

/**
 * Created by matej on 21.1.2015.
 */


import android.view.View.MeasureSpec;

public class CroppedResolutionPolicy extends BaseResolutionPolicy {
    private final float desiredWidth;
    private final float desiredHeight;
    private float userWidth;
    private float userHeight;
    private float left;
    private float right;
    private float top;
    private float bottom;
    public CroppedResolutionPolicy(float pWidth, float pHeight) {
        desiredWidth = pWidth;
        desiredHeight = pHeight;
    }
    @Override
    public void onMeasure(Callback pResolutionPolicyCallback,
                          int pWidthMeasureSpec, int pHeightMeasureSpec) {
        BaseResolutionPolicy.throwOnNotMeasureSpecEXACTLY(pWidthMeasureSpec,
                pHeightMeasureSpec);
        final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
        final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);
        final float desiredRatio = (float) desiredWidth / (float) desiredHeight;
        float scaleRatio;
        float resultWidth;
        float resultHeight;
        if ((float) measuredWidth / (float) measuredHeight < desiredRatio) {
// Scale to fit height, width will crop
            resultWidth = measuredHeight * desiredRatio;
            resultHeight = measuredHeight;
            scaleRatio = desiredHeight / resultHeight;
        } else {
// Scale to fit width, height will crop
            resultHeight = measuredWidth / desiredRatio;
            resultWidth = measuredWidth;
            scaleRatio = desiredWidth / resultWidth;
        }
        userWidth = measuredWidth * scaleRatio;
        userHeight = measuredHeight * scaleRatio;
        left = (desiredWidth - userWidth) / 2f;
        right = userWidth + left;
        bottom = (desiredHeight - userHeight) / 2f;
        top = userHeight + bottom;
        pResolutionPolicyCallback.onResolutionChanged(Math.round(resultWidth), Math.round(resultHeight));
    }
    public float getUserWidth() {
        return userWidth;
    }
    public float getUserHeight() {
        return userHeight;
    }
    public float getLeft() {
        return left;
    }
    public float getRight() {
        return right;
    }
    public float getTop() {
        return top;
    }
    public float getBottom() {
        return bottom;
    }
}