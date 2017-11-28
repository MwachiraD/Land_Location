package code0.land_location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import code0.nearme.SearchLocbyType;

public class ClientActivity extends AppCompatActivity {
ListView listView;
    String user_id;
    Spinner spinner7;

    public static String coerdinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        spinner7=(Spinner)findViewById(R.id.spinner7);

        int selected=spinner7.getSelectedItemPosition();

        if(selected==0)
        {

            getJSON("client");
        }
        else
        {
           // getJSON("1");
        }

        Intent intent = getIntent();
        user_id=intent.getStringExtra("username");
        listView=(ListView)findViewById(R.id.listview);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view,final  int position, long id)
            {
                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                coerdinates=map.get("cordinates");
                final String land_id=map.get("land_id");
                final  String location =   map.get("location");
                final  String price=map.get("price");
                final String user=map.get("user");
                final String status= map.get("statis");
                final String time=map.get("time");

               final AlertDialog.Builder  builder = new AlertDialog.Builder(ClientActivity.this);
                builder.setMessage("Select Action");
                builder.setPositiveButton("Book land", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        startActivity(new Intent(ClientActivity.this, ClientBook.class)
                                .putExtra("location",location)
                                .putExtra("price",price)
                                .putExtra("user",user)
                                .putExtra("status", status)
                                .putExtra("time", price)
                                .putExtra("land_id", land_id)
                        );


                    }});
                builder.setNegativeButton("View nearby amenities", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Log.d("coerdinates",coerdinates);

                        startActivity(new Intent(ClientActivity.this, SearchLocbyType.class)
                                .putExtra("location",location)
                                .putExtra("price",price)
                                .putExtra("user",user)
                                .putExtra("coerdinates",coerdinates)
                                .putExtra("status", status)
                                .putExtra("time", price)
                                .putExtra("land_id", land_id));

                 }
                });
                builder.show();

                    }
                });

    }

    public void getJSON(final String owner_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ClientActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", owner_id );
                String res=rh.sendPostRequest(URLs.main+"fetchlands.php",employees);
                //http://192.168.0.35/gym/fetchcoaches.php

                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);
                 //Toast.makeText(ClientActivity.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }


    private void showthem(String s) {

        Log.d("result",s);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);


                succes=jo.getString("success");
                if (succes.equals("1"))
                {
                    String location, price,status,time, cordinates;
                    String land_id=jo.getString("id");
                    String owner=jo.getString("user");
                    location=jo.getString("location");
                    price=jo.getString("price");
                    cordinates=jo.getString("cordinates");
                    time=jo.getString("timestamps");
                    status=jo.getString("status");
                    HashMap<String, String> employees = new HashMap<>();

                    employees.put("location", price);
                    employees.put("land_id", land_id);
                    employees.put("cordinates",cordinates);
                    employees.put("price", location);
                    employees.put("user", owner);
                    employees.put("status", status);
                    employees.put("time", time);
                    list.add(employees);
                }
                else
                {

                }
            }


        } catch (JSONException e) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClientActivity.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(ClientActivity.this, list, R.layout.admin_view_coaches,
                new String[]{"location", "price","status", "time"}, new int[]{R.id.textView27, R.id.textView26,R.id.textView28, R.id.textView25});
        listView.setAdapter(adapter);
    }












    //    public void getJSON(final  String type)
//    {
//        class GetJSON extends AsyncTask<Void, Void, String> {
//
//            ProgressDialog progressDialog = new ProgressDialog(ClientActivity.this);
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                progressDialog.setMessage("Fetching data...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                RequestHandler rh = new RequestHandler();
//                HashMap<String, String> employees = new HashMap<>();
//                employees.put("type",type);
//                String res = rh.sendPostRequest(URLs.main + "fetchcoachtime.php", employees);
//                return res;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                progressDialog.dismiss();
//                showthem(s);
//                //Toast.makeText(Utensils.this, s, Toast.LENGTH_SHORT).show();
//
//            }
//
//
//            /*
//           eti payment should be working during progress,
//           apo kwa payment itakuwa two types(gold and silver)
//            so if a person is willing to pay gold class then he is considered first..
//            if they are more than one for gold we use FCFS nd the other one is rescheduled
//             */
//
//        }
//        GetJSON jj = new GetJSON();
//        jj.execute();
//    }
//
//
//    private void showthem(String s) {
//
//       // Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//
//        JSONObject jsonObject = null;
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//
//        try {
//            jsonObject = new JSONObject(s);
//            JSONArray result = jsonObject.getJSONArray("result");
//
//            String succes = "0";
//            for (int i = 0; i < result.length(); i++) {
//                JSONObject jo = result.getJSONObject(i);
//String price;
//
//                succes = jo.getString("success");
//                if (succes.equals("1")) {
//                    String datetime,coach_name,coach_gender, coach_phone, id;
//id=jo.getString("id");
//                    coach_name = jo.getString("coach_name");
//                    price=jo.getString("price");
//                    coach_gender = jo.getString("coach_gender");
//                    coach_phone = jo.getString("coach_phone");
//                    datetime = jo.getString("datetime");
//
//                    HashMap<String, String> employees = new HashMap<>();
//                    employees.put("id", id);
//                    employees.put("price",price);
//                    employees.put("datetime", datetime);
//                    employees.put("coach_phone", coach_phone);
//                    employees.put("coach_gender", coach_gender);
//                    employees.put("coach_name", coach_name);
//                    list.add(employees);
//
//                } else {
//
//                }
//
//
//            }
//
//        } catch (JSONException e)
//        {
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClientActivity.this);
//            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
//                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .setCancelable(true).show();
//        }
//
//        ListAdapter adapter = new SimpleAdapter(ClientActivity.this, list, R.layout.client_coach_list,
//                new String[]{"datetime","coach_phone","coach_gender","coach_name","price"}, new int[]{
//                R.id.textView37,R.id.textView36,R.id.textView35,R.id.textView20, R.id.textView40});
//        listView.setAdapter(adapter);
//    }
}
