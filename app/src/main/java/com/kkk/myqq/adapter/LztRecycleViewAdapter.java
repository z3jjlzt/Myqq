package com.kkk.myqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkk.myqq.R;
import com.kkk.myqq.entity.Msg;

import java.util.List;

/**
 * @param <T> VH泛型
 * @param <K> LIST泛型
 * @author z3jjlzt 2015年12月4日
 */
public abstract class LztRecycleViewAdapter<T extends ViewHolder, K> extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<K> list;
    private int layout_id;


    public LztRecycleViewAdapter(Context context, int layout_id, List<K> list) {
        this.context = context;
        this.layout_id = layout_id;
        this.list = list;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        BindVh((T) arg0, arg1);
    }

    @Override
    public int getItemViewType(int position) {
        Boolean isLeft = ((Msg) list.get(position)).isLeft();

        return isLeft ? 0 : 1;
    }

    @Override
    public T onCreateViewHolder(ViewGroup arg0, int arg1) {
        View l = LayoutInflater.from(context).inflate(layout_id, null, false);
        View r = LayoutInflater.from(context).inflate(R.layout.chat_right, null, false);
        if (arg1 == 0) {
            return createVh(l);
        } else {
            return createVh(r);
        }
    }

    /**
     * 在onCreateViewHolder中调用
     *
     * @param v viewholder关联的视图
     * @return
     */
    public abstract T createVh(View v);

    /**
     * 在onBindViewHolder中调用
     *
     * @param viewholder VH
     * @param position   位置
     */
    public abstract void BindVh(T viewholder, int position);

}