package xu.qiwei.com.jpushtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioGroupTestActivity extends AppCompatActivity {

    private RadioGroup test_radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_group_test);
        initDatas();
        initViews();
        initEvents();
    }

    private void initEvents() {

    }

    private void initViews() {
        //获取控件并修改
        test_radiogroup = (RadioGroup) findViewById(R.id.test_radiogroup);
        for (int i = 5; i > 0; i--) {
            test_radiogroup.addView(generageRadioBtn(i+""));
        }


    }

    private RadioButton generageRadioBtn(String txt) {

        RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.test_radiobtn_layout, null, false);
        radioButton.setText(txt);
        return radioButton;

    }

    private void initDatas() {

    }
}
