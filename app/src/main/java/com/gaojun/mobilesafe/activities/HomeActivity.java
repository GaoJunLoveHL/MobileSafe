package com.gaojun.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaojun.mobilesafe.R;
import com.gaojun.mobilesafe.utils.Md5Utils;

public class HomeActivity extends Activity {

    private GridView list_home;
    private MyAdapter myAdapter;

    private SharedPreferences sp;

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
        sp = getSharedPreferences("cinfig",MODE_PRIVATE);
        list_home = (GridView) findViewById(R.id.list_home);
        myAdapter = new MyAdapter();
        list_home.setAdapter(myAdapter);

        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗页面
                        showLostFindDialog();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8://进入设置界面
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    private void showLostFindDialog() {
        //判断是否设置过密码
        if (pwdExist()){
            //有密码
            showEnterDialog();
        }else {
            //无密码
            showSetPwdDialog();
        }
    }
    private EditText edt_pwd_first,edt_pwd_second;
    private Button btn_pwd_enter,btn_pwd_cancel;
    private AlertDialog dialog;
    /**
     * 设置密码
     */
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自定义布局文件
        View view = View.inflate(this,R.layout.dialog_setup_pwd,null);
        edt_pwd_first = (EditText) view.findViewById(R.id.edt_pwd_first);
        edt_pwd_second = (EditText) view.findViewById(R.id.edt_pwd_second);
        btn_pwd_enter = (Button) view.findViewById(R.id.btn_pwd_enter);
        btn_pwd_cancel = (Button) view.findViewById(R.id.btn_pwd_cancel);
        btn_pwd_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置密码
                String pwd1 = edt_pwd_first.getText().toString().trim();
                String pwd2 = edt_pwd_second.getText().toString().trim();
                if (TextUtils.isEmpty(pwd1)||TextUtils.isEmpty(pwd2)){
                    Toast.makeText(HomeActivity.this,"密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断两次输入是否一样
                if (pwd1.equals(pwd2)){
                    //一样保存密码，关掉dialog，进入页面
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", Md5Utils.sha1Password(pwd1));
                    editor.commit();
                    dialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this,
                            LostFindActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(HomeActivity.this,"密码不一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btn_pwd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view,0,0,0,0);
        dialog.show();
    }

    /**
     * 输入密码
     */
    private void showEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自定义布局文件
        View view = View.inflate(this,R.layout.dialog_enter_pwd,null);
        edt_pwd_first = (EditText) view.findViewById(R.id.edt_pwd_first);
        btn_pwd_enter = (Button) view.findViewById(R.id.btn_pwd_enter);
        btn_pwd_cancel = (Button) view.findViewById(R.id.btn_pwd_cancel);
        btn_pwd_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置密码
                String pwd1 = edt_pwd_first.getText().toString().trim();
                String pwd2 = sp.getString("password","");
                if (TextUtils.isEmpty(pwd1)){
                    Toast.makeText(HomeActivity.this,"密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断输入是否一样
                if (Md5Utils.sha1Password(pwd1).equals(pwd2)){
                    //一样保存密码，关掉dialog，进入页面
                    dialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this,
                            LostFindActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(HomeActivity.this,"密码错误",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btn_pwd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view,0,0,0,0);
        dialog.show();
    }

    /**
     * 判断是否设置过密码
     * @boolean
     */
    private boolean pwdExist(){
        String pwd = sp.getString("password",null);
        return !TextUtils.isEmpty(pwd);
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
