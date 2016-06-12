package com.kkk.myqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kkk.myqq.R;
import com.kkk.myqq.entity.Friend;
import com.kkk.myqq.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkk on 2016/5/25.
 * z3jjlzt.github.io
 */
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.vh> implements View.OnClickListener {

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Friend friend);
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;
    private List<Friend> mFriendList = null;
    private Context context;
    public recyclerAdapter(Context context,List<Friend> FriendList){
        super();
        this.context=context;
        this.mFriendList=FriendList;
    }
    @Override
    public vh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
        view.setOnClickListener(this);
        return new vh(view);
    }

    @Override
    public void onBindViewHolder(vh holder, int position) {
        Friend friend = mFriendList.get(position);
        holder.nick_tv.setText(friend.getName());
        holder.sign_tv.setText(friend.getSignature());
        Glide.with(context)
                .load(Constants.BASE_URL+"/Myqq"+friend.getHead())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.head_iv);
        holder.itemView.setTag(friend);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        mOnRecyclerViewItemClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v, (Friend) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }
   final class vh extends RecyclerView.ViewHolder {
       @Bind(R.id.head_iv)
       ImageView head_iv;
       @Bind(R.id.nick_tv)
       TextView nick_tv;
       @Bind(R.id.sign_tv)
       TextView sign_tv;

        public vh(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
