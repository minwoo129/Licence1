package com.example.license;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity3 extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    ListView lstTv;

    MyAdapter myAdapter;
    ArrayList<Contents> arr = new ArrayList<>();

    class RowDataHolder {
        TextView mvTvHolder;
        TextView mvTv2Holder;
        TextView mvTv3Holder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lstTv = findViewById(R.id.lstTv);

        myAdapter = new MyAdapter(this);
        lstTv.setAdapter(myAdapter);

        requestPost();


        lstTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity3.this);
                alert.setTitle("**상세정보**");
                alert.setMessage(arr.get(position).content);
                alert.setPositiveButton("끝내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter.notifyDataSetChanged();
                    }
                });
                alert.show();
            }
        });

    }

    private void requestPost(){
        /** POST **/
        RequestQueue stringRequest = Volley.newRequestQueue(this);
        String url = "http://ikey0129.woobi.co.kr/json_info.php";

        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //여기받아오기
                        JSONObject jsonObject = null;
                        arr.clear();
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("OK")) {
                                JSONArray jsonArr = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    JSONObject temp = jsonArr.getJSONObject(i);
                                    String ltitle = temp.getString("ltitle");
                                    String mtitle = temp.getString("mtitle");
                                    String stitle = temp.getString("stitle");
                                    String content = temp.getString("content");
                                    int num = temp.getInt("num");
                                    if(num ==3){
                                        arr.add(new Contents(ltitle,mtitle,stitle,content,num));
                                    }else{

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyDataSetChanged();
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


    private class MyAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.main_list, arr);
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
            ListActivity3.RowDataHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(R.layout.main_list, parent, false);
                viewHolder = new RowDataHolder();
                viewHolder.mvTvHolder = convertView.findViewById(R.id.mvTv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RowDataHolder) convertView.getTag();
            }

            viewHolder.mvTvHolder.setText(" " + arr.get(position).ltitle + " > " + arr.get(position).mtitle + " > " + arr.get(position).stitle);
            viewHolder.mvTvHolder.setTextSize(16);

            return convertView;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }


}

