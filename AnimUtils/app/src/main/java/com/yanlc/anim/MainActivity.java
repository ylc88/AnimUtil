package com.yanlc.anim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yanlc.anim.util.AnimUtils;
import com.yanlc.anim.view.BounceMaskView;
import com.yanlc.anim.view.SmallBang;

public class MainActivity extends AppCompatActivity {

   private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIcon = (ImageView) findViewById(R.id.icon);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BounceMaskView mask = BounceMaskView.attach2Window(MainActivity.this);
//                mask.bang(view,100);
                SmallBang bang = SmallBang.attach2Window(MainActivity.this);
                bang.bang(view,100,null);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AnimUtils.PropertyShake(mIcon).start();
//                AnimUtils.startWallet(mIcon);
//                AnimUtils.bounceScaleAnim(mIcon).start();
//                AnimUtils.PropertyShakeXY(mIcon,View.TRANSLATION_Y,1000,8).start();
                AnimUtils.propertyRotate(mIcon,1000).start();
            }


        });
    }


}
