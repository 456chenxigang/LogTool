package dsppa.com.logtool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import dsppa.com.library.LogManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView openTv;

    private MyHandler handler = new MyHandler();

    private TextView logTv;

    private static final int MSG_TYPE_E = 19;
    private static final int MSG_TYPE_I = 20;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_TYPE_E:
                    Log.e(TAG, "is error log");

                    handler.sendEmptyMessageDelayed(MSG_TYPE_E, 4000);
                    break;
                case MSG_TYPE_I:
                    Log.i(TAG, "is info log");
                    handler.sendEmptyMessageDelayed(MSG_TYPE_I, 2000);
                    break;
                default:
                    break;
            }
            //recyclerView.scrollToPosition(logInfoList.size() - 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.sendEmptyMessageDelayed(MSG_TYPE_E, 4000);
        handler.sendEmptyMessageDelayed(MSG_TYPE_I, 2000);

        openTv = findViewById(R.id.openTv);

        openTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LogManager.getInstance().isShowing()){
                    LogManager.getInstance().close();
                }else {
                    LogManager.getInstance().show();
                }
            }
        });

        openTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this,OtherActivity.class));
                return false;
            }
        });

    }

}
