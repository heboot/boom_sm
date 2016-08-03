package com.codingfeel.sm.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Heboot on 16/5/20.
 */
public class DownloadUtils {
    static int curProgress = 0;

    public static void downloadFile(final Activity context, final String apkurl, int totalSize, final MaterialDialog pb) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkurl);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setConnectTimeout(10 * 1000); //超时时间
                    connection.connect();  //连接
                    long fileLength = connection.getContentLength();
                    if (connection.getResponseCode() == 200) { //返回的响应码200,是成功.
                        File file = new File(Environment.getExternalStorageDirectory() + "/codeboom.apk");   //这里我是手写了。建议大家用自带的类
                        file.createNewFile();
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); //缓存
                        byte[] buffer = new byte[1024 * 10];
                        long readedLength = 0l;
                        while (true) {
                            int len = inputStream.read(buffer);
//                            MLog.e("DownloadUtils", "===>" + len);
                            readedLength += len;
                            if (curProgress < 100) {
                                curProgress = (int) (((float) readedLength / fileLength) * 100);
                            } else {
                                curProgress = 100;
                            }


//                            context.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    MLog.e("DownloadUtils", "===>" + curProgress);
//                                    pb.setProgress(curProgress);
//                                }
//                            });
                            pb.incrementProgress(curProgress);
                            pb.setProgress(curProgress);
                            if (len == -1) {
                                break;  //读取完
                            }
                            arrayOutputStream.write(buffer, 0, len);  //写入
                        }
                        arrayOutputStream.close();
                        inputStream.close();

                        byte[] data = arrayOutputStream.toByteArray();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(data); //记得关闭输入流
                        fileOutputStream.close();

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/codeboom.apk")),
                                "application/vnd.android.package-archive");
                        context.startActivity(intent);
                        pb.dismiss();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private static Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };


}
