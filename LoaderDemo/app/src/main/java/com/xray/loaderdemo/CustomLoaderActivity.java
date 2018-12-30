package com.xray.loaderdemo;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * 调用自定义Loader的界面
 */
public class CustomLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<UserBean>> {

    private LoaderManager loaderManager;
    private ListView listView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_loader);

        listView = (ListView) findViewById(R.id.list_view);
        userAdapter = new UserAdapter(this);
        listView.setAdapter(userAdapter);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(888,null,this);
    }


    @Override
    public Loader<List<UserBean>> onCreateLoader(int id, Bundle args) {
        //使用自定义Loader
        if(id == 888){
            return new CustomLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<UserBean>> loader, List<UserBean> data) {
        userAdapter.addUsers(data);
    }

    @Override
    public void onLoaderReset(Loader<List<UserBean>> loader) {

    }
}
