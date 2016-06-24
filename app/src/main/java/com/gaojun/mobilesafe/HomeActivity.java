package com.gaojun.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

    private GridView list_home;
    private MyAdapter myAdapter;
    private static String[] names = {
            "手机防盗","通讯卫士","软件管理",
            "进程管理","流量统计","手机杀毒",
            "缓存清理","高级工具","设置中心"
    };
    private static int[] ids = {
        R.mipmap.safe,R.mipmap.callmsgsafe,R.mipmap.app,
        R.mipmap.taskmanager,R.mipmap.netmanager,R.mipmap.trojan,
        R.mipmap.sysoptimize,R.mipmap.atools,R.mipmap.settings,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list_home = (GridView) findViewById(R.id.list_home);
        myAdapter = new MyAdapter();
        list_home.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =  View.inflate(HomeActivity.this,R.layout.list_item_home,null);
            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

            iv_item.setImageResource(ids[position]);
            tv_item.setText(names[position]);
            return view;
        }
    }
}
