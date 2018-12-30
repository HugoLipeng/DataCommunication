package com.xray.loaderdemo;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private LoaderManager loaderManager;
    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 初始化ListView，给ListView设置游标适配器

        listView = (ListView) findViewById(R.id.contacts_list_view);

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(simpleCursorAdapter);

        //获得LoaderManager
        loaderManager = getSupportLoaderManager();
        //初始化Loader，参数1 Loader的ID，参数2 给Loader的参数，参数3 Loader回掉接口
        loaderManager.initLoader(999, null, this);
    }

    /**
     * 创建Loader的回掉方法
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //返回Loader对象
        if(id == 999){
            return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        }
        return null;
    }

    /**
     * 数据加载完成的回掉方法
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //更新适配器的游标数据
        Cursor cursor = simpleCursorAdapter.swapCursor(data);
        //关闭旧的游标
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //释放当前游标
        Cursor cursor = simpleCursorAdapter.swapCursor(null);
        if(cursor != null){
            cursor.close();
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(this,CustomLoaderActivity.class));
    }
}
