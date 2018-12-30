package com.zhy.imooc_butter_knife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class CategoryActivity extends AppCompatActivity
{

    @BindView(R.id.id_listview)
    ListView mListView;

    private List<String> mData = new ArrayList<>(Arrays.asList("Simple Use", "RecyclerView Use"));


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);

        mListView.setAdapter(new CategoryAdapter(this, mData));

    }

    @OnItemClick(R.id.id_listview)
    public void itemClicked(int position)
    {
        Intent intent = null;
        switch (position)
        {
            case 0:
                intent = new Intent(this, MainActivity.class);
                break;
            case 1:
                intent = new Intent(this, RecyclerViewActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
