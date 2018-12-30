# DataCommunication
 Android数据通信开发与应用  
 
## Android数据通信开发与应用（一）：Android组件通信  
### 第一节：广播接收者
一、Broadcast简介
广播是一种广泛运用在应用程序之间传输信息的机制，而BroadcastReceiver是对发送出来的广播进行过滤接收并相应的一类组件。

二、Broadcast机制
在Android里面有各种各样的广播，比如电池的使用状态，电话的接收和短信的接收都会产生一个广播，应用程序开发者也可以监听这些广播并作出程序逻辑的处理。

三、Broadcast注册
静态注册
1、声明一个类继承自BroadcastReceiver，并实现onReceive方法。

```
public class PowerConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_POWER_CONNECTED)){
            Toast.makeText(context, "电源已连接", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "电源已断开", Toast.LENGTH_SHORT).show();
        }
    }
}
```
2、在manifest文件中声明过滤策略，设置优先级。

```
        <receiver android:name=".PowerConnectReceiver">
            <intent-filter>
                <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
                <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED"/>
            </intent-filter>
        </receiver>
```
注意：不是所有的系统广播都支持静态注册。

动态注册  
1、实例化BroadcastReceiver对象，重写onReceive方法。

2、实例化IntentFilter对象，添加action。

3、使用registerReceiver注册BroadcastReceiver对象，使用unregisterReceiver注销BroadcastReceiver对象。  

```
public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Intent.ACTION_POWER_CONNECTED)){
                Toast.makeText(context, "充电器已连接", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "充电器已断开", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver,filter);
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
```
 

四、Broadcast生命周期  
Broadcast生命周期只有十秒左右，如果在onReceive()内做超过十秒的事情，就会报错。  
会出现ANR(Application Not Response)

如何用好BroadcastReceiver  
• 如果需要完成一项比较耗时的工作，应该通过发送Intent给Service，由Service来完成。  
• 这里不能使用子线程来解决，因为BroadcastReceiver的生命周期很短，子线程可能还没结束，Broadcast就先结束了。  

五、自定义广播  
首先在需要发送信息的地方，把要发送的信息和用于过滤的信息(如Action、Category)装入一个Intent对象，然后通过调用 sendBroadcast、sendOrderedBroadcast()或sendStickyBroadcast()方法，把 Intent对象以广播方式发送出去。

　　当Intent发送以后，所有已经注册的BroadcastReceiver会检查注册时的IntentFilter是否与发送的Intent相匹配，若匹配则就会调用BroadcastReceiver的onReceive()方法。

可通过Intent携带消息 :intent.putExtra("msg", "这是我发送的消息！");

1、发送端App设置：

```
mBtnSend.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v){
        Intent intent=new Intent();
        intent.setAction("com.sample.custom.action");
        MainActivity.this.sendBroadcast(intent);
    }
};
```

2、接收端App设置：动态注册、静态注册都可以。

动态注册：

filter.addAction("com.sample.custom.action");
静态注册：  

```
        <receiver android:name=".PowerManageReceiver">
            <intent-filter android:priority="120">
                <action android:name="com.sample.custom.action"/>
            </intent-filter>
        </receiver>
```
启动后台程序  

```
Intent intent=new Intent();
intent.setClass(context,MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
contenct.startActivity(intent);  
```
六、实例--电池检测小程序
电池检测小程序

### 第二节：Application全局应用
Application类简介  
Application是维护应用全局状态的基类。Android系统会在启动应用进程时创建一个Application对象 。其它应用组件可以通过getApplication()、getApplicationContext()访问它。

自定义Application类  
我们可以扩展Application类，让Android系统使用我们自定义的Application类来创建Application对象。
 创建Application子类  
 在清单文件中为 application 标签添加 android:name 属性  

Application对象的生命周期  
Application对象诞生于其它任何组件对象之前，并且一直存活，直到应用进程结束。  

Application对象的回调函数  
Application对象由Android系统管理，它的回调函数都运行于UI线程。  
 onCreate    Application对象创建时调用，可以在这里做一些初始化工作，不要阻塞主线程。  
 onConfigurationChanged    系统配置发生变更时被调用，如屏幕方向变化、系统语言更改。  
 onLowMemory    系统内存吃紧时调用，可以在这里释放内存。  

Application对象的作用  
Application对象全局可访问，且全程陪同应用进程。所以特别适合完成以下任务：  
 共享全局状态 如用户名。  
 初始化全应用所需的服务  

