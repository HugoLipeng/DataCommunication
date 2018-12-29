package com.imooc.sample.biz;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class TcpClientBiz {

    private Socket mSocket;
    private InputStream mIs;
    private OutputStream mOs;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());


    public interface OnMsgComingListener {
        void onMsgComing(String msg);

        void onError(Exception ex);
    }

    private OnMsgComingListener mListener;

    public void setOnMsgComingListener(OnMsgComingListener listener) {
        mListener = listener;
    }


    public TcpClientBiz() {

        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket("192.168.1.107", 9090);
                    mIs = mSocket.getInputStream();
                    mOs = mSocket.getOutputStream();

                    readServerMsg();
                } catch (final IOException e) {

                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onError(e);
                            }
                        }
                    });
                }
            }
        }.start();


    }

    private void readServerMsg() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(mIs));

        String line = null;
        while ((line = br.readLine()) != null) {
            final String finalLine = line;
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onMsgComing(finalLine);
                    }
                }
            });

        }
    }


    public void sendMsg(final String msg) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mOs));
                    bw.write(msg);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void onDestroy() {

        try {
            if (mIs != null) {
                mIs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mOs != null) {
                mOs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
