package code0.land_location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        spinner7 = (Spinner) findViewById(R.id.spinner7);
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selected, long id) {
                if (selected == 0)
                {
                    getJSON("lands");

                } else
                {
                    getJSON("surveyor");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        user_id = intent.getStringExtra("username");
        listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);

                final String who = map.get("who");
                //Toast.makeText(ClientActivity.this, who, Toast.LENGTH_SHORT).show();
                if(who.equals("land"))
                {

                    coerdinates = map.get("cordinates");
                    final String land_id = map.get("land_id");
                    Log.d("result", land_id);
                    final String location = map.get("location");
                    final String price = map.get("price");
                    final String user = map.get("user");
                    final String status = map.get("statis");
                    final String time = map.get("time");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
                    builder.setMessage("Select Action");
                    builder.setPositiveButton("Book land", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(ClientActivity.this, ClientBook.class)
                                    .putExtra("location", location)
                                    .putExtra("price", price)
                                    .putExtra("user", user)
                                    .putExtra("status", status)
                                    .putExtra("time", price)
                                    .putExtra("land_id", land_id)
                            );


                        }
                    });
                    builder.setNegativeButton("View nearby amenities", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.d("coerdinates", coerdinates);

                            startActivity(new Intent(ClientActivity.this, SearchLocbyType.class)
                                    .putExtra("location", location)
                                    .putExtra("price", price)
                                    .putExtra("user", user)
                                    .putExtra("coerdinates", coerdinates)
                                    .putExtra("status", status)
                                    .putExtra("time", price)
                                    .putExtra("land_id", land_id));

                        }
                    });
                    builder.show();

                }
                else
                {

                    final String phone_number = map.get("phone");
                    final AlertDialog.Builder  alert = new AlertDialog.Builder(ClientActivity.this);
                    alert
                            .setMessage("Choose your preferred way to contact the surveyor")
                            .setNegativeButton("Call", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String phone = "+254"+phone_number;
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                    startActivity(intent);
                                }
                            })
                            .setPositiveButton("Send short message", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    sendSMS(phone_number, "Land System \nUser ID"+user_id+" is requesting for your serevices!");

                                }
                            })
                            .show();
                }

                }





        });

    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    public void getJSON(final String owner_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ClientActivity.this);
            String url;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //Toast.makeText(ClientActivity.this, owner_id, Toast.LENGTH_SHORT).show();
                if(owner_id=="lands")
                {
                    //Toast.makeText(ClientActivity.this, owner_id, Toast.LENGTH_SHORT).show();
                    url=URLs.main + "fetchlands.php";
                }
                else
                {
                    url=URLs.main + "fetchsurveyors.php";

                }


                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", "client");
                String res = rh.sendPostRequest(url, employees);
                return res;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

           //     Toast.makeText(ClientActivity.this, s, Toast.LENGTH_SHORT).show();

                if(owner_id=="lands")
                {
                    url=URLs.main + "fetchlands.php";
                    showthem(s);
                }
                else
                {
                    url=URLs.main + "fetchsurveyors.php";
                    showthem2(s);
                }



            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }

    private void showthem2(String s)
    {
//{"result":[{"surveyor_id":"2580","category":"Government","name":"Robert","phone":"079854","email":"email","location":"","success":"1"}]}
        Log.d("result", s);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes = "0";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);


                succes = jo.getString("success");
                if (succes.equals("1"))
                {
                    //{"result":[{"surveyor_id":"2580","category":"Government","nbame":"Robert","phone":"079854","email":"email","location":"","success":"1"}]}

                    String location, surveyor_id, category, name, phone, email;

                   // String owner = jo.getString("user");
                    location = jo.getString("location");
                    surveyor_id = jo.getString("surveyor_id");
                    category = jo.getString("category");
                    name = jo.getString("nbame");
                    phone = jo.getString("phone");
                    String is_verified = jo.getString("is_verified");

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("who", "surveyor");
                    employees.put("location", location);
                    employees.put("surveyor_id", surveyor_id);
                    employees.put("category", category);
                    employees.put("name", name);
                    employees.put("phone", phone);
                    employees.put("is_verified",is_verified);
                    Log.d("result", String.valueOf(employees));


                    list.add(employees);
                } else
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

        ListAdapter adapter = new SimpleAdapter(ClientActivity.this, list, R.layout.client_coach_list,
                new String[]{"location", "name", "phone","is_verified"}, new int[]{R.id.textView35, R.id.textView37, R.id.textView36, R.id.textView42});
        listView.setAdapter(adapter);


    }


    private void showthem(String s)
    {

        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.d("result", s);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes = "0";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);


                succes = jo.getString("success");
                if (succes.equals("1")) {
                    String location, price, status, time, cordinates;
                    String land_id = jo.getString("id");
                    String owner = jo.getString("user");
                    location = jo.getString("location");
                    price = jo.getString("price");
                    cordinates = jo.getString("cordinates");
                    time = jo.getString("timestamps");
                   // Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
                    status = jo.getString("status");
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("who", "land");
                    employees.put("location", price);
                    employees.put("land_id", land_id);
                    employees.put("cordinates", cordinates);
                    employees.put("price", location);
                    employees.put("user", owner);
                    employees.put("status", status);
                    employees.put("time", time);
                    Log.d("result", String.valueOf(employees));
                    list.add(employees);
                } else {

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
                new String[]{"location", "price", "status", "time"}, new int[]{R.id.textView27, R.id.textView26, R.id.textView28, R.id.textView25});
        listView.setAdapter(adapter);
    }


}