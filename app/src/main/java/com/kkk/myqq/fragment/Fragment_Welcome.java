package com.kkk.myqq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kkk on 2016/5/22.
 * z3jjlzt.github.io
 */
public class Fragment_Welcome extends Fragment {
    private int id_pic;
    private int id_layout;

    /**
     * @param args 设置图片ID
     */
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.id_pic = args.getInt("pic");
        this.id_layout =args.getInt("layout");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(id_layout,null,false);
       // view.setBackground(getResources().getDrawable(id_pic));
        view.setBackgroundResource(id_pic);
        return view;
    }
}
