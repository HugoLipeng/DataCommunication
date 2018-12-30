package com.imooc.glidetest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

public class ListDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdemo);
        ListView listView = (ListView) findViewById(R.id.lv_list);
        listView.setAdapter(new ImagesAdapter(this, new String[]{
                "http://img.mukewang.com/551b92340001c9f206000338.jpg",
                "http://img.mukewang.com/55237dcc0001128c06000338.jpg",
                "http://img.mukewang.com/5523711700016d1606000338.jpg",
                "http://img.mukewang.com/551e470500018dd806000338.jpg",
                "http://img.mukewang.com/551de0570001134f06000338.jpg",
                "http://img.mukewang.com/552640c300018a9606000338.jpg",
                "http://img.mukewang.com/551b98ae0001e57906000338.jpg",
                "http://img.mukewang.com/550b86560001009406000338.jpg",
                "http://img.mukewang.com/551916790001125706000338.jpg",
                "http://img.mukewang.com/5518ecf20001cb4e06000338.jpg",
                "http://img.mukewang.com/5518bbe30001c32006000338.jpg",
                "http://img.mukewang.com/551380400001da9b06000338.jpg",
                "http://img.mukewang.com/550a33b00001738a06000338.jpg",
                "http://img.mukewang.com/5513a1b50001752806000338.jpg"
        }));
    }

    public class ImagesAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private String[] imageUrls;

        public ImagesAdapter(Context context, String[] imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return imageUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item, parent, false);
            Glide.with(context)
                    .load(imageUrls[position])
                    .into((ImageView) convertView);
            return convertView;
        }
    }
}
