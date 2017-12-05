package xu.qiwei.com.jpushtest;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button handler_test_button;
    private TextView padinfo_textview;
    private TextView test_textview;
    private TextView test_textview_2;
    private Button canvas_test_button;
    private EditText input_edittext;
    private Button cacular_button;
    private Button surfaceview_test_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
        initEvents();
    }

    private void initViews() {
        handler_test_button = (Button) findViewById(R.id.handler_test_button);
        padinfo_textview = (TextView) findViewById(R.id.padinfo_textview);
        test_textview=(TextView)findViewById(R.id.test_textview);
        test_textview_2 = (TextView)findViewById(R.id.test_textview_2);
        canvas_test_button = (Button)findViewById(R.id.canvas_test_button);
        input_edittext = (EditText) findViewById(R.id.input_edittext);
        cacular_button = (Button)findViewById(R.id.cacular_button);
        surfaceview_test_button = (Button)findViewById(R.id.surfaceview_test_button);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Log.e("x==y", point.toString());
        int xx=getResources().getDisplayMetrics().densityDpi;
        Log.e("dpi==",  xx+ "");

        String fbl = point.toString();
        String dpi = xx+"";
        String finalStr = new StringBuffer().append("分辨率：").append(fbl).append("\n")
                .append("dpi:").append(dpi).toString();
        padinfo_textview.setText(finalStr);
        test_textview.post(new Runnable() {
            @Override
            public void run() {
                Log.e("test==",test_textview.getWidth()+"=="+test_textview.getHeight());
            }
        });
        test_textview_2.post(new Runnable() {
            @Override
            public void run() {
                Log.e("test2==",test_textview_2.getWidth()+"=="+test_textview_2.getHeight());

            }
        });

    }

    private void initDatas() {

    }

    private void initEvents() {
        handler_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HandlerTestActivity.class));
            }
        });
        canvas_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CanvasActivity.class));
            }
        });
        cacular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] test = new byte[]{125};
//                int inputno = Integer.parseInt(input_edittext.getText().toString());
                cacular_button.setText(getWaveY(test[0])+"");
            }
        });
        surfaceview_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DrawPathTestActivity.class));

            }
        });
    }
    private int getWaveY(int originaly) {

        return Utils.bytesToInt(new byte[]{(byte) originaly, 0, 0, 0}, 0);
    }
}