Application对象 vs. 静态单例  
 静态单例模块化程度更好  
 Application就是一个context，所以有访问资源的能力  
 静态单例可以接受context参数  
 Application对象能接收系统回调，自动知悉系统环境变化  
 Application对象的生命周期由系统控制  

### 第三节：Service基础
一、Service简介  
1、Service是Android四大组件之一。  

2、用于后台处理耗时操作，如：下载、音乐播放等。  

3、不受Activity生命周期影响，Service自己有生命周期。  

二、两种Service服务的简单实用  
Service的生命周期有：  onCreate,onStartCommand,onBind,onUnbind,onDestroy

1、启动Service  
  
```
Intent intent1=new Intent(this,MyService.class);
startService(intent1);  
```

第一次创建服务：onCreate->onStartCommand

服务已经创建：onStartCommand （每次启动都会调用onStartCommand）

2、停止服务
  
```
Intent intent2=new Intent(this,MyService.class);
stopService(intent2);  
```

生命周期：onDestroy

3、绑定服务  

```
    ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
 
        }
 
        @Override
        public void onServiceDisconnected(ComponentName name) {
 
        }
    };
Intent intent3=new Intent(this,MyService.class);
bindService(intent3,mConnection,BIND_AUTO_CREATE);  

```
服务未启动：onCreate->onBind  

服务已启动：onBind（只调用一次onBind）  

4、解绑服务  
```
unbindService(mConnection);
```

服务未启动：onUnbind->onDestroy

服务已启动：onUnbind

备注：绑定服务只能创建不能启动，如果服务未启动，解绑或关闭Activity都会销毁服务。

三、利用binderService进行进度监控  
绑定服务：最大作用是用来实现对Service执行的任务进行进度监控。  

进度监控需要用到两个接口：ServiceConnection、IBinder(Binder类实现了IBinder接口)  

1、在Service中开启线程进行耗时操作。  

```
    private int mCount;
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    for (int i = 0; i < 100; i++) {
                        mCount=i;
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }  
```

2、在Service中创建内部类继承自Binder类，创建方法获取进度并在onBind方法中返回该类的实例。  

```
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind: 服务绑定了");
        //throw new UnsupportedOperationException("Not yet implemented");
        return new MyBinder();
    }
 
    class MyBinder extends Binder{
        public int getCount(){
            return mCount;
        }
    }  
```  

3、在ServiceConnection的onServiceConnected方法中读取进度。

```
    ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder= (MyService.MyBinder) service;
            Log.d(TAG, "onServiceConnected: "+myBinder.getCount());
        }
 
        @Override
        public void onServiceDisconnected(ComponentName name) {
 
        }
    };
```    
 



### 第四节：横竖屏切换状态保持
一、Activity的状态  

当横竖屏切换的时候Activity会销毁并重建。  

Activity需要保存和恢复的状态有：所有视图的状态、成员变量的值。Activity在销毁重建过程中会保存视图的状态（不包括自定义视图），但不会保存成员变量。  

如：文本框获取的当前时间会变化。  

```
long createTime=System.currentTimeMillis();
String formatTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(createTime);
mTxtTime.setText(formatTime);  
```

二、应对配置变更

1、限定方向切换。在manifest文件的Activity中加入如下代码：

android:screenOrientation="portrait"//限定竖屏
android:screenOrientation="landscape"//限定横屏
2、自己处理变更。在manifest文件的Activity中加入如下代码：

android:configChanges="orientation|screenSize|keyboard"
发生这三个变化时系统不销毁重建activity。

3、让系统处理变更。

onSaveInstanceState：保存状态。

会调用onSaveInstanceState的情况：

按home键时(Activity切换至后台，系统资源不足时可能销毁掉);

被来电覆盖时(Activity的onStop会调用，系统也可能销毁资源);

横竖屏切换时。

Activity状态保持：Activity销毁时，系统会通过onSaveInstanceState将状态保存到Bundle中，恢复时通过onCreate（需要自己写代码）或onRestoreInstanceState恢复状态。

Fragment状态保持：

1、可以通过setRetainInstance方法通知系统保存Fragment对象。

2、利用Fragment的onSaveInstanceState方法保存状态，利用onCreate或onActivityCreate方法恢复状态，FragmentManager查找到保存的对象。

## Android数据通信开发与应用（二）：Android App通信  
### 第一节：AIDL实现远程服务的通信
AIDL  

全称：（Android Interface definition language），Android接口定义的语言。  

