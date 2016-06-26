package com.gaojun.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaojun.mobilesafe.R;

/**
 * Created by Administrator on 2016/6/25.
 */
public class SettingItemView extends RelativeLayout{
    private CheckBox cb_status;
    private TextView tv_desc;
    private TextView tv_title;
    private String title,desc_on,desc_off;

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.SettingItemView);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            switch (i){
                case R.styleable.SettingItemView_mtitle:
                    title = array.getString(i);
                    tv_title.setText(title);
                    break;
                case R.styleable.SettingItemView_desc_on:
                    desc_on = array.getString(i);
                    break;
                case R.styleable.SettingItemView_desc_off:
                    desc_off = array.getString(i);
                    break;
            }
        }
        setDesc(desc_off);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context c) {
        View v =  View.inflate(c, R.layout.setting_item_view,this);
        cb_status = (CheckBox) v.findViewById(R.id.cb_status);
        tv_desc = (TextView) v.findViewById(R.id.tv_desc_item);
        tv_title = (TextView) v.findViewById(R.id.tv_title_item);
    }

    /**
     * 检验checkbox状态
     */
    public boolean isChecked(){
        return cb_status.isChecked();
    }
    /**
     * 设置checkbox状态
     */
    public void setChecked(Boolean checked){
        if (checked){
            setDesc(desc_on);
        }else {
            setDesc(desc_off);
        }
        cb_status.setChecked(checked);
    }
    /**
     * 控件的描述信息
     */
    public void setDesc(String s){
        tv_desc.setText(s);
    }
}
