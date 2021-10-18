package com.example.license;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    RelativeLayout calenderLayout, memoLayout, testLayout, accountLayout, loginLayout, addAccountLayout;
    LinearLayout btnLayout;
    Button calendarBt, memoBt, testBt, accountBt, addMemoBt, logoutBt, changePwBt, administorBt, loginBt, addAccountBt, sameCheckBt, addBt;
    ListView memoLv, listLv;
    TextView userNameTv, userAuthTv, top_bar;
    EditText inputIdEt, inputPwEt, nameEt, idEt, pwEt, pwEt1, adminPassEt;
    RadioGroup rg;

    MemoAdapter memoAdapter;
    TestAdapter testadapter;

    GridView calGv;
    TextView monthTv;
    MyAdapter adapter;
    ArrayList<TestData> calArr = new ArrayList<>();
    ArrayList<TestData> test = new ArrayList<>();
    Calendar cal= Calendar.getInstance();
    TextView lv;

    Calendar calen= Calendar.getInstance();

    Account userAccount = null;
    boolean login = false;
    static boolean isLogin = false;
    boolean sameCheck = false;
    boolean isWrong = false;

    int LOG_IN = 100;
    int GET_MEMO = 110;
    int NEW_LOG_IN = 120;
    int RELOGIN = 130;
    int SAME_CHECK = 140;
    int ADD_MEMO = 150;
    int UPDATE_MEMO = 160;
    int DELETE_MEMO = 170;
    int GET_TEST = 180;

    int LOGO_ACTIVITY = 1000;
    int ADD_MEMO_ACTIVITY = 1100;
    int INTENT_ACTIVITY = 1200;

    int mode = 0;
    int posss=-1;

    String adminAccessCode = encoder("ezen1406!!");

    ArrayList<Memo_sub> arr = new ArrayList<>();
    ArrayList<Memo> memoArr = new ArrayList<>();
    ArrayList<String> listArr = new ArrayList<>();

    ArrayList<Contents> arrData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calenderLayout = findViewById(R.id.calenderLayout); memoLayout = findViewById(R.id.memoLayout); testLayout = findViewById(R.id.testLayout);
        accountLayout = findViewById(R.id.accountLayout); loginLayout = findViewById(R.id.loginLayout); addAccountLayout = findViewById(R.id.addAccountLayout);
        btnLayout = findViewById(R.id.btnLayout); calendarBt = findViewById(R.id.calendarBt); memoBt = findViewById(R.id.memoBt);
        testBt = findViewById(R.id.testBt); accountBt = findViewById(R.id.accountBt); addMemoBt = findViewById(R.id.addMemoBt);
        logoutBt = findViewById(R.id.logoutBt); changePwBt = findViewById(R.id.changePwBt); administorBt = findViewById(R.id.administorBt);
        loginBt = findViewById(R.id.loginBt); addAccountBt = findViewById(R.id.addAccountBt); sameCheckBt = findViewById(R.id.sameCheckBt);
        addBt = findViewById(R.id.addBt); memoLv = findViewById(R.id.memoLv); userNameTv = findViewById(R.id.userNameTv);
        userAuthTv = findViewById(R.id.userAuthTv); inputIdEt = findViewById(R.id.inputIdEt); inputPwEt = findViewById(R.id.inputPwEt);
        nameEt = findViewById(R.id.nameEt); idEt = findViewById(R.id.idEt); pwEt = findViewById(R.id.pwEt); pwEt1 = findViewById(R.id.pwEt1);
        adminPassEt = findViewById(R.id.adminPassEt); rg = findViewById(R.id.rg); top_bar = findViewById(R.id.top_bar); listLv = findViewById(R.id.listLv);

        showLayout(1);
        if(!login) {
            Intent intent = new Intent(this, com.example.license.LogoActivity.class);
            startActivityForResult(intent, LOGO_ACTIVITY);
        }

        memoAdapter = new MemoAdapter(this);
        memoLv.setAdapter(memoAdapter);

        calendarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout(1);
            }
        });
        memoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout(2);
            }
        });
        testBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout(3);
            }
        });
        accountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout(4);
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = NEW_LOG_IN;
                getLoginPost(encoder(inputIdEt.getText().toString()));
            }
        });


        //----------------------------------------- calendar ------------------------------------------
        calGv = findViewById(R.id.calGv);
        monthTv = findViewById(R.id.monthTv);
        adapter = new MyAdapter(this);
        calGv.setAdapter(adapter);
        calGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getContent(calArr.get(position).year, calArr.get(position).month, calArr.get(position).date)) {
                    lv.setText(calArr.get(position).content + "\n"+ calArr.get(position).detail);
                    posss = calArr.get(position).code;

                }else
                    lv.setText("일정없음"+calArr.get(position).date);
                Log.d("kkk", "위치: " + String.valueOf(position));
            }
        });
        findViewById(R.id.pre_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calen.add(Calendar.MONTH, -2);
                setCal();
                adapter.notifyDataSetChanged();
                Log.d("kk", "버튼1="+calen.get(Calendar.MONTH)+"/"+calen.get(Calendar.DATE));
                lv.setText("");
            }
        });
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calen.add(Calendar.MONTH, 0);
                setCal();
                adapter.notifyDataSetChanged();
                Log.d("kk", "버튼2="+calen.get(Calendar.MONTH)+"/"+calen.get(Calendar.DATE));
                lv.setText("");
            }
        });

        lv = findViewById(R.id.lv);
        requestPost();
        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posss==1){
                    startActivity(new Intent(MainActivity.this,com.example.license.ListActivity.class));
                }
                else if(posss == 2){
                    startActivity(new Intent(MainActivity.this,com.example.license.ListActivity2.class));
                }
                else if(posss == 3){
                    startActivity(new Intent(MainActivity.this,com.example.license.ListActivity3.class));

                }
            }
        });

        //------------------------------------------ memo ---------------------------------------------
        memoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, com.example.license.AddMemoActivity.class);
                intent.putExtra("idx", String.valueOf(memoArr.get(position).idx));
                intent.putExtra("pos", String.valueOf(position));
                intent.putExtra("title", memoArr.get(position).title);
                intent.putExtra("context", memoArr.get(position).context);
                startActivityForResult(intent, ADD_MEMO_ACTIVITY);
            }
        });
        addMemoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.license.AddMemoActivity.class);
                intent.putExtra("pos", "-1");
                intent.putExtra("idx", "-1");
                startActivityForResult(intent, ADD_MEMO_ACTIVITY);
            }
        });
        //----------------------------------------------------test-------------------------------------------------



        testadapter = new TestAdapter(this);
        listLv.setAdapter(testadapter);

        listArr.add("정보처리기사");
        listArr.add("수질환경기능사");
        listArr.add("귀금속가공기능사");
        testadapter.notifyDataSetChanged();

        listLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestQueue stringRequest = Volley.newRequestQueue(MainActivity.this);
                String uri = "http://ikey0129.woobi.co.kr/json_quiz.php?field="+listArr.get(position);
                StringRequest myReq = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Storage.qArr.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArr = jsonObject.getJSONArray("list");

                            Log.d("ddd","claer : "+ Storage.qArr);
                            for (int i = 0; i < jArr.length() ; i++) {
                                JSONObject tempObj = jArr.getJSONObject(i);
                                String subject = tempObj.getString("field");
                                int num = Integer.parseInt(tempObj.getString("num"));
                                String question = tempObj.getString("question");
                                String img = tempObj.getString("img");
                                String ex1 = tempObj.getString("ex1");
                                String ex2 = tempObj.getString("ex2");
                                String ex3 = tempObj.getString("ex3");
                                String ex4 = tempObj.getString("ex4");
                                String answer = tempObj.getString("answer");

                                Storage.qArr.add(new Storage(subject,num,question,ex1,ex2,ex3,ex4,img,answer,isWrong));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
                stringRequest.add(myReq);

                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                alert.setTitle("기출문제바로가기");
                alert.setMessage("기출문제 시작~~~~~ 하겠습니다~~~~~!!");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                Log.d("dd","position"+listArr.get(position));

                        startActivity(new Intent(MainActivity.this, com.example.license.TestActivity.class));

                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();
            }
        });




        //----------------------------------------------------account----------------------------------------------

        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("로그아웃");
                ab.setMessage("로그아웃하시겠습니까?");
                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "로그아웃되었습니다.", Toast.LENGTH_LONG).show();
                        isLogin = false;
                        showLayout(5);
                    }
                });
                ab.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ab.setCancelable(false);
                ab.show();
            }
        });

        changePwBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.changepw_dialog, null);

                final EditText nowPw = (EditText)view.findViewById(R.id.nowPwEt);
                final EditText nextPw = (EditText)view.findViewById(R.id.nextPwEt);
                final EditText nextPw1 = (EditText)view.findViewById(R.id.nextPwEt1);

                ab.setView(view);

                ab.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(encoder(nowPw.getText().toString()).equals(userAccount.pw)) {
                            if(encoder(nextPw.getText().toString()).equals(nextPw1.getText().toString())) {
                                updateAccountPost(userAccount.id, encoder(nextPw.getText().toString()));
                                Toast.makeText(MainActivity.this, "비밀번호가 변경되었습니다. \n 다시 로그인해주세요", Toast.LENGTH_LONG).show();
                                showLayout(5);
                            }
                            else
                                Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(MainActivity.this, "기존 비밀번호가 다릅니다!", Toast.LENGTH_LONG).show();
                    }
                });
                ab.show();

            }
        });

        administorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAccount.user.equals("admin")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                    ab.setTitle("관리자모드 접근");
                    ab.setMessage("추가확인을 위해 본인의 계정 비밀번호와\n 관리자 접근 비밀번호를 입력해주세요");

                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.admin_dialog, null);

                    final EditText pw = (EditText)view.findViewById(R.id.et1);
                    final EditText adminpw = (EditText)view.findViewById(R.id.et2);

                    ab.setView(view);

                    ab.setPositiveButton("관리자모드 접근", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(encoder(pw.getText().toString()).equals(userAccount.pw) && encoder(adminpw.getText().toString()).equals(adminAccessCode)) {
                                Toast.makeText(MainActivity.this, "암호가 확인되었습니다. \n 관리자 모드에 접근합니다", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, com.example.license.AdminActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(MainActivity.this, "접근 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                    ab.show();
                }
                else
                    Toast.makeText(MainActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_LONG).show();


            }
        });
        //--------------------------------------------------login--------------------------------------------------

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = NEW_LOG_IN;
                getLoginPost(encoder(idEt.getText().toString()));
            }
        });

        addAccountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout(6);
            }
        });

        //------------------------------------------------addAccount-----------------------------------------------
        sameCheckBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = SAME_CHECK;
                getLoginPost(encoder(idEt.getText().toString()));
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb2) {
                    adminPassEt.setVisibility(View.VISIBLE);
                }
            }
        });

        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sameCheck) {
                    if(encoder(pwEt.getText().toString()).equals(encoder(pwEt1.getText().toString()))) {
                        if(encoder(adminPassEt.getText().toString()).equals(adminAccessCode)) {
                            Toast.makeText(MainActivity.this, "관리자 암호가 확인되었습니다. \n" +
                                    "관리자 계정으로 가입되었습니다.", Toast.LENGTH_LONG).show();
                            addAccountPost(nameEt.getText().toString(), encoder(idEt.getText().toString()), encoder(pwEt1.getText().toString()), "admin");
                        }
                        else {
                            Toast.makeText(MainActivity.this, "일반 회원으로 가입되었습니다", Toast.LENGTH_LONG).show();
                            addAccountPost(nameEt.getText().toString(), encoder(idEt.getText().toString()), encoder(pwEt1.getText().toString()), "user");
                        }
                        showLayout(5);
                    }
                    else
                        Toast.makeText(MainActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this, "아이디 중복체크를 해주세요", Toast.LENGTH_LONG).show();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGO_ACTIVITY && resultCode == 100) {
            initDb();
            loadDb();

            if(userAccount != null) {
                mode = LOG_IN;
                getLoginPost(userAccount.id);
            }
            else {
                Intent intent = new Intent(this, com.example.license.IntroActivity.class);
                startActivityForResult(intent, INTENT_ACTIVITY);
            }
        }
        else if(requestCode == ADD_MEMO_ACTIVITY && resultCode == 100) {
            mode = ADD_MEMO;
            if(data.getStringExtra("act").equals("ADD")) {
                addMemoPost(userAccount.id, data.getStringExtra("title"), data.getStringExtra("context"));
            }
            else if(data.getStringExtra("act").equals("UPDATE")) {
                mode = UPDATE_MEMO;
                updateMemoPost(Integer.parseInt(data.getStringExtra("idx")), data.getStringExtra("title"), data.getStringExtra("context"));
            }
            else if(data.getStringExtra("act").equals("DELETE")) {
                mode = DELETE_MEMO;
                deleteMemoPost(Integer.parseInt(data.getStringExtra("idx")));
            }
        }
        else if(requestCode == INTENT_ACTIVITY) {
            showLayout(5);
        }
    }

    // 1: 캘린더, 2: 메모 3: test 4: account, 5: 로그인 6: 회원가입
    void showLayout(int mode) {
        calenderLayout.setVisibility(View.GONE);
        memoLayout.setVisibility(View.GONE);
        testLayout.setVisibility(View.GONE);
        accountLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.GONE);
        addAccountLayout.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);

        switch (mode) {
            case 1: // 캘린더
                calenderLayout.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);

                top_bar.setText("시험일정");
               break;
            case 2: // 메모
                top_bar.setText("메모");
                memoLayout.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
                getMemoPost(userAccount.id);
                break;
            case 3: // test
                top_bar.setText("예상문제 테스트");
                testLayout.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);

                requestGet();
                break;
            case 4: // 계정정보
                top_bar.setText("계정정보");
                accountLayout.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);

                userNameTv.setText(userAccount.name);
                userAuthTv.setText(userAccount.user.equals("admin")? "관리자": "일반 사용자");
                break;
            case 5: // 로그인
                top_bar.setText("로그인");
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case 6: // 회원가입
                top_bar.setText("회원가입");
                addAccountLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) { }
    @Override
    public void onResponse(String response) {
        if(mode == LOG_IN) {
            try {
                String id = "", pw = "";
                int found = 0;
                JSONObject o = new JSONObject(response);
                JSONArray a = new JSONArray(o.getString("list"));
                for(int i = 0; i < a.length(); i++) {
                    JSONObject o1 = a.getJSONObject(i);
                    id = o1.getString("id");
                    pw = o1.getString("pw");

                    if(userAccount.id.equals(id) && userAccount.pw.equals(pw)) {
                        found++;
                        saveDb(o1.getString("name"), o1.getString("id"), o1.getString("pw"), o1.getString("user"));
                        break;
                    }
                }

                if(found != 0)
                    login = true;
                else
                    login = false;

                setLogin(login);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(mode == GET_MEMO) {
            try {
                JSONArray a = new JSONArray(new JSONObject(response).getString("list"));
                for(int i = 0; i < a.length(); i++) {
                    JSONObject o = a.getJSONObject(i);
                    memoArr.add(new Memo(o.getInt("idx"), o.getString("id"), o.getString("date"), o.getString("title"), o.getString("context")));
                    arr.add(new Memo_sub(o.getString("title")));
                    memoAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(mode == NEW_LOG_IN || mode == RELOGIN) {
            try {
                String id = "", pw = "";
                int found = 0;
                JSONObject o = new JSONObject(response);
                JSONArray a = new JSONArray(o.getString("list"));
                for(int i = 0; i < a.length(); i++) {
                    JSONObject o1 = a.getJSONObject(i);
                    id = o1.getString("id");
                    pw = o1.getString("pw");

                    if(encoder(inputIdEt.getText().toString()).equals(id) && encoder(inputPwEt.getText().toString()).equals(pw)) {
                        found++;
                        saveDb(o1.getString("name"), o1.getString("id"), o1.getString("pw"), o1.getString("user"));
                        break;
                    }
                }

                if(found != 0)
                    isLogin = true;
                else
                    isLogin = false;

                if(isLogin) {
                    loadDb();
                    showLayout(1);
                    Toast.makeText(this, "로그인되었습니다. \n"+
                            userAccount.name + "(" + (userAccount.user.equals("admin")? "관리자":"일반 사용자")
                            + ")님 반갑습니다", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(mode == SAME_CHECK) {
            try {
                String id = "";
                int found = 0;
                JSONObject o = new JSONObject(response);
                JSONArray a = new JSONArray(o.getString("list"));
                for(int i = 0; i < a.length(); i++) {
                    JSONObject o1 = a.getJSONObject(i);
                    id = o1.getString("id");

                    if(userAccount.id.equals(id)) {
                        found++;
                        break;
                    }
                }

                if(found != 0) {
                    Toast.makeText(this, "이 아이디는 사용할 수 있습니다", Toast.LENGTH_LONG).show();
                    sameCheck = true;
                }
                else {
                    Toast.makeText(this, "이미 사용중인 아이디입니다", Toast.LENGTH_LONG).show();
                    sameCheck = false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(mode == ADD_MEMO || mode == UPDATE_MEMO || mode == DELETE_MEMO) {
            arr.clear();
            memoArr.clear();
            mode = GET_MEMO;
            getMemoPost(userAccount.id);
        }
        else if(mode == GET_TEST) {

        }
    }
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
    public void saveDb(String name, String id, String pw, String user){
        SQLiteDatabase db = null;
        if (db == null) {
            db = openOrCreateDatabase("indivisualAccount.db",  Context.MODE_PRIVATE, null);
        }
        db.execSQL("INSERT INTO member (name,id,pw,user) VALUES ('"+name+"', '"+id+"', '"+pw+"', '"+user+"')");
        db.close();
    }
    public void getLoginPost(final String id) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/json.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("id", id);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);

    }
    public void addAccountPost(final String name, final String id, final String pw, final String user) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/insert_ez.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("name", name);
                params.put("id", id);
                params.put("pw", pw);
                params.put("usercode", user);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);
    }
    public void updateAccountPost(final String id, final String pw) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/update_ez.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("id", id);
                params.put("pw", pw);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);
    }
    public static String encoder(String data) {
        String retVal = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for(int i=0; i<byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }

            retVal = sb.toString();

        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return retVal;

    }
    public void setLogin(boolean login) {
        if(login) {
            Toast.makeText(this, "로그인되었습니다. \n"+
                    userAccount.name + "(" + (userAccount.user.equals("admin")? "관리자":"일반 사용자")
            + ")님 반갑습니다", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "로그인에 실패하였습니다. \n 로그인 화면으로 이동합니다.", Toast.LENGTH_LONG).show();

        isLogin = login;

        if(isLogin)
            showLayout(1);
        else
            showLayout(5);

    }
    public void getMemoPost(final String id) {
        mode = GET_MEMO;
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/get_memo.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("id", id);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);

    }
    public void addMemoPost(final String id, final String title, final String context) {
        SimpleDateFormat f = new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 9);
        final String date = f.format(cal.getTime());

        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/insert_memo.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("id", id);
                params.put("date", date);
                params.put("title", title);
                params.put("context", context);
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);
    }
    public void updateMemoPost(final int idx, final String title, final String context) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/update_memo.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("idx", String.valueOf(idx));
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);
    }
    public void deleteMemoPost(final int idx) {
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String uri = "http://ikey0129.woobi.co.kr/delete_memo.php?";
        StringRequest myReq = new StringRequest(Request.Method.POST, uri, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String >();
                params.put("idx", String.valueOf(idx));
                return params;
            };
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f)

        );
        stringRequest.add(myReq);
    }
    private void requestGet(){
        mode = GET_TEST;
        RequestQueue stringRequest = Volley.newRequestQueue(MainActivity.this);
        String uri = "http://ikey0129.woobi.co.kr/json_info.php";
        StringRequest myReq = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jArr = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jArr.length() ; i++) {
                        JSONObject tempObj = jArr.getJSONObject(i);
                        String title = tempObj.getString("stitle");

                        listArr.add(title);
                    }

                    testadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000, 0, 1f));
        stringRequest.add(myReq);
    }
    private boolean getContent(int year, int month, int date){
        boolean result = false;
        for(int i=0; i<calArr.size(); i++){
            if(calArr.get(i).year == year && calArr.get(i).month == month && calArr.get(i).date == date){
                if(!calArr.get(i).content.equals(""))
                    result = true;
            }
        }
        return result;
    }
    private void requestPost() {
        /** POST **/
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String url = "http://ikey0129.woobi.co.kr/json_day.php";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("kkk", response);
                        //여기받아오기
                        JSONObject jsonObject = null;
                        arrData.clear();
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("OK")) {
                                JSONArray jsonArr = new JSONArray(jsonObject.getString("list"));
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject temp = jsonArr.getJSONObject(i);
                                    String date = temp.getString("date");   // 회차
                                    String startDay = temp.getString("startDay");  // 원서접수시작일자
                                    String endDay = temp.getString("endDay");  // 원서접수종료일자
                                    String testStDay = temp.getString("testStDay");  // 시험시작일자
                                    String testEndDay = temp.getString("testEndDay");  // 시험종료일자
                                    String passStDay = temp.getString("passStDay"); // 합격자발표시작일
                                    String passEndDay = temp.getString("passEndDay");  // 합격자발표종료일
                                    int code = temp.getInt("num");
                                    arrData.add(new Contents(date,startDay,endDay,testStDay,testEndDay, passStDay, passEndDay,code));

                                    aa(code);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setCal();
                        adapter.notifyDataSetChanged();
                    }
                }, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(3000,0,1f)
        );
        stringRequest.add(myReq);
    }
    void aa(int code){
        Log.d("kkk", arrData.size()+"");
        String dd="";
        if(code == 1)
            dd="기술사시험 \n";
        else if(code==2)
            dd="기사시험 \n";
        else if(code==3)
            dd="기능사시험 \n";
        for (int i = 0; i < arrData.size() ; i++) {
            String dat = dd+"원서접수기간: "+arrData.get(i).startDay+" ~ "+arrData.get(i).endDay +"\n"
                    +"시험 기간: "+arrData.get(i).testStDay+" ~ "+arrData.get(i).testEndDay +"\n"
                    +"합격자 발표기간: "+arrData.get(i).passStDay+" ~ "+arrData.get(i).passEndDay ;
            aaaaa(arrData.get(i).testStDay,arrData.get(i).date,code, dat);
        }

    }
    private void aaaaa(String date, String title, int code, String detail){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));

        test.add(new TestData(year,month,day,title,code,detail));
    }
    private void setCal(){
        calen.set(Calendar.DATE, 1);
        int dow = calen.get(Calendar.DAY_OF_WEEK);
        int dis = dow -1;
        calen.add(Calendar.MONTH, -1);
        int max = calen.getActualMaximum(Calendar.DATE);
        calArr.clear();
        String str = "";
        int co=0;
        String ss = "";
        for (int i = 0; i < dis; i++) {
            for(int j=0; j<test.size(); j++){
                if(test.get(j).year == calen.get(Calendar.YEAR) && test.get(j).month == calen.get(Calendar.MONTH)+1 && test.get(j).date == max) {
                    str = test.get(j).content;
                    co=test.get(j).code;
                    ss=test.get(j).detail;
                    break;
                }else {
                    str = "";
                    co=0;
                    ss="";
                }
            }
            calArr.add(0, new TestData(calen.get(Calendar.YEAR),calen.get(Calendar.MONTH), max, str,co,ss));
            max--;
        }

        calen.add(Calendar.MONTH, 1);
        max = calen.getActualMaximum(Calendar.DATE);
        for (int i = 0; i < max; i++) {
            for(int j=0; j<test.size(); j++){
                if(test.get(j).year == calen.get(Calendar.YEAR) && test.get(j).month == calen.get(Calendar.MONTH)+1 && test.get(j).date == i+1 ){
                    str =test.get(j).content;
                    co=test.get(j).code;
                    ss=test.get(j).detail;
                    break;
                }else{
                    str= "";
                    co=0;
                    ss="";
                }
            }
            calArr.add(new TestData(calen.get(Calendar.YEAR),calen.get(Calendar.MONTH),i+1,str,co,ss));
        }

        calen.set(Calendar.DATE, max);
        dow = calen.get(Calendar.DAY_OF_WEEK);  /// 이번달 마지막의 요일
        dis = 7 - dow;
        calen.add(Calendar.MONTH, 1);
        for (int i = 0; i < dis; i++) {
            for(int j=0; j<test.size(); j++){

                Log.d("aaa", test.get(j).year+","+calen.get(Calendar.YEAR)+"  // "+test.get(j).month +","+(calen.get(Calendar.MONTH)+1) + "   //   "+(test.get(j).date )+ " :  "+(i+1));
                Log.d("aaa", i+"---"+j);
                if(test.get(j).year == calen.get(Calendar.YEAR) && test.get(j).month == calen.get(Calendar.MONTH)+1 && test.get(j).date == i+1) {

                    str = test.get(j).content;
                    co=test.get(j).code;
                    ss=test.get(j).detail;
                    Log.d("kkk", "연 체크 if: " + String.valueOf(test.get(j).date) +", "+ String.valueOf(i+1));
                    break;
                }else{
                    str= "";
                    co=0;
                    ss="";
                    Log.d("kkk", "월 체크 else : " + String.valueOf(test.get(j).date) +", "+ String.valueOf(i+1));
                }

            }
            calArr.add(new TestData(calen.get(Calendar.YEAR),calen.get(Calendar.MONTH),i+1,str,co,ss));
        }
        if(calen.get(Calendar.MONTH) == 0){
            monthTv.setText("12"+"월");
        }else {
            monthTv.setText(calen.get(Calendar.MONTH) + "월");
        }

        Log.d("kkk", "calArr index 30: " + calArr.get(29).content);
    }

    class MemoAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MemoAdapter(Activity context) {
            super(context, R.layout.item, arr);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            MemoDataViewHolder viewHolder;
            if(convertView == null) {
                convertView = lnf.inflate(R.layout.item, parent, false);
                viewHolder = new MemoDataViewHolder();
                viewHolder.titleHolder = convertView.findViewById(R.id.memoTv);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MemoDataViewHolder)convertView.getTag();
            }

            viewHolder.titleHolder.setText(arr.get(position).title);

            return convertView;
        }
    }
    public class MemoDataViewHolder {
        public TextView titleHolder;
    }
    class TestAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public TestAdapter(Activity context) {
            super(context, R.layout.test_item, listArr);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listArr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listArr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RowDataViewHolder viewHolder;
            if(convertView == null) {
                convertView = lnf.inflate(R.layout.test_item, parent, false);
                viewHolder = new RowDataViewHolder();
                viewHolder.nameHolder = convertView.findViewById(R.id.listTv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (RowDataViewHolder)convertView.getTag();
            }

            viewHolder.nameHolder.setText(listArr.get(position));
            return convertView;
        }
    }
    public class RowDataViewHolder {
        public TextView nameHolder;
    }
    class MyAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.cal_item, calArr);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return calArr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return calArr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CalRowDataViewHolder viewHolder;
            if(convertView == null) {
                convertView = lnf.inflate(R.layout.cal_item, parent, false);
                viewHolder = new CalRowDataViewHolder();
                viewHolder.dateHolder = convertView.findViewById(R.id.dateTv);
                viewHolder.contentHolder = convertView.findViewById(R.id.contentTv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (CalRowDataViewHolder)convertView.getTag();
            }

            viewHolder.dateHolder.setText(calArr.get(position).date+"");
            int year = calArr.get(position).year;
            int month = calArr.get(position).month;
            int date = calArr.get(position).date;
            boolean isContent = getContent(year, month, date);

            Log.d("roo","12345="+calArr.get(position).code);
            if(isContent){
                if(calArr.get(position).content.equals("1"))
                    viewHolder.contentHolder.setText("                        ");
                else
                    viewHolder.contentHolder.setText(calArr.get(position).content);
                //viewHolder.contentHolder.setBackgroundColor(Color.GREEN);
                if(calArr.get(position).code==1) {
                    viewHolder.contentHolder.setBackgroundColor(Color.RED);
                    viewHolder.contentHolder.setTextColor(Color.parseColor("#000000"));
                }
                else if(calArr.get(position).code==2) {
                    viewHolder.contentHolder.setBackgroundColor(Color.GREEN);
                    viewHolder.contentHolder.setTextColor(Color.parseColor("#ff00ff"));
                }else if(calArr.get(position).code==3) {
                    viewHolder.contentHolder.setBackgroundColor(Color.BLUE);
                    viewHolder.contentHolder.setTextColor(Color.parseColor("#ffffff"));
                }
            }else{
                viewHolder.contentHolder.setText("");
            }

            return convertView;
        }
    }
    public class CalRowDataViewHolder {
        public TextView dateHolder;
        public TextView contentHolder;
    }

}
