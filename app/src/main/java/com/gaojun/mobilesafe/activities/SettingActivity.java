package com.gaojun.mobilesafe.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gaojun.mobilesafe.R;
import com.gaojun.mobilesafe.ui.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private SettingItemView siv_update;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean update = sp.getBoolean("update",false);
        if (update){
            siv_update.setChecked(true);
        }else {
            siv_update.setChecked(false);
        }
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                //判断是否选中
                if (siv_update.isChecked()){
                    //已经打开检查更新
                    siv_update.setChecked(false);
                    editor.putBoolean("update",false);
                }else {
                    //未打开
                    siv_update.setChecked(true);
                    editor.putBoolean("update",true);
                }
                editor.commit();
            }
        });
    }
}
