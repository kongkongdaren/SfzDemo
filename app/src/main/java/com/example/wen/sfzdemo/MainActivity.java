package com.example.wen.sfzdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * Created by jwj on 2018/1/10.
 */

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 头像面
        findViewById(R.id.idcardheadbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        DataFileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

            }
        });
        // 初始化
        initAccessTokenWithAkSk();

    }
    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                Log.d("MainActivity", "onResult: " + accessToken.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "初始化认证成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(OCRError ocrError) {
                ocrError.printStackTrace();
                Log.e("MainActivity", "onError: " + ocrError.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "初始化认证失败,请检查 key", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, getApplicationContext(),
                // 根据自己的包名，去百度云自行配置
                "avXvTAghUgiG4k7k3jeB3vds",
                // 根据自己的包名，去百度云自行配置
                "2yRmOb3x0cp5e1mdHaRKnIKx9qng3hpO");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = DataFileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
//                        身份证头像面信息
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("cardtype", IDCardParams.ID_CARD_SIDE_FRONT);
                        intent.putExtra("cardimage",filePath);
                        startActivity(intent);
                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }


}
