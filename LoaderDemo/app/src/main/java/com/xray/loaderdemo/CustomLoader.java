package com.xray.loaderdemo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Loader，加载UserBean数据集合
 */
public class CustomLoader extends AsyncTaskLoader<List<UserBean>> {

    public CustomLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //Loader开始执行后，强制调用loadInBackground()方法
        if(isStarted()){
            forceLoad();
        }
    }

    /**
     * 在子线程加载数据
     * @return
     */
    @Override
    public List<UserBean> loadInBackground() {
        List<UserBean> users = new ArrayList<>();
        users.add(new UserBean("zhangsan","123456"));
        users.add(new UserBean("lisi","123456"));
        users.add(new UserBean("wangwu","123456"));
        users.add(new UserBean("zhaoliu","123456"));
        return users;
    }

}
