package com.kkk.myqq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkk.myqq.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkk on 2016/5/22.
 * z3jjlzt.github.io
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    private List<View> mViewList;
    private LayoutInflater inf;
    private TextView mTextView;

    @Bind(R.id.guide_dots_container)
    LinearLayout mGuideDotsContainer;

    @Bind(R.id.viewpager)
    ViewPager mViewpager;


    protected void initVariables() {
        int[] guideImages = {R.layout.guide_1, R.layout.guide_2, R.layout.guide_3};
        mViewList = new ArrayList<>();
        inf = getLayoutInflater();
        for (int i = 0; i < 3; i++) {
            View view = inf.inflate(guideImages[i], null);
            mViewList.add(view);
            if(i == 2){
                View start = view.findViewById(R.id.guide_start);
                mTextView = (TextView) view.findViewById(R.id.tv);
                start.setVisibility(View.VISIBLE);
                start.setOnClickListener(this);
            }
        }
    }


    protected void bindUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        for (int i = 0; i < 3; i++) {
            View dot = inf.inflate(R.layout.item_dot, null);
            if (i == 0) {
                dot.setSelected(true);
            }
            mGuideDotsContainer.addView(dot);
        }
    }


    protected void doLogic() {
        Boolean isFirst;
        SharedPreferences sp = getSharedPreferences("first_in",MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst",true);
        if(isFirst) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirst",false);
            editor.commit();
            PagerAdapter adapter = new MyPageAdapter(mViewList);
            mViewpager.setAdapter(adapter);
            mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    set_doti_select(position);
                }

                private void set_doti_select(int position) {
                    for (int i = 0; i < 3; i++) {
                        mGuideDotsContainer.getChildAt(i).setSelected(false);
                    }
                    mGuideDotsContainer.getChildAt(position).setSelected(true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guide_start:{
                start_anim();
            }
        }
    }

    private void start_anim() {
        ScaleAnimation scaleanim = new ScaleAnimation(1.0f,3.0f,1.0f,3.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleanim.setDuration(1500);
        scaleanim.setFillAfter(true);

        AlphaAnimation alphaanim = new AlphaAnimation(1.0f,0.0f);
        alphaanim.setDuration(1500);
        alphaanim.setFillAfter(true);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleanim);
        set.addAnimation(alphaanim);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start_App();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
       mTextView.startAnimation(set);
    }

    private void start_App() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

class MyPageAdapter extends PagerAdapter {
    private List<View> mViews;

    public MyPageAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

