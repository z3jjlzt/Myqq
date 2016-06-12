package com.kkk.myqq.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkk.myqq.R;

/**
 * Created by kkk on 2016/5/23.
 * z3jjlzt.github.io
 */
public class Fragment_Friend extends Fragment {
    public interface IFriend {
        public abstract void updateInfo(RecyclerView listView);
    }

    private IFriend mIFriend;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mIFriend = (IFriend) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.friend_lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // mRecyclerView.setOnClickListener();
        mIFriend.updateInfo(mRecyclerView);
        return view;
    }


}
