package com.gaojun.mobilesafe.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.gaojun.mobilesafe.R;
import com.gaojun.mobilesafe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;
    private TextView tv_splash_version;
    private TextView tv_update_info;
    private String description;
    private String apkurl;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //检查升级
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                //申请权限
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},RESULT_CANCELED);
            }
        }
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号:" + getVersionName());
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        boolean update = sp.getBoolean("update", true);
        if (update) {

            checkUpdate();
        } else {
            //不检查更新
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进入主页面
                    enterHome();
                }
            }, 2000);
        }
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(aa);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            Toast.makeText(this,"授权成功",Toast.LENGTH_LONG).show();
        } else {
            // Permission Denied
            Toast.makeText(this,"授权失败",Toast.LENGTH_LONG).show();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:
                    showUpdateDialog();
                    Log.i(TAG, "显示升级的对话框");
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "JSON错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(getApplicationContext(), "url错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
        //this = Activity.this
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示升级");
//		builder.setCancelable(false);//强制升级
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                //进入主页面
                enterHome();
                dialog.dismiss();

            }
        });
        builder.setMessage(description);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载APK，并且替换安装
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // sdcard存在
                    // afnal
                    FinalHttp finalhttp = new FinalHttp();
                    finalhttp.download(apkurl, Environment
                                    .getExternalStorageDirectory().getAbsolutePath() + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {

                                @Override
                                public void onFailure(Throwable t, int errorNo,
                                                      String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    // TODO Auto-generated method stub
                                    super.onLoading(count, current);
                                    tv_update_info.setVisibility(View.VISIBLE);
                                    tv_update_info.setVisibility(View.VISIBLE);
                                    //当前下载百分比
                                    int progress = (int) (current * 100 / count);
                                    tv_update_info.setText("下载进度：" + progress + "%");
                                }

                                @Override
                                public void onSuccess(File t) {
                                    // TODO Auto-generated method stub
                                    super.onSuccess(t);
                                    installAPK(t);
                                }

                                /**
                                 * 安装APK
                                 * @param t
                                 */
                                private void installAPK(File t) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");

                                    startActivity(intent);

                                }


                            });
                } else {
                    Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                enterHome();// 进入主页面
            }
        });
        builder.show();

    }

    /**
     * 进入Home页面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        new Thread() {

            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.serverurl));
                    //联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //联网成功
                        InputStream is = conn.getInputStream();
                        //把流转成String
                        String result = StreamTools.readFromStream(is);
                        Log.d(TAG, result);

                        JSONObject obj = new JSONObject(result);
                        String version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        //校验是否有新版本
                        if (getVersionName().equals(version)) {
                            //版本一致,进入主界面
                            msg.what = ENTER_HOME;
                        } else {
                            //弹出升级对话框
                            msg.what = SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long useTime = endTime - startTime;
                    if (useTime < 2000) {
                        try {
                            Thread.sleep(2000 - useTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    mHandler.sendMessage(msg);
                }

            }
        }.start();
    }

    /**
     * 得到版本名称
     */
    private String getVersionName() {
        //用来管理手机的APK
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
