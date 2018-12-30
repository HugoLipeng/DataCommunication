package com.zhy.imooc_butter_knife;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder>
{
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public RvAdapter(Context context, List<String> datas)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new RvAdapter.ViewHolder(mInflater.inflate(R.layout.item_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        viewHolder.mTextView.setText(mDatas.get(i));
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.id_title_tv)
        TextView mTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
