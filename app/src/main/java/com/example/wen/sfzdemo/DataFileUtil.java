package com.example.wen.sfzdemo;

import android.content.Context;

import java.io.File;

/**
 * Created by jwj on 2018/1/10.
 */

public class DataFileUtil {

    public static File getSaveFile(Context context) {
        return new File(context.getFilesDir(), "pic.jpg");
    }
}
