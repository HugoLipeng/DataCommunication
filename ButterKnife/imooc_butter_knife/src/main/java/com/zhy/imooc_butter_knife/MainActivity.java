package com.zhy.imooc_butter_knife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.id_tv)
     TextView mTv;
    @BindView(R.id.id_btn1)
    Button mBtn1;
    @BindView(R.id.id_btn2)
    Button mBtn2;

    @BindString(R.string.hello_world)
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mTv.setText(str);
        mBtn1.setText("Hello");
        mBtn2.setText("Imooc");

    }

    @OnClick({R.id.id_btn1, R.id.id_btn2})
    public void btnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.id_btn1:
                Toast.makeText(this, "Btn1 Clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_btn2:
                Toast.makeText(this, "Btn2 Clicked!", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
