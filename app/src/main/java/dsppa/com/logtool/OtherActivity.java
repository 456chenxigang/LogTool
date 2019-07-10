package dsppa.com.logtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OtherActivity extends AppCompatActivity {

    private static final String TAG = "OtherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Log.i(TAG,"i info");
        Log.d(TAG,"debug info");
        Log.e(TAG,"error info");
        Log.w(TAG,"a=warm info");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OtherActivity.this,"onClick",Toast.LENGTH_SHORT).show();
                Log.e(TAG,"onClick");
            }
        });
    }
}
