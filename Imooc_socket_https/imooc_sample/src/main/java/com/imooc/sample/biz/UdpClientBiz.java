package com.imooc.sample.biz;

import android.os.Handler;
import android.os.Looper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class UdpClientBiz {

    private String mServerIp = "192.168.1.107";
    private InetAddress mServerAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());


    public UdpClientBiz() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnMsgReturnedListener {
        void onMsgReturned(String msg);

        void onError(Exception ex);
    }

    public void sendMsg(final String msg, final OnMsgReturnedListener listener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    byte[] clientMsgBytes = msg.getBytes();
                    DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                            clientMsgBytes.length, mServerAddress, mServerPort);

                    mSocket.send(clientPacket);

                    // receive msg
                    byte[] buf = new byte[1024];
                    DatagramPacket serverMsgPacket = new DatagramPacket(buf, buf.length);
                    mSocket.receive(serverMsgPacket);

                    final String serverMsg = new String(serverMsgPacket.getData(),
                            0, serverMsgPacket.getLength());

                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onMsgReturned(serverMsg);
                        }
                    });

                } catch (final Exception e) {
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(e);
                        }
                    });
                }
            }
        }.start();

    }

    public void onDestroy() {
        if (mSocket != null) {
            mSocket.close();
        }
    }

}
