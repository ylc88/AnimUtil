package com.yanlc.anim.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yanlc.anim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanlc on 2016/8/31.
 */
public class AnimUtils {

    /**
     * 抖动动画 上下左右倾斜抖动
     */
    public static ObjectAnimator PropertyShake(View view) {
        return PropertyShake(view, 1f);
    }

    /**
     * 抖动动画 上下左右倾斜抖动
     * @param view
     * @param shakeFactor 倾斜系数
     * @return
     */
    public static ObjectAnimator PropertyShake(View view, float shakeFactor) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

    /**
     *
     * @param view
     * @param property View.TRANSLATION_X View.TRANSLATION_Y确定抖动方向
     * @param delta  偏移量
     * @return
     */
    public static ObjectAnimator PropertyShakeXY(View view, Property property, int duration, int delta) {
        PropertyValuesHolder pvhTranslate = PropertyValuesHolder.ofKeyframe(property,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );
//掉下抖动效果
//        PropertyValuesHolder pvhTranslateBounce = PropertyValuesHolder.ofKeyframe(property,
//                Keyframe.ofFloat(0f, 0),
//                Keyframe.ofFloat(0.25f, -delta*4),
//                Keyframe.ofFloat(.49f, 0),
//                Keyframe.ofFloat(.65f, -delta*3),
//                Keyframe.ofFloat(.75f, 0),
//                Keyframe.ofFloat(.85f, -delta*2),
//                Keyframe.ofFloat(.90f, 0),
//                Keyframe.ofFloat(.95f, -delta),
//                Keyframe.ofFloat(1f, 0f)
//        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslate).
                setDuration(duration);
    }


    /**
     * 顺序执行Scale动画
     * @param view
     * @param values 动画变化值
     * @param dur 没两个变化值用时
     */
    public static void PropertyScale(View view, float[] values, int[] dur) {
        List<ObjectAnimator> animators = new ArrayList<ObjectAnimator>();
        AnimatorSet set = new AnimatorSet();
        for (int i = 0; i < values.length - 1; i++) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, View.SCALE_X, values[i], values[i + 1]).setDuration(dur[i]);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, values[i], values[i + 1]).setDuration(dur[i]);
            if (i == 0) {
                set.play(animator1).with(animator2);
            } else {
                set.play(animator1).with(animator2).after(animators.get(animators.size() - 1));
            }
            animators.add(animator1);
        }
        animators.clear();
        animators = null;
        set.start();
    }

    /**
     * view先变小胖，后边高瘦在复原
     * @param view
     */
    public static void startWallet(View view) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, View.SCALE_X, 1, 1.1f, 0.9f, 1);
        objectAnimator1.setDuration(600);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1, 0.75f, 1.25f, 1);
        objectAnimator2.setDuration(600);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.start();
    }

    public static ObjectAnimator bounceScaleAnim(View view) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0.0f, 0.0f),
                Keyframe.ofFloat(0.367f, 1.2f),
                Keyframe.ofFloat(0.467f, 0.9f),
                Keyframe.ofFloat(0.5f, 0.875f),
                Keyframe.ofFloat(0.533f, 0.875f),
                Keyframe.ofFloat(0.567f, 0.9f),
                Keyframe.ofFloat(0.667f, 1.013f),
                Keyframe.ofFloat(0.7f, 1.025f),
                Keyframe.ofFloat(0.733f, 1.013f),
                Keyframe.ofFloat(0.833f, 0.96f),
                Keyframe.ofFloat(0.867f, 0.95f),
                Keyframe.ofFloat(0.9f, 0.96f),
                Keyframe.ofFloat(0.967f, 0.99f),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0.0f, 0.0f),
                Keyframe.ofFloat(0.367f, 1.2f),
                Keyframe.ofFloat(0.467f, 0.9f),
                Keyframe.ofFloat(0.5f, 0.875f),
                Keyframe.ofFloat(0.533f, 0.875f),
                Keyframe.ofFloat(0.567f, 0.9f),
                Keyframe.ofFloat(0.667f, 1.013f),
                Keyframe.ofFloat(0.7f, 1.025f),
                Keyframe.ofFloat(0.733f, 1.013f),
                Keyframe.ofFloat(0.833f, 0.96f),
                Keyframe.ofFloat(0.867f, 0.95f),
                Keyframe.ofFloat(0.9f, 0.96f),
                Keyframe.ofFloat(0.967f, 0.99f),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,pvhScaleY).
                setDuration(1000);
    }

    /**
     * 围绕view中心旋转
     * @param view
     * @return
     */
    public static ObjectAnimator propertyRotate(View view,int dur){
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f);
        anim.setDuration(dur);
        anim.setRepeatCount(-1); //默认为0只执行一次，-1一直执行
        //anim.setRepeatMode(Animation.REVERSE); //repeatMode 重复的模式 默认为restart，即重头开始重新运行，可以为reverse即从结束开始向前重新运行
                                                 //setRepeatCount 等于-1 repeatMode为reverse的时候是来回运行
        anim.setInterpolator(new LinearInterpolator());//不加会有停顿
        return anim;
    }


    //===================================非属性动画==============
    public static boolean startAnim(Activity activity, int fromX, int toX, int fromY, int toY) {
        try {
            final ImageView img = new ImageView(activity);
            img.setImageResource(R.mipmap.ic_launcher);
            final FrameLayout tempLayout = new FrameLayout(activity);
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(fromX, fromY, 0, 0);
            tempLayout.addView(img, lp);
            final ViewGroup container = (ViewGroup) activity.getWindow().getDecorView();
            container.addView(tempLayout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            final Animation anim = new TranslateAnimation(0, toX - fromX, 0, toY - fromY);
            anim.setDuration(500);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    container.post(new Runnable() {
                        @Override
                        public void run() {
                            container.removeView(tempLayout);
                        }
                    });
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            img.startAnimation(anim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}