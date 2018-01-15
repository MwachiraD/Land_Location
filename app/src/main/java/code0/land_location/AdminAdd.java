package code0.land_location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

import static code0.land_location.LoginActivity.user;

public class AdminAdd extends AppCompatActivity {
EditText location, price, email_adress, telephone_number;
    Button reg;
Spinner spinner4, spinner8;


    public static String cordinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        getSupportActionBar().setTitle("Add land");

        Button button6;

        spinner4=(Spinner)findViewById(R.id.spinner4);
        spinner8=(Spinner)findViewById(R.id.spinner8);

        location=(EditText)findViewById(R.id.editText3);
        price=(EditText)findViewById(R.id.editText4);
        reg=(Button)findViewById(R.id.button2);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locaton=location.getText().toString();
                String pric=price.getText().toString();

                try
                {
                    if(cordinate.length()<=0)
                    {
                        Toast.makeText(AdminAdd.this, "Place not selected", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String altitude = spinner4.getSelectedItem().toString();
                        String soiltype = spinner8.getSelectedItem().toString();


                        register(user,locaton, pric, cordinate, altitude, soiltype);
                    }
                }
                catch (Exception ex)
                {
                  //  Toast.makeText(AdminAdd.this, "", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button6=(Button)findViewById(R.id.button6);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminAdd.this, PlaceViewer.class));
            }
        });


    }


   public void register(final String user,final String location, final String price, final String cordinate, final String altitude, final String soiltype)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog pDialog = new ProgressDialog(AdminAdd.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog.setMessage("Adding location...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> paramms = new HashMap<>();
                paramms.put("user", user);
                paramms.put("cordinate", cordinate);
                paramms.put("location", location);

                paramms.put("altitude", altitude);
                paramms.put("soiltype", soiltype);


                paramms.put("price", price);
                String s = rh.sendPostRequest(URLs.main+"regland.php", paramms);
                return s;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
               // Toast.makeText(AdminAdd.this, s, Toast.LENGTH_SHORT).show();
                if(s.equals("1"))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminAdd.this);
                    alertDialogBuilder.setTitle("Attention!").setMessage("Registration success.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(true).show();
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminAdd.this);
                    alertDialogBuilder.setTitle("Attention!")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setMessage("Registration Failed.").setCancelable(true).show();
                }

            }


        }
        GetJSON jj = new GetJSON();
        jj.execute();
    }
}
