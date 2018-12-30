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



笔记  <https://blog.csdn.net/zhang_zxk/article/category/8030405>

学习路线 <http://www.imooc.com/article/264731>