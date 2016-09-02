package com.yanlc.anim.view;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 阻尼scale，在view上面添加一个动画
 * Created by yanlc on 2016/8/31.
 * 借鉴 hanks 的SmallBang
 */
@SuppressLint("NewApi")
public class BounceMaskView extends View {

    private long ANIMATE_DURATION = 1000;
    private float MAX_CIRCLE_RADIUS = 100;
    private float fraction;
    private float progress;
    private Paint circlePaint;

    private int maskColor = Color.parseColor("#88EEFF");

    private int[] mExpandInset = new int[2];
    private int centerY;
    private int centerX;

    public BounceMaskView(Context context) {
        super(context);
        init(null, 0);
    }

    public BounceMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BounceMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    /**
     * 设置蒙版颜色
     * @param color
     */
    public void setMaskColor(int color){
        maskColor = color;
    }

    public static BounceMaskView attach2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        BounceMaskView smallBang = new BounceMaskView(activity);
        rootView.addView(smallBang, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return smallBang;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLACK);
    }

    public void bang(final View view) {
        bang(view, -1);
    }

    public void bang(final View view, float radius) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);

        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);

        centerX = r.left + r.width() / 2;
        centerY = r.top + r.height() / 2;

        if (radius != -1) {
            initRadius(radius);
        } else {
            initRadius(Math.max(r.width(), r.height()));
        }

        Keyframe kf0 = Keyframe.ofFloat(0.0f, 0.0f);
//        Keyframe kf3 = Keyframe.ofFloat(0.333f, 1.25f);
        Keyframe kf4 = Keyframe.ofFloat(0.367f, 1.2f);
        Keyframe kf5 = Keyframe.ofFloat(0.467f, 0.9f);
        Keyframe kf6 = Keyframe.ofFloat(0.5f, 0.875f);
        Keyframe kf7 = Keyframe.ofFloat(0.533f, 0.875f);
        Keyframe kf8 = Keyframe.ofFloat(0.567f, 0.9f);
        Keyframe kf9 = Keyframe.ofFloat(0.667f, 1.013f);
        Keyframe kf10 = Keyframe.ofFloat(0.7f, 1.025f);
        Keyframe kf11 = Keyframe.ofFloat(0.733f, 1.013f);
        Keyframe kf12 = Keyframe.ofFloat(0.833f, 0.96f);
        Keyframe kf13 = Keyframe.ofFloat(0.867f, 0.95f);
        Keyframe kf14 = Keyframe.ofFloat(0.9f, 0.96f);
        Keyframe kf15 = Keyframe.ofFloat(0.967f, 0.99f);
        Keyframe kf16 = Keyframe.ofFloat(1.0f, 1.0f);
        //这里属性随便写一个
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("yanlc", kf0, kf4, kf5, kf6, kf7, kf8, kf9, kf10, kf11, kf12, kf13, kf14, kf15, kf16);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhRotation);
        scaleAnim.setDuration(ANIMATE_DURATION);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取值
                float animV = (Float) valueAnimator.getAnimatedValue();
                view.setScaleX(animV);
                view.setScaleY(animV);
                //获取百分比
                fraction = valueAnimator.getAnimatedFraction();
                progress = animV;
                invalidate();
            }
        });
        scaleAnim.start();
    }

    private void initRadius(float max_circle_radius) {
        MAX_CIRCLE_RADIUS = max_circle_radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (fraction >= 0 && fraction <= 0.2) {
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(maskColor);
            canvas.drawCircle(centerX, centerY, MAX_CIRCLE_RADIUS * progress, circlePaint);
        } else if (fraction > 0.2 && fraction < 0.98) {
                circlePaint.setStyle(Paint.Style.STROKE);
                float progress2 = (fraction - 0.2f) / 0.8f;
                float strokeWidth = (MAX_CIRCLE_RADIUS) * (1 - getInterpolation(1.0f, progress2));
                circlePaint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(centerX, centerY, MAX_CIRCLE_RADIUS*progress , circlePaint);
        }
    }

    /**
     * 一个开始比较快然后减速的插值器
     * @param mFactor 动画的快慢度。将factor值设置为1.0f时将产生一条从上向下的y=x^2抛物线。
     *        增加factor到1.0f以上将使渐入的效果增强（也就是说，开头更快，结尾更慢）
     * @param input
     * @return
     */
    public float getInterpolation(float mFactor,float input) {
        float result;
        if (mFactor == 1.0f) {
            result = (1.0f - ((1.0f - input) * (1.0f - input)));
        } else {
            result = (float)(1.0f - Math.pow((1.0f - input), 2 * mFactor));
        }
        return result;
    }


}