作用：进程间的通信接口。

一、远程服务的开启
1、在服务提供端App的Manifest文件中为service配置action属性  

``` 
<service
            android:name=".MyService"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.imooc.admin.myservice"/>
            </intent-filter>
        </service>  
```  

2、本地App端声明Intent对象，设置action和package，利用startService、BindService启动、绑定服务。

```
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_btn: {
                //Android5.0 以前版本 可以隐式声明
                //Intent intent=new Intent("com.imooc.admin.myservice");
 
                //Android5.0以后版本 必须显示声明
                Intent intent = new Intent();
                intent.setAction("com.imooc.admin.myservice");
                intent.setPackage("com.example.servicedemo");
                startService(intent);
                break;
            }
            case R.id.stop_btn: {
                Intent intent=new Intent();
                intent.setAction("com.imooc.admin.myservice");
                intent.setPackage("com.example.servicedemo");
                stopService(intent);
                break;
            }
            case R.id.bind_btn: {
                Intent intent=new Intent();
                intent.setAction("com.imooc.admin.myservice");
                intent.setPackage("com.example.servicedemo");
                bindService(intent,mConnection,BIND_AUTO_CREATE);
                break;
            }
            case R.id.unbind_btn: {
                unbindService(mConnection);
                break;
            }
        }
    }  
```  

二、远程服务的通信  
远程服务的启动并不能进行通信，要实现通信需要使用AIDL。IBinder也能实现通信，但是IBinder仅限于同一进程，而AIDL是进程间的通信。  

步骤：  

1、在远端创建AIDL文件。  

右键->new->AIDL->AIDL File;  
添加自定义方法，ReBuild。  

```
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
 
    //自定义方法：显示进度
    void showProgress();
}  
``` 

2、实现AIDL。实例化AIDL对象，并在Service的onBind中返回。  

```
   public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind: 服务绑定了");
        //throw new UnsupportedOperationException("Not yet implemented");
        return new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
 
            }
 
            @Override
            public void showProgress() throws RemoteException {
                Log.d(TAG, "showProgress: 当前进度："+mCount);
            }
        };
    }  
```  

3、本地端创建目录，并拷贝远端aidl文件。  

切换project模式，创建目录\src\main\aidl\包名
拷贝aidl文件到目录中，rebuild。  

4、在Serviceconnection 中获取aidl，并调用自定义方法。  

```
    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMyAidlInterface aidlInterface=IMyAidlInterface.Stub.asInterface(service);
            try {
                aidlInterface.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
 
        @Override
        public void onServiceDisconnected(ComponentName name) {
 
        }
    };  
```  

 

### 第二节：Android线程间通信
一、什么是多线程？  
线程是程序中一个单一的顺序控制流程，在单个程序中同时运行多个线程完成不同的工作，称为多线程。  

二、ANR 的基础知识及产生  
应用程序无响应ANR（Application Not Responding）  

1、原因：  
系统繁忙  
app没有优化好  
2、三种情况：  
主要类型案件或触摸事件在特定时间（5秒）内无响应。  
BroadcastReceiver在特定时间（10秒）内无法处理完成。    
小概率类型Service在特定的时间内无法完成处理。  
三、线程  
线程状态：创建、就绪、运行、阻塞、消亡  

线程中常用的方法：  

start方法：启动线程。  
run方法：不需要调用，继承Thread类必须重写  
sleep方法：让当前线程睡眠，交出CPU，让CPU去执行其他的任务  
yield方法：让当前线程交出CPU权限，让CPU去执行其他线程。但是yield不能控制具体交出CPU的时间，另外、yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。调用yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，这一点是和sleep方法不一样的。  
join方法：让调用join方法的线程占用CPU指定的时间或直到线程执行完毕。  
interrupt方法：中断，可以使处于阻塞状态的线程抛出一个异常，也就是说，它可以用来中断一个处于阻塞状态的线程。  
getName方法：获取线程名称。  
currentThread方法：静态方法,获取当前线程。  
提示：多线程会增加一定的系统开销，如，新建线程分配资源、线程切换的状态保存等，所以编程时应确定是否真的需要使用多线程。  

四、实现的两种方式：  
扩展java.lang.Thread类：  
创建类继承Thread或者new Thread。  
实现Runnable接口：创建类实现Runnable接口，new Thread(runnable).start();  
五、线程间通信  
Handler、Looper、MessageQueue、Thread之间的关系  

