package com.galaxybruce.component.app.crash;

import android.content.DialogInterface;
import android.os.Bundle;

import com.blankj.utilcode.util.AppUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ExceptionActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AlertDialog.Builder(this)
                .setMessage(getIntent().getStringExtra("message"))
                .setCancelable(false)
                .setPositiveButton("重启APP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.relaunchApp(true);
                    }
                })
                .create()
                .show();
    }
}