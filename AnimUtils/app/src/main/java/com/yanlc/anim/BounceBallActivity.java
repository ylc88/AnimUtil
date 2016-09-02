package com.yanlc.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.yanlc.anim.util.ShapeHolder;

import java.util.ArrayList;

/**
 * Created by yanlc on 2016/9/2.
 */
public class BounceBallActivity extends Activity {
    private ShapeHolder mShapeHolder;

    private final float BALLHEIGH = 100f;
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouncing_balls);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        mShapeHolder = addBall(screenWidth/2, 100);
        final MyAnimationView myAnimationView = new MyAnimationView(this);
        container.addView(myAnimationView);
        myAnimationView.post(new Runnable() {

            @Override
            public void run() {
                myAnimationView.showBall();
            }
        });
    }

    public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animation = null;

        public MyAnimationView(Context context) {
            super(context);
        }

        public void showBall() {
            float startY = mShapeHolder.getY();
            float endY = getHeight() - BALLHEIGH;
            long duration = 1000;

            //小球落下
            ValueAnimator downAnimator = ObjectAnimator.ofFloat(mShapeHolder, "y", startY , endY);
            downAnimator.setDuration(duration);
            downAnimator.setInterpolator(new AccelerateInterpolator());
            //每次都需要添加这个监听刷新UI 否则不能看到动画
            downAnimator.addUpdateListener(this);

            // 1.设置小球的宽度变大
            ValueAnimator makeBallWide = ObjectAnimator.ofFloat(mShapeHolder, "width", mShapeHolder.getWidth(),
                    mShapeHolder.getWidth() + BALLHEIGH/2);
            makeBallWide.setDuration(duration/4);
            //重复一次
            makeBallWide.setRepeatCount(1);
            //在重复另一次的时候回到原来的状态
            makeBallWide.setRepeatMode(ValueAnimator.REVERSE);
            makeBallWide.setInterpolator(new DecelerateInterpolator());


            // 2.在小球变扁的时候 需要让他居中
            ValueAnimator centerAnimator = ObjectAnimator.ofFloat(mShapeHolder, "x", mShapeHolder.getX(),
                    mShapeHolder.getX() - BALLHEIGH/4);
            centerAnimator.setDuration(duration/4);
            centerAnimator.setRepeatCount(1);
            centerAnimator.setRepeatMode(ValueAnimator.REVERSE);
            centerAnimator.setInterpolator(new DecelerateInterpolator());
            centerAnimator.addUpdateListener(this);


            //3. 小球高度的变小
            ValueAnimator makeBallLow = ObjectAnimator.ofFloat(mShapeHolder, "height",
                    mShapeHolder.getHeight(), mShapeHolder.getHeight() - BALLHEIGH/4);
            makeBallLow.setDuration(duration/4);
            makeBallLow.setRepeatCount(1);
            makeBallLow.setInterpolator(new DecelerateInterpolator());
            makeBallLow.setRepeatMode(ValueAnimator.REVERSE);
            //4.小球高度变化时y的居中
            ValueAnimator  yWhenBallisPlan = ObjectAnimator.ofFloat(mShapeHolder, "y", endY, endY + BALLHEIGH/3);
            yWhenBallisPlan.setDuration(duration/4);
            yWhenBallisPlan.setRepeatCount(1);
            yWhenBallisPlan.setInterpolator(new DecelerateInterpolator());
            yWhenBallisPlan.setRepeatMode(ValueAnimator.REVERSE);


            //小球上升
            ValueAnimator upAnimator = ObjectAnimator.ofFloat(mShapeHolder, "y", endY, startY);
            upAnimator.setDuration(duration);
            upAnimator.setInterpolator(new DecelerateInterpolator());
            upAnimator.addUpdateListener(this);


            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(downAnimator).before(centerAnimator);
            animatorSet.playTogether(centerAnimator , makeBallWide ,makeBallLow, yWhenBallisPlan);
            animatorSet.play(upAnimator).after(centerAnimator);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animatorSet.start();
                    super.onAnimationEnd(animation);
                }

            });
            animatorSet.start();

        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            canvas.translate(mShapeHolder.getX(), mShapeHolder.getY());
            mShapeHolder.getShape().draw(canvas);
            canvas.restore();
        }
        @Override
        public void onAnimationUpdate(ValueAnimator arg0) {
            invalidate();
        }

    }
    private ShapeHolder addBall(float x, float y) {
        OvalShape circle = new OvalShape();
        circle.resize(BALLHEIGH, BALLHEIGH);
        ShapeDrawable drawable = new ShapeDrawable(circle);
        ShapeHolder shapeHolder = new ShapeHolder(drawable);
        shapeHolder.setX(x - BALLHEIGH/2);
        shapeHolder.setY(y - BALLHEIGH/2);
        int red = (int)(Math.random() * 255);
        int green = (int)(Math.random() * 255);
        int blue = (int)(Math.random() * 255);
        int color = 0xff000000 | red << 16 | green << 8 | blue;
        Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
        int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
        RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                50f, color, darkColor, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        shapeHolder.setPaint(paint);
        return shapeHolder;
    }

}
