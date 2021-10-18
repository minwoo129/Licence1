package com.example.license;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //정보처리기사
    //270초

    RelativeLayout mainLayout;
    TextView qTv;
    TextView secTv;
    ImageView qIv;
    Button nextBt;
    Button finishBt;
    Button returnBt;
    RadioGroup numGrup;
    RadioButton ans1;
    RadioButton ans2;
    RadioButton ans3;
    RadioButton ans4;

    RelativeLayout subLayout;
    TextView numTv;
    GridView gv;
    MyAdapter adapter;

    int idx = 0;
    int score = 0;
    int sec = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mainLayout = findViewById(R.id.mainLayout);
        qTv = findViewById(R.id.qTv);
        qIv = findViewById(R.id.qIv);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        numGrup = findViewById(R.id.numGrup);
        nextBt = findViewById(R.id.nextBt);
        finishBt = findViewById(R.id.finishBt);
        secTv = findViewById(R.id.secTv);

        subLayout = findViewById(R.id.subLayout);
        numTv = findViewById(R.id.numTv);
        gv = findViewById(R.id.gv);
        adapter = new MyAdapter(this);

        gv.setOnItemClickListener(this);

        showQuestion();

//        for (int i = 0; i < qArr.size() ; i++) {
//            Log.d("dd", "arr : "+qArr.get(i).subject + "  num : "+ qArr.get(i).num);
//        }

        gv.setAdapter(adapter);

        handler.sendEmptyMessage(0);

        findViewById(R.id.nextBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkAns();
            }
        });

        findViewById(R.id.finishBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkAns();
                handler.removeMessages(0);
                mainLayout.setVisibility(View.INVISIBLE);
                subLayout.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainLayout.setVisibility(View.VISIBLE);
        subLayout.setVisibility(View.INVISIBLE);
        returnBt.setVisibility(View.VISIBLE);
        secTv.setVisibility(View.INVISIBLE);
        backQuestion(position);
    }

    private void backQuestion(int position){
        qTv.setText(Storage.qArr.get(position).ques);
        ans1.setText(Storage.qArr.get(position).ex1);
        ans2.setText(Storage.qArr.get(position).ex2);
        ans3.setText(Storage.qArr.get(position).ex3);
        ans4.setText(Storage.qArr.get(position).ex4);

        Glide
                .with(this)
                .load(Storage.qArr.get(position).img)
                .into(qIv);


    }

    class MyAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.row, Storage.qArr);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Storage.qArr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return Storage.qArr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RowDataViewHolder viewHolder;
            if(convertView == null) {
                convertView = lnf.inflate(R.layout.row, parent, false);
                viewHolder = new RowDataViewHolder();
                viewHolder.numHolder = convertView.findViewById(R.id.numTv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (RowDataViewHolder)convertView.getTag();
            }

            viewHolder.numHolder.setText(Storage.qArr.get(position).num+"");

            if(Storage.qArr.get(position).isWrong){
                viewHolder.numHolder.setBackground(getDrawable(R.drawable.rounded));
            }else{
                viewHolder.numHolder.setBackground(getDrawable(R.drawable.round));
            }


            return convertView;
        }
    }
    public class RowDataViewHolder {
        public TextView numHolder;

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            handler.sendEmptyMessageDelayed(0,1000);

            sec--;

            if(sec == 0){
                numTv.setText("점수 : " + score+"점");
                Toast.makeText(TestActivity.this, "시간초과 입니다!", Toast.LENGTH_SHORT).show();
                mainLayout.setVisibility(View.INVISIBLE);
                subLayout.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                handler.removeMessages(0);
            }

            secTv.setText("남은 시간 "+ sec + "초");
        }
    };


    private void showQuestion(){

        qTv.setText(Storage.qArr.get(idx).ques);
        ans1.setText(Storage.qArr.get(idx).ex1);
        ans2.setText(Storage.qArr.get(idx).ex2);
        ans3.setText(Storage.qArr.get(idx).ex3);
        ans4.setText(Storage.qArr.get(idx).ex4);

        Glide
                .with(this)
                .load(Storage.qArr.get(idx).img)
                .into(qIv);


    }

    private void chkAns(){
        RadioButton rb = findViewById(numGrup.getCheckedRadioButtonId());
//        Log.d("dd","radio : "+rb.getText().toString());
//        Log.d("dd","ans : "+ qArr.get(idx).ans);
        try {
            if (rb.getText().toString().equals(Storage.qArr.get(idx).ans)) {
                Storage.qArr.get(idx).isWrong = true;
                score += 10000;
                numTv.setText("점수 : " + score+"점");
                rb.setChecked(false);
            } else {
                rb.setChecked(false);
                Storage.qArr.get(idx).isWrong = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        idx++;

        if (idx == Storage.qArr.size()-1){
            nextBt.setVisibility(View.INVISIBLE);
            finishBt.setVisibility(View.VISIBLE);
            Toast.makeText(this, "마지막 문제 입니다!", Toast.LENGTH_SHORT).show();
        }

        if (idx >= Storage.qArr.size()) {
            idx --;
        }else{
            showQuestion();
        }
//        Log.d("dd","score : "+ score);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
