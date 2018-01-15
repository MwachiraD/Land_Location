package code0.land_location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ClientBook extends AppCompatActivity {

    Button  button4,button8;
    EditText amt, editText14;
    String location, user, time, status;
    String user_id;
    String price;
    static String land_id;
    String phone_number ="254726442087";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_book);
        Intent intent = getIntent();
        location=intent.getStringExtra("location");
       // location=intent.getStringExtra("location");
        price=intent.getStringExtra("price");
        user=intent.getStringExtra("user");
        price=intent.getStringExtra("location");
        land_id=intent.getStringExtra("land_id");
        Log.d("result", land_id);

        amt=(EditText) findViewById(R.id.editText9);
        amt.setText(price);
        editText14=(EditText) findViewById(R.id.editText14);
        button4=(Button)findViewById(R.id.button4);
        button8=(Button)findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
startActivity(new Intent(ClientBook.this, ClientMap.class));
            }
        });
        TextView t16= (TextView)findViewById(R.id.textView16);
        t16.setText(user);

        Button btn7 =(Button)findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientBook.this, ActivityChat.class)
                .putExtra("receiver", user)
                        .putExtra("land_id", land_id)
                        .putExtra("who", "client")
                        .putExtra("chat_type", "landseller")
                );
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                phone_number= editText14.getText().toString();
                book(phone_number, price,land_id,user);
            }
        });


    }


public void book(final String phone, final String ammt, final String land_id, final String user_id)
{
    class GetJSON extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(ClientBook.this);

        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();

          //  Toast.makeText(ClientBook.this, user_id, Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Applying...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            RequestHandler rh = new RequestHandler();
            HashMap<String, String> employees = new HashMap<>();
            employees.put("user_id", user_id);
            employees.put("land_id", land_id);
            employees.put("ammount", ammt);
            employees.put("phone",phone);
            String res=rh.sendPostRequest(URLs.main+"mpesa/home.php",employees);
            return res;

        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);


           progressDialog.dismiss();
            if (s.substring(0,1).equals("1"))
            {
                new AlertDialog.Builder(ClientBook.this)
                        .setMessage("Your request request has been received and is awaiting confirmation and feedback. " )
                        .setNegativeButton("Okay", null)
                        .show();
            }
            else
            {
                new AlertDialog.Builder(ClientBook.this)
                        .setMessage("Request failed! Please try again later.")
                        .setNegativeButton("Okay", null)
                        .show();
            }

        }

    }
    GetJSON jj =new GetJSON();
    jj.execute();
}
    public void getJSON(final String coach_phone_, final String ammount)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(ClientBook.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Applying...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> employees = new HashMap<>();
                employees.put("coach_phone", coach_phone_);
                employees.put("ammount", ammount);
                employees.put("user_id", user_id);
                employees.put("date_time", "");
                String res=rh.sendPostRequest(URLs.main+"fetchcoaches.php",employees);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
               // showthem(s);
                Toast.makeText(ClientBook.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }


}








/*


  button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final String ammount= amt.getText().toString();

                if(ammount.isEmpty()||editText14.getText().toString().trim().length()!=10)
                {
                    Toast.makeText(ClientBook.this, "Invalid amount or phone number!", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    if(spinner.getSelectedItem().toString().equals("Gold"))
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientBook.this);
                        builder.setMessage("Are you sure want to pay "+ ammount+" ?" +
                                "You selected GOLD package, you will be charged higher but given more priority!")
                                .setTitle("Please confirm.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        String category="GOLD";
                                       String phone="254"+String.valueOf(Integer.parseInt(editText14.getText().toString())+200);
                                        book(phone,coach_phone, ammount, timebooked, category);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {

                                    }
                                });
                        builder.show();
                    }
                    else
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ClientBook.this);
                            builder.setMessage("Are you sure want to pay "+ ammount+" ?")
                                    .setTitle("Please confirm")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {


                                            String category="SILVER";
                                            String phone="254"+String.valueOf(Integer.parseInt(editText14.getText().toString()));
                                            book(phone, coach_phone, ammount, timebooked, category);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {

                                        }
                                    });
                            builder.show();


                        }



                }





            }
        });
 */