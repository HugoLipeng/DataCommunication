package imooc.android.com.changeCase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NdkUtils().convert("/storage/emulated/0/imooc_lower.txt", "/storage/emulated/0/imooc_upper.txt");
    }

    static {
        System.loadLibrary("changeCase");
    }

}
