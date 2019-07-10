package dsppa.com.library;

import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogManager {

    private static final String TAG = "LogManager";

    private static final int MSG_TYPE_ADD = 10;
    private static final int MSG_TYPE_REMOVE = 11;

    private static LogManager instance = null;
    private MyRecyclerView recyclerView;
    private LogRecyclerViewAdapter adapter;
    private boolean isStop = true;

    private String logGrade = "logcat My App:D *:e *:w *:i *:d -v brief &";
    private String logTag = "";

    private List<LogInfo> logInfoList = Collections.synchronizedList(new ArrayList<LogInfo>());

    private Context context;

    private LinearLayout layoutView;
    LinearLayout bottomLayout;

    public synchronized static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void init(Context context) {

        this.context = context;

        initLogWindow();

        adapter = new LogRecyclerViewAdapter(context, logInfoList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private MyHandler handler = new MyHandler();
    private class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_TYPE_ADD) {
                if (adapter != null) {
                    adapter.setDates(logInfoList);
                }
            } else if (msg.what == MSG_TYPE_REMOVE) {
                if (adapter != null) {
                    adapter.setDates(null);
                }
            }
        }
    }

    private void initLogWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.log_layout, null);
        float widthScale = 0.6f;
        float heighScale = 0.6f;
        FloatWindow
                .with(context)
                .setMoveType(MoveType.active)
                .setView(view)
                .setWidth(Screen.width, widthScale)                               //设置控件宽高
                .setHeight(Screen.height, heighScale)
                .setX(0)                                   //设置控件初始位置
                .setY(100)
                .setDesktopShow(false)                        //桌面显示
                .setViewStateListener(new ViewStateListener() {
                    @Override
                    public void onPositionUpdate(int i, int i1) {
                        Log.i(TAG, "onPositionUpdate");
                    }

                    @Override
                    public void onShow() {
                        Log.i(TAG, "onShow");
                        if (isStop) {
                            close();
                        }
                    }

                    @Override
                    public void onHide() {
                        Log.i(TAG, "onHide");
                    }

                    @Override
                    public void onDismiss() {
                        Log.i(TAG, "onDismiss");
                    }

                    @Override
                    public void onMoveAnimStart() {
                        Log.i(TAG, "onMoveAnimStart");
                    }

                    @Override
                    public void onMoveAnimEnd() {
                        Log.i(TAG, "onMoveAnimEnd");
                    }

                    @Override
                    public void onBackToDesktop() {
                        Log.i(TAG, "onBackToDesktop");
                    }
                })
                .build();
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutView = view.findViewById(R.id.layout);

        view.findViewById(R.id.clearTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logInfoList != null){
                    logInfoList.clear();
                }
                handler.sendEmptyMessage(MSG_TYPE_REMOVE);
            }
        });
        final Spinner logGradeSp = view.findViewById(R.id.log_grade_sp);
        logGradeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: //v
                        logGrade = "logcat My App:D *:e *:w *:i *:d -v brief &";
                        break;
                    case 1: //d
                        logGrade = "logcat My App:D *:e *:w *:i *:d -v brief &";
                        break;
                    case 2: //i
                        logGrade = "logcat My App:D *:e *:w *:i -v brief &";
                        break;
                    case 3: //w
                        logGrade = "logcat My App:D *:e *:w -v brief &";
                        break;
                    case 4: //e
                        logGrade = "logcat My App:D *:e -v brief &";
                        break;
                        default:
                            break;
                }
                LogcatHelper.getInstance(context).setLogGrade(logGrade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText editText = view.findViewById(R.id.filter_et);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        layoutView.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                        ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        editText.requestFocus();
                        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null){
                            Log.e(TAG,"isShow:"+imm.showSoftInput(editText,0));
                            imm.showSoftInput(editText, 0);
                            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                    }
                },500);
            }
        });
        bottomLayout = view.findViewById(R.id.bottom_layout);
        setViewVisible(View.GONE);
    }

    public void show() {
        if (FloatWindow.get() == null) {
            return;
        }
        isStop = false;
        setViewVisible(View.VISIBLE);
        LogcatHelper.getInstance(context).start();

        FloatWindow.get().show();
    }

    public void close() {
        if (FloatWindow.get() == null) {
            return;
        }
        isStop = true;
        setViewVisible(View.GONE);
        LogcatHelper.getInstance(context).stop();
        FloatWindow.get().hide();
        if (logInfoList != null) {
            logInfoList.clear();
        }
    }

    public boolean isShowing() {
        if (FloatWindow.get() == null) {
            return false;
        }
        Log.e("TAG", "isShow:" + FloatWindow.get().isShowing());
        return FloatWindow.get().isShowing();
    }

    private void destroy() {
        if (logInfoList != null) {
            logInfoList.clear();
        }
        LogcatHelper.getInstance(context).stop();
        FloatWindow.destroy();
    }

    public void addInfo(LogInfo info) {
        if (isStop) {
            return;
        }
        if (logInfoList != null) {
            logInfoList.add(0, info);
        }
        handler.sendEmptyMessage(MSG_TYPE_ADD);
    }

    public String getLogGrade(){
        return  logGrade;
    }

    public String getLogTag(){
        return logTag;
    }

    private void setViewVisible(int value) {
        recyclerView.setVisibility(value);
        layoutView.setVisibility(value);
        bottomLayout.setVisibility(value);
        FloatWindow.get().getView().setFocusableInTouchMode(false);
    }
}
