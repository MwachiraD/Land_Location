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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowUsers extends AppCompatActivity {
    ListView listView;
    String land_id;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        listView= (ListView) findViewById(R.id.listview);
        spinner=(Spinner)findViewById(R.id.spinner3);
        land_id=getIntent().getStringExtra("land_id");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShowUsers.this, land_id, Toast.LENGTH_SHORT).show();
                switch (position)
                {
                    case 0:
                        getJSON(land_id);
                        break;
                    case 1:
                        getJSON2(land_id);
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getJSON(final String land_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ShowUsers.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", land_id );
                employees.put("type","booked");
                String res=rh.sendPostRequest(URLs.main+"fetchbooked.php",employees);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }

    public void getJSON2(final String land_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ShowUsers.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Fetching data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", land_id );
                employees.put("type","chat");
                String res=rh.sendPostRequest(URLs.main+"fetchbooked.php",employees);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem1(s);

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }



    private void showthem1(String s)
    {

       // new AlertDialog.Builder(ShowUsers.this).setMessage(s).show();

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++)
            {

                    JSONObject jo = result.getJSONObject(i);
                    String location, price, status, time, cordinates;
                    String userid = jo.getString("userid");
                    String landid = jo.getString("landid");

                    String land_name = jo.getString("location");
                    String name = jo.getString("name");


                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("userid", userid);
                    employees.put("landid", landid);
                    employees.put("name", "User :"+name);
                    employees.put("land_name", land_name);
                    Log.d("result", String.valueOf(employees));
                    list.add(employees);

            }

        } catch (JSONException e)
        {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowUsers.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(ShowUsers.this, list, R.layout.chats,
                new String[]{"name", "land_name"}, new int[]{R.id.textView29, R.id.textView17,R.id.textView28, R.id.textView25});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {


                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);

                String userid = map.get("userid");
                String landid = map.get("landid");
                String name = map.get("name");
                String land_name = map.get("land_name");


                startActivity
                        (new Intent(ShowUsers.this, ActivityChat.class)
                        .putExtra("receiver", userid)
                        .putExtra("land_id", landid)
                        .putExtra("who", "admin")
                        .putExtra("chat_type", "landseller")
                        );

            }
        });
    }


    private void showthem(String s)
    {

       // new AlertDialog.Builder(ShowUsers.this).setMessage(s).show();

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);


                succes=jo.getString("success");
                if (succes.equals("true"))
                {
                    String location, price, status, time, cordinates;

                    String land_id = jo.getString("land_id");
                    String user = jo.getString("user");

                   String client_name = jo.getString("client_name");

                   String land_name = jo.getString("land_name");

                   String amt_paid = jo.getString("amt_paid");


                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("land_id", land_id);
                    employees.put("client_name", client_name);
                    employees.put("land_name", land_name);
                    employees.put("amt_paid","Paid "+ amt_paid);
                    Log.d("result", String.valueOf(employees));

                    list.add(employees);
                }
                else
                {

                }

            }





        } catch (JSONException e)
        {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowUsers.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(ShowUsers.this, list, R.layout.booked_lands,
                new String[]{"land_name", "client_name","amt_paid"}, new int[]{R.id.textView30,
                R.id.textView38,R.id.textView39});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(null);
    }

}
