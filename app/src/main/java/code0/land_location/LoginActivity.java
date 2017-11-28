package code0.land_location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{


    public static  String user;
    public static  String user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // MultiDex.install(this);
        getSupportActionBar().setTitle("Land system");
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Login as..");
        spinnerArray.add("surveyor");
        spinnerArray.add("client");
        spinnerArray.add("land_seller");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner2);
        sItems.setAdapter(adapter);
        final EditText username, password;
        username= (EditText)findViewById(R.id.editText) ;
        password= (EditText)findViewById(R.id.editText2) ;
        Button signinnow=(Button)findViewById(R.id.button);
        signinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected=sItems.getSelectedItemPosition();

                if(selected==0)
                {
                    Toast.makeText(LoginActivity.this, "Please select login title", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String username_= username.getText().toString();
                    String password_ =password.getText().toString();


                    login_now( username_, password_, selected);
                }
            }
        });

        TextView register =  (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, ClientRegistration.class));
            }
        });

    }


    public void login_now(final String username, final String password, final int selected)
    {





        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                progressDialog.setMessage("Verifying you..");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("username", username);
                employees.put("password", password);
                employees.put("selected", String.valueOf(selected));
                String res=rh.sendPostRequest(URLs.main+"login.php",employees);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
             //   Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
              if(s.equals("1"))
              {
                  user=username;
                  switch (selected){
                      case 1:

                          startActivity(new Intent(LoginActivity.this, CoachActivity.class)
                                  .putExtra("username", username));
                          user_type="surveyor";

                          break;
                      case 2:
                          startActivity(new Intent(LoginActivity.this, ClientActivity.class)
                                  .putExtra("username", username));
                          user_type="client";
                          break;
                      case 3:
                          startActivity(new Intent(LoginActivity.this, AdminActivity.class)
                          .putExtra("username", username));
                          user_type="land_seller";
                          break;
                  }
              }
              else
              {
                  Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
              }







            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();





















    }




}
