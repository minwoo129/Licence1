package com.example.license;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class IntroActivity extends AppCompatActivity {
    ViewPager pager;
    MyAdapter adapter;
    Button starBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        starBt = findViewById(R.id.startBt);
        pager = findViewById(R.id.pager);
        adapter = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        starBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("login", false);
                setResult(100, intent);
                finish();


            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE){
                    Log.d("heu","IDLE");
                }else if(state == ViewPager.SCROLL_STATE_DRAGGING){
                    Log.d("heu","DRAG");
                }else if(state == ViewPager.SCROLL_STATE_SETTLING){
                    Log.d("heu","SETTING");
                }
            }
        });



    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d("heu","pos: "+position);
            if(position % 3 == 0){
                return new Frag1Activity();
            }else if(position % 3 == 1){
                return new Frag2Activity();
            }else if(position % 3== 2){
                return new Frag3Activity();
            }else{
                return null;
            }

        }

        @Override
        public int getCount() {
            return 10;
        }
    }

}
