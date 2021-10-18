package com.example.license;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMemoActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    TextView cancelTv, saveTv;
    EditText titleEt, contentEt;
    Button clearBt, deleteBt;
    private Account userAccount = null;

    int idx = 0;
    int pos1 = 0;

    int mode = 0;
    int ADD_MEMO = 100;
    int DELETE_MEMO = 200;
    int UPDATE_MEMO = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        cancelTv = findViewById(R.id.cancelTv);
        saveTv = findViewById(R.id.saveTv);
        titleEt = findViewById(R.id.titleEt);
        contentEt = findViewById(R.id.contentEt);
        clearBt = findViewById(R.id.clearBt);
        deleteBt = findViewById(R.id.deleteBt);

        saveTv.setOnClickListener(saveListner);
        cancelTv.setOnClickListener(cancelListner);
        clearBt.setOnClickListener(clearListner);
        deleteBt.setOnClickListener(deleteListner);

        Intent intent = getIntent();
        initDb();
        loadDb();
        Log.d("kkk", "pos: "+ intent.getStringExtra("pos"));
        idx = Integer.parseInt(intent.getStringExtra("idx"));
        pos1 = Integer.parseInt(intent.getStringExtra("pos"));

        if(pos1 != -1) {
            titleEt.setText(intent.getStringExtra("title"));
            contentEt.setText(intent.getStringExtra("context"));
            saveTv.setEnabled(false);
            clearBt.setEnabled(true);
            deleteBt.setEnabled(true);
        }
        else {
            saveTv.setEnabled(true);
            clearBt.setEnabled(false);
            deleteBt.setEnabled(false);
        }


    }

    View.OnClickListener saveListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("act", "ADD");
            intent.putExtra("title", titleEt.getText().toString());
            intent.putExtra("context", contentEt.getText().toString());
            setResult(100, intent);
            finish();
            Toast.makeText(getApplicationContext(), "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();

        }

    };

    View.OnClickListener cancelListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "메모 기록이 취소 되었습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    View.OnClickListener clearListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mode = UPDATE_MEMO;
            AlertDialog.Builder alert = new AlertDialog.Builder(AddMemoActivity.this);
            alert.setTitle("수정");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra("act", "UPDATE");
                    intent.putExtra("idx", String.valueOf(pos1));
                    intent.putExtra("title", titleEt.getText().toString());
                    intent.putExtra("context", contentEt.getText().toString());
                    setResult(100, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), " 수정되었습니다.", Toast.LENGTH_SHORT).show();

                }
            });

            alert.setNegativeButton("취소", null);

            alert.setMessage("수정하시겠습니까?");
            alert.show();
        }
    };

    View.OnClickListener deleteListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mode = DELETE_MEMO;
            AlertDialog.Builder alert = new AlertDialog.Builder(AddMemoActivity.this);
            alert.setTitle("삭제");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra("act", "DELETE");
                    intent.putExtra("idx", String.valueOf(pos1));
                    setResult(100, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), " 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                }

            });

            alert.setNegativeButton("취소", null);

            alert.setMessage("삭제하시겠습니까?");
            alert.show();
        }

    };

    public void initDb(){
        SQLiteDatabase db = null;
        if (db == null) {
            db = openOrCreateDatabase("indivisualAccount.db",  Context.MODE_PRIVATE, null);
        }
        db.execSQL("CREATE TABLE IF NOT EXISTS member("
                + "idx INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "id TEXT,"
                + "pw TEXT,"
                + "user TEXT" + ");");

        db.close();
    }
    public void loadDb() {
        SQLiteDatabase db = null;
        if (db == null) {
            db = openOrCreateDatabase("indivisualAccount.db",  Context.MODE_PRIVATE, null);
        }

//        Cursor c = db.rawQuery("SELECT * FROM member WHERE idx=4", null);
        Cursor c = db.rawQuery("SELECT * FROM member", null);
        //Log.d("heu", "count: " + c.getCount()); // 전체 줄수를 가져온다
        c.moveToFirst();
        String str = "";
        while (c.isAfterLast() == false) {
            String name = c.getString(1);
            String id = c.getString(2);
            String pw = c.getString(3);
            String user = c.getString(4);

            userAccount = new Account(name, id, pw, user);
            c.moveToNext();
        }

        c.close();
        db.close();

    }

    @Override
    public void onErrorResponse(VolleyError error) { }
    @Override
    public void onResponse(String response) {
        if(mode == ADD_MEMO) {

        }
        else if(mode == UPDATE_MEMO) {

        }
        else if(mode == DELETE_MEMO) {

        }
    }


}
