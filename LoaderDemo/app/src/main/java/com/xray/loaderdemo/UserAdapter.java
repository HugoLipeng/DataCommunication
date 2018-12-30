package com.xray.loaderdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义数据适配�?
 */
public class UserAdapter extends BaseAdapter{

    private final LayoutInflater inflater;
    private Context context;
    private List<UserBean> users = new ArrayList<>();

    public UserAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addUsers(List<UserBean> userList){
        users.addAll(userList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.user_item,null);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        UserBean userBean = users.get(position);
        holder.usernameTv.setText(userBean.getUserName());
        holder.passwordTv.setText(userBean.getPassword());
        return view;
    }

    class ViewHolder{
        TextView usernameTv;
        TextView passwordTv;
        public ViewHolder(View view){
            usernameTv = (TextView) view.findViewById(R.id.username_tv);
            passwordTv = (TextView) view.findViewById(R.id.password_tv);
            view.setTag(this);
        }
    }
}