一个线程对应一个Looper。  
一个Looper对应一个MessageQueue。  
一个Looper可以对应多个Handler。  
主线程默认开启Looper。  
子线程Looper需要手动启动：Looper.prepare();Looper.loop();   
备注：Looper.getMainLooper()；获取主线程的Looper。  

 

### 第三节：Socket&Https通信  
一、什么是Socket  
网络上的两个程序通过一个双向的通信连接实现数据的交换，这个连接的一端称为一个socket（套接字）。IP用来定位主机，Socket用来定位应用。Socket有两种通信模型UDP和TCP，都是传输层协议。  

UDP：  
（User Datagram Protocol 用户数据报协议）应用：视频、语音通讯等。特点是速度快，但是不可靠，容易丢包。   

InetAddress：  
静态方法：  

getByAddress​(byte[] addr) 通过IP地址获取InetAddress  
getByName​(String host) 通过主机名获取InetAddress  
getLocalHost() 创建本地主机的InetAddress  
常用方法：  

getAddress() 返回IP地址，byte[]类型  
getHostName() 返回主机名  
DatagramSocket：  
构造：  

DatagramSocket() 本地默认IP地址，随机端口，常用于客户端。  
DatagramSocket​(int port) 默认IP，指定端口，常用于服务端。  
DatagramSocket​(int port, InetAddress laddr) 指定IP、指定端口。  
常用方法：  

receive​(DatagramPacket p) 从当前socket中接收数据。  
send​(DatagramPacket p) 从当前socket向外发送数据。  
DatagramPacket：  
常用构造：  

DatagramPacket​(byte[] buf, int length) 用于客户端接收数据  
DatagramPacket​(byte[] buf, int length, InetAddress address, int port) 用于服务端发送数据   
常用方法：   

getAddress() 获取IP地址  
getData() 获取数据  
getPort() 获取数据  
getSocketAddress() 获取SocketAddress (IP地址、和端口)  
TCP：  
（Transmission Control Protocol 传输控制协议）应用： 浏览器、邮件、文件传输等。特点速度慢，可靠，占用系统资源高。  

ServerSocket  
构造方法：  

ServerSocket()  
ServerSocket​(int port)  
ServerSocket​(int port, int backlog)  
ServerSocket​(int port, int backlog, InetAddress bindAddr) port服务端要监听的端口，设置0,自动分配；backlog客户端连接请求的队列长度；bindAddr服务端绑定IP，多个网卡时使用。  
常用方法：  

accept() 等待客户端接入，返回客户端的Socket，阻塞状态。  

getInetAddress() 返回IP地址，InetAddress类型。  

getLocalPort() 返回端口，int类型。  

Socket  
构造方法：  

Socket()  
Socket​(String host, int port)  
Socket​(String host, int port, InetAddress localAddr, int localPort)  
Socket​(InetAddress address, int port)  
Socket​(InetAddress address, int port, InetAddress localAddr, int localPort) 参数分别为目标IP、目标端口、绑定本地IP、绑定本地端口。除不带参数的构造外，其它构造函数会尝试建立与服务器的连接。如果成功，则返回Socket对象。  
常用方法：  

getInetAddress() 返回远程服务端的IP地址，InetAddress 类型。   
getPort() 返回远程服务端的端口，整型。  
getLocalAddress() 返回本地IP地址，InetAddress 类型。  
getLocalPort() 返回本地端口，整型。  
getInputStream() 获得输入流，InputStream类型。  
getOutputStream() 获得输入流，OutputStream类型。  
isBound() Socket与本地端口绑定，返回true；否则返回false；  
isClosed() 连接已关闭，返回true；否则返回false  
isConnected() 如果曾经连接过，返回true；否则返回false  
 

Http、Socket区别：  

Http 应用层协议  
Socket传输层协议，灵活高效，服务端推送能力  
网络七层协议（OSI：Open System Interconnection开放系统互联）：  

分别是物理层、数据链路层、网络层、传输层、会话层、表示层、应用层。
Https ：验证证书、校验域名。  

二、UDP实例：服务端、客户端通信   

