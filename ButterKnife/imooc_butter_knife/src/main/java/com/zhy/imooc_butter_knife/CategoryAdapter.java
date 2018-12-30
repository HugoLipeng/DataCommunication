package com.zhy.imooc_butter_knife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryAdapter extends ArrayAdapter<String>
{
    private LayoutInflater mInflater;

    public CategoryAdapter(Context context, List<String> objects)
    {
        super(context, -1, objects);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(getItem(position));
        return convertView;
    }

    static class ViewHolder
    {
        @BindView(R.id.id_title_tv)
        TextView mTextView;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }

    }
}
