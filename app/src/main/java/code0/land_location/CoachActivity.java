package code0.land_location;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static code0.land_location.LoginActivity.user;

public class CoachActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText phone, email, location;
    ListView listView;
    Button save,button6;
    EditText editText12;
    String username;
    Spinner spinner;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);


        spinner =(Spinner)findViewById(R.id.spinner);
        save = (Button) findViewById(R.id.button3);
        phone = (EditText) findViewById(R.id.editText7);//phonenumber
        email = (EditText) findViewById(R.id.editText8);//email
        location=(EditText)findViewById(R.id.editText5);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(phone.getText().toString().isEmpty()||email.getText().toString().isEmpty()||spinner.getSelectedItemPosition()==0)
                {
                    Toast.makeText(CoachActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    send_to_the_db(user, phone.getText().toString(), email.getText().toString(), location.getText().toString(), spinner.getSelectedItem().toString());
                }
            }
        });









    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
       // date.setText(String.valueOf(i) + "-" + String.valueOf(i1) + "-" + String.valueOf(i2));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
       // time.setText(String.valueOf(i) + ":" + String.valueOf(i1));
    }


    public void getJSON(final String coach_id)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(CoachActivity.this);

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
                employees.put("coach_id", coach_id);
                employees.put("who", "asf");
                String res = rh.sendPostRequest(URLs.main + "fetchcoachtime.php", employees);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem(s);
                //Toast.makeText(CoachActivity.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }






    public void send_to_the_db(final String username, final String phone, final String email, final String location, final String type)
    {
        // send_to_the_db(user, phone.getText().toString(), email.getText().toString(), location.getText().toString());
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(CoachActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Saving data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("user", username);
                employees.put("phone", phone);
                employees.put("type", type);
                employees.put("email", email);
                employees.put("location", location);
                String res = rh.sendPostRequest(URLs.main + "savesurveyor.php", employees);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if(s.equals("1"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoachActivity.this);
                    alertDialogBuilder.setTitle("Success").setMessage(String.valueOf("Details saved!"))
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(true).show();
                }
                else
                    new AlertDialog.Builder(CoachActivity.this).setMessage("Failed to add your qualifications. Please try again later").show();

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }



    private void showthem(String s) {

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
                    String datetime;
                    datetime = jo.getString("datetime");
                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("datetime", datetime);
                    list.add(employees);
                } else {

                }


            }

        } catch (JSONException e) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoachActivity.this);
            alertDialogBuilder.setTitle("Attention!").setMessage(String.valueOf(e))
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true).show();
        }

        ListAdapter adapter = new SimpleAdapter(CoachActivity.this, list, R.layout.coach_panel_list,
                new String[]{"datetime"}, new int[]{R.id.textView20});
        listView.setAdapter(adapter);
    }


}