```   
public class UdpClient {  
    private String mServerIp="172.20.10.4";  
    private InetAddress mServerAddress;  
    private int mServerPort=7777;  
    private DatagramSocket mSocket;  
    private Scanner mScanner;  
 
    public UdpClient() {
        try {
            mServerAddress=InetAddress.getByName(mServerIp);
            mSocket=new DatagramSocket();
            mScanner=new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
 
    public void start(){
        while(true){
            try {
                String clientMsg=mScanner.next();
                byte[] clientMsgBytes=clientMsg.getBytes();
                DatagramPacket clientPacket=new DatagramPacket(clientMsgBytes,clientMsgBytes.length,mServerAddress,mServerPort);
                mSocket.send(clientPacket);
 
                byte[] buf=new byte[1024];
                DatagramPacket serverMsgPacket=new DatagramPacket(buf,buf.length);
                mSocket.receive(serverMsgPacket);
 
                String serverMsg=new String(serverMsgPacket.getData(),0,serverMsgPacket.getLength());
                System.out.println("msg="+serverMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void main(String[] args){
        new UdpClient().start();
    }
}
public class UdpServer {
    private InetAddress mInetAddress;
    private  int mPort = 7777;
    private DatagramSocket mSocket;
    private Scanner mScanner;
 
    public UdpServer() {
        try {
            mInetAddress=InetAddress.getLocalHost();
            mSocket=new DatagramSocket(mPort,mInetAddress);
 
            mScanner=new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
 
    public void start(){
        while(true){
            try {
                //接收
                byte[] buf=new byte[1024];
                DatagramPacket receivedPacket=new DatagramPacket(buf,buf.length);
                mSocket.receive(receivedPacket);//阻塞
 
                //显示
                InetAddress address = receivedPacket.getAddress();
                int port = receivedPacket.getPort();
                byte[] data = receivedPacket.getData();
                String clientMsg=new String(data,0,receivedPacket.getLength());
                System.out.println("address="+address+",port="+port+",msg="+clientMsg);
 
                //发送
                String returnedMsg = mScanner.next();
                DatagramPacket sendPacket=new DatagramPacket(
                        returnedMsg.getBytes(),
                        returnedMsg.getBytes().length,
                        receivedPacket.getSocketAddress());
                mSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void main(String[] args){
        new UdpServer().start();
    }
}  
```  

三、TCP实例：多人聊天室  
服务端   
 
```
public class TcpServer {  
 
    public void start(){
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(9090);
            MsgPool.getInstance().start();
            //等待客户端接入
            while(true) {
                Socket socket = serverSocket.accept();//等待客户端，阻塞状态
                System.out.println("ip=" + socket.getInetAddress().getHostAddress() + ",port=" + socket.getPort() + " is online...");
 
                //两个流都是阻塞状态，直接读取会导致其他客户端的接入进入等待状态。
                //socket.getInputStream();//获取客户端输入流
                //socket.getOutputStream();//获取客户端输出流
 
                //此处为每个接入的客户端单独启动一个线程，进行读写操作
                ClientTask clientTask = new ClientTask(socket);
                MsgPool.getInstance().addListener(clientTask);
                clientTask.start();
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args){
        new TcpServer().start();
    }
}  
```  

服务端子线程：接收客户端发送的信息，并向所有客户端转发  

```
public class ClientTask extends Thread implements MsgPool.MsgComingListener{
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    public ClientTask(Socket socket) {
        try {
            this.mSocket=socket;
            mInputStream=socket.getInputStream();
            mOutputStream=socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void run() {
        super.run();
 
        BufferedReader reader=new BufferedReader(new InputStreamReader(mInputStream));
        String line=null;
        try {
            while((line=reader.readLine())!=null){//readline()阻塞状态
                System.out.println("read="+line);
                //转发消息至其他Socket,将消息添加至队列中
                MsgPool.getInstance().sendMsg(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //向客户端发送消息，被观察者发出通知时执行
    @Override
    public void onMsgComing(String msg) {
        try {
            mOutputStream.write(msg.getBytes());
            mOutputStream.write("\n".getBytes());
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}  
```  

利用消息池，解决服务端向所有客户端转发数据可能出现的并发问题  

