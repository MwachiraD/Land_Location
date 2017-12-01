package code0.land_location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowUsers extends AppCompatActivity {
    ListView listView;
    String land_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        listView= (ListView) findViewById(R.id.listview);
        land_id=getIntent().getStringExtra("land_id");
        getJSON(land_id);

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
                employees.put("land_id", land_id );
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

    private void showthem(String s)
    {
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

                    String location, price, status, time, cordinates;
                    String land_id = jo.getString("id");
                    String owner = jo.getString("user");
                    location = jo.getString("location");
                    price = jo.getString("price");
                    cordinates = jo.getString("cordinates");
                    time = jo.getString("timestamps");
                    status = jo.getString("status");
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("location", price);
                    employees.put("land_id", land_id);
                    employees.put("cordinates", cordinates);
                    employees.put("price", location);
                    employees.put("user", owner);
                    employees.put("status", status);
                    employees.put("time", time);
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

        ListAdapter adapter = new SimpleAdapter(ShowUsers.this, list, R.layout.admin_view_coaches,
                new String[]{"location", "price","status", "time"}, new int[]{R.id.textView27, R.id.textView26,R.id.textView28, R.id.textView25});
        listView.setAdapter(adapter);
    }

}
