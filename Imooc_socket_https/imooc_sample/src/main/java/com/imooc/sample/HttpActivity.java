package com.imooc.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imooc.sample.biz.TcpClientBiz;
import com.imooc.sample.https.HttpUtils;
import com.imooc.sample.https.HttpUtils2;

public class HttpActivity extends AppCompatActivity {

    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    return;
                }

                HttpUtils2.doGet(getApplicationContext(), url, new HttpUtils2.HttpListener() {
                    @Override
                    public void onSuccess(String content) {
                        mTvContent.setText(content);
                    }

                    @Override
                    public void onFail(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });


    }


    private void initViews() {
        mEtMsg = (EditText) findViewById(R.id.id_et_msg);
        mBtnSend = (Button) findViewById(R.id.id_btn_send);
        mTvContent = (TextView) findViewById(R.id.id_tv_content);


    }


}