```  
public class MsgPool {
    private static MsgPool sInstance=new MsgPool();
    private LinkedBlockingQueue<String> mQueue=new LinkedBlockingQueue<>();//阻塞队列,有消息处理，没消息阻塞
 
    public static MsgPool getInstance(){
        return sInstance;
    }
 
    private MsgPool() {
    }
 
    //观察者模式↓
 
    //观察者对象接口（订阅者接口）
    public interface MsgComingListener{
        public void onMsgComing(String msg);
    }
    //观察者队列（订阅者队列）
    private List<MsgComingListener> mListeners=new ArrayList<>();
 
    //添加观察者（添加订阅者）
    public void addListener(MsgComingListener listener){
        mListeners.add(listener);
        System.out.println("添加观察者"+listener.toString());
    }
 
    //向队列添加消息
    public void sendMsg(String msg){
        try {
            mQueue.put(msg);
            System.out.println("已经添加到消息队列中:"+msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
    //从队列取出消息,独立线程操作
    public void start(){
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        String msg = mQueue.take();//有消息就取出，没有消息就阻塞
                        notifyMsgComming(msg);//通知所有观察者
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
 
    //通知所有观察者
    private void notifyMsgComming(String msg) {
        for(MsgComingListener listener:mListeners){
            listener.onMsgComing(msg);
        }
    }
}  
``` 

客户端  

``` 
public class TcpClient {
    private Scanner mScanner;
    private Socket mSocket;
 
    public TcpClient() {
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }
 
    public void start(){
        try {
            mSocket = new Socket("192.168.1.200",9090);
            InputStream inputStream= mSocket.getInputStream();
            OutputStream outputStream= mSocket.getOutputStream();
 
            final BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream));
 
            //创建子线程，用来处理服务端发送过来的信息
            new Thread(){
                @Override
                public void run() {
                    try {
                        String line=null;
                        while((line=reader.readLine())!=null){
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
 
            //客户端发送消息
            while(true){
                String msg = mScanner.next();
                writer.write(msg);
                writer.newLine();
                writer.flush();
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args){
        new TcpClient().start();
    }
}  
```  

四、Https通信  
 

### 第四节：经典蓝牙通信
一、蓝牙权限：  
执行蓝牙通信需要权限BLUETOOTH，例如：请求链接、接受链接、传输数据等；如果需要启动设备发现或操作蓝牙设置，则需要声明BLUETOOTH_ADMIN权限。  

```
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>  
```  

二、设置蓝牙：  
获取蓝牙适配器：BluetoothAdapter.getDefaultAdapter()  
设备是否支持蓝牙：bluetoothAdapter对象为null，则不支持蓝牙。  
设备是否开启蓝牙：bluetoothAdapter.isEnabled()  
开启蓝牙：startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));  
查找已配对设备：bluetoothAdapter.getBondedDevices()  
发现新设备：bluetoothAdapter.startDiscovery()  
开启蓝牙代码：  

```
    public static final int REQUEST_ENABLE_BT = 10001;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            Toast.makeText(this, "支持蓝牙", Toast.LENGTH_SHORT).show();
            if(bluetoothAdapter.isEnabled()){
                Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }else{
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT){
            Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
        }
    }  
```  

查找设备代码：  

```  
   Set<BluetoothDevice> bluetoothDevices=bluetoothAdapter.getBondedDevices();
                if(bluetoothDevices.size()>0){
                    for(BluetoothDevice device:bluetoothDevices){
                        Log.d(TAG, "Device Name: "+device.getName());
                        Log.d(TAG, "Device Addr: "+device.getAddress());
                    }
                }  
```  

搜索新的设备代码：

```
   //如果正在搜索，则取消
    if(bluetoothAdapter.isDiscovering()){
        bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();//立刻返回是否成功启动发现操作
 

    private BroadcastReceiver mBluetoothReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "New device name: "+device.getName());
                Log.d(TAG, "New device addr: "+device.getAddress());
            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                Log.d(TAG, "Discovery done!");
            }
        }
    };
        IntentFilter filter=new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver,filter);
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothReceiver);
    }  
```  

连接为服务器

设置服务器套接字并接受连接的基本过程：  
-1.通过调用 listenUsingRfcommWithServiceRecord(String, UUID)获取 BluetoothServerSocket  
-2.通过调用 accept() 开始侦听连接请求  
-3.除非要接受更多连接，否则调用 close()  

设置客户端  
- 发起与远程设备（保持开放的服务器套接字的设备）的连接  
a. 首先要获取表示该远程设备的 BluetoothDevice 对象  
b. 然后使用 BluetoothDevice 来获取 BluetoothSocket 并发起连接  
1. 使用 BluetoothDevice，通过调用   createRfcommSocketToServiceRecord(UUID) 获取BluetoothSocket  
2. 通过调用 connect() 发起连接  

 

### 第五节：低功耗蓝牙应用
蓝牙4.0 ：BLE





## 参考 


笔记  <https://blog.csdn.net/zhang_zxk/article/category/8030405>

学习路线 <http://www.imooc.com/article/264731>