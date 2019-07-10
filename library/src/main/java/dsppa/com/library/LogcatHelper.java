package dsppa.com.library;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatHelper {

    private static LogcatHelper INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId;

    public static final int LOG_TYPE_UNKNOWN = 9;
    public static final int LOG_TYPE_E = 10;
    public static final int LOG_TYPE_I = 11;
    public static final int LOG_TYPE_D = 12;
    public static final int LOG_TYPE_W = 13;

    private String logGrade = "logcat My App:D *:e *:w *:i *:d -v brief &";
    //private String logGrade = "logcat -b main";
    private String logTag = "";

    private String cmds = null;

    /**
     *
     * 初始化目录
     *
     * */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/dsppa/" + "Log/";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                    + "/dsppa/";
        }
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            boolean val = file.mkdirs();
            if (!val){
                return;
            }
        }
        File file1 = new File(PATH_LOGCAT,"test.log");
        if (!file1.exists()){
            try {
                boolean val = file1.createNewFile();
                if (!val){
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogcatHelper(context);
        }
        return INSTANCE;
    }

    private LogcatHelper(Context context) {
        //init(context);
        mPId = android.os.Process.myPid();
    }

    public void start() {
        if (mLogDumper == null)
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    public void setLogGrade(String grade){
        logGrade = grade;
        if (!TextUtils.isEmpty(logTag)){
            logGrade = logGrade + " -s " + logTag;
        }
        stop();
        start();
    }

    public void setLogTag(String tag){
        logTag = tag;
        if (!TextUtils.isEmpty(logTag)){
            logGrade = logGrade + " -s " + logTag;
        }
        stop();
        start();
    }

    private class LogDumper extends Thread {

        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private String mPID;
        private FileOutputStream out = null;
        private Process logcatProc;

        public LogDumper(String pid, String dir) {
            mPID = pid;
//            try {
//                out = new FileOutputStream(new File(dir, "test.log"));
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            cmds = logGrade;
            //cmds = "logcat -b main";
            //cmds = "logcat My App:D *:e *:w";
            // cmds = "logcat *:e *:w | grep (" + mPID + ")";
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
            // cmds = "logcat -s way";//打印标签过滤信息
            //cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";

        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                //logcatProc = Runtime.getRuntime().exec(new String[]{"logcat","-b","main"});
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);
                String line;
//                byte[] bytes = new byte[10];
//                BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProc.getErrorStream()));
//                String reLine;
//                while ((reLine = reader.readLine()) != null){
//                    if (reLine.length() > 0){
//                        Log.e("TAG",reLine);
//                    }
//                }
//                int rol = logcatProc.getErrorStream().read(bytes);
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.contains(mPID)) {
//                        out.write((getDateEN() + "  " + line + "\n")
//                                .getBytes());
                        if (!TextUtils.isEmpty(line)){
                            if (line.startsWith("E/") || line.startsWith("e/")){
                                LogManager.getInstance().addInfo(new LogInfo(LOG_TYPE_E,line));
                            }else if (line.startsWith("D/") || line.startsWith("d/")){
                                LogManager.getInstance().addInfo(new LogInfo(LOG_TYPE_D,line));
                            }else if (line.startsWith("I/") || line.startsWith("i/")){
                                LogManager.getInstance().addInfo(new LogInfo(LOG_TYPE_I,line));
                            }else if (line.startsWith("W/") || line.startsWith("w/")){
                                LogManager.getInstance().addInfo(new LogInfo(LOG_TYPE_W,line));
                            }else {
                                LogManager.getInstance().addInfo(new LogInfo(LOG_TYPE_UNKNOWN,line));
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }

            }

        }

    }

    public String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31
    }

    public String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

}
