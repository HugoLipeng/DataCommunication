package com.imooc.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imooc.sample.biz.UdpClientBiz;

public class UdpActivity extends AppCompatActivity {

    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvContent;

    private UdpClientBiz mUdpClientBiz = new UdpClientBiz();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    return;
                }

                appendMsgToContent("client:" + msg);
                mEtMsg.setText("");
                mUdpClientBiz.sendMsg(msg, new UdpClientBiz.OnMsgReturnedListener() {
                    @Override
                    public void onMsgReturned(String msg) {
                        appendMsgToContent("server:" + msg);
                    }

                    @Override
                    public void onError(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
    }

    private void appendMsgToContent(String msg) {
        mTvContent.append(msg + "\n");
    }

    private void initViews() {
        mEtMsg = (EditText) findViewById(R.id.id_et_msg);
        mBtnSend = (Button) findViewById(R.id.id_btn_send);
        mTvContent = (TextView) findViewById(R.id.id_tv_content);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUdpClientBiz.onDestroy();
    }
}
