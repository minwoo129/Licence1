package com.example.license;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity {
    ImageView intro;
    TextView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        intro = findViewById(R.id.intro);
        background = findViewById(R.id.background);


        handler.sendEmptyMessageDelayed(0, 2000);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            goback();
        }
    };

    void goback() {
        handler.removeMessages(0);
        Intent intent = new Intent();
        setResult(100, intent);
        finish();
    }


}
