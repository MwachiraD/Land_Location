package code0.land_location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import static code0.land_location.LoginActivity.user;

public class ActivityChat extends AppCompatActivity
{
   // private FirebaseListAdapter<ChatMessage> adapter;
    ChatView mChatView;
    String msgfrom, msgto="joseph", chattype="";
    String who;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        if(database==null)
        {
            database.setPersistenceEnabled(true);
            myRef= database.getReference();
        }
        else
        {
            myRef= database.getReference();
        }

        Intent intent= getIntent();
        msgfrom=user;
        msgto=intent.getStringExtra("receiver");
        who = getIntent().getStringExtra("who");
        final String land_id=intent.getStringExtra("land_id");
        Log.d("land_id", land_id);

        chattype= intent.getStringExtra("chat_type");
        getSupportActionBar().setTitle("Land system chat");

        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityChat.this, String.valueOf(databaseError), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {


                ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);

                String isme="false";

                    String sender_then=chat.getCurrent_user();
                    String chatType=chat.getChatType();
                    String Message_text=chat.getMessage_text();


                    String MessageReceiver=chat.getMessageReceiver();


                    String land_id_feteched = chat.getLand_id();
                    long Time=chat.getTime();
                    String final_data=chat.getCurrent_user()+" "+chat.getMessage_sender() +" "+chat.getMessageReceiver() +" "+ chat.getLand_id();
                    Log.d("chat_data", String.valueOf(final_data));


                if(chatType.equals(chattype))
                {


                    Log.d("msg sender_then", sender_then);
                    Log.d("msg MessageReceiver", MessageReceiver);
                    Log.d("msg user", user);
                    if( (sender_then.equals(user) || MessageReceiver.equals(user)) && land_id_feteched.equals(land_id))
                    {


//                        if(msgto.equals(MessageReceiver))
//                        {

                            if(sender_then.equals(user))
                            {
                                //eg user, user or land_seller,land_seller, or surveyor,surveyou
                                isme="true";
                            }



                            if(isme.equals("true"))
                            {
                                Bitmap myIcon = null;
                                User me = new User(1, sender_then, myIcon);
                                Message message = new Message.Builder()
                                        .setUser(me)
                                        .setRightMessage(true)
                                        .setMessageText(Message_text)
                                        .hideIcon(true)
                                        .build();
                                mChatView.send(message);
                            }
                            else if (isme.equals("false"))
                            {

                                Bitmap yourIcon = null;
                                User you = new User(2,MessageReceiver , yourIcon);
                                Message receivedMessage = new Message.Builder()
                                        .setUser(you)
                                        .setRightMessage(false)
                                        .setMessageText(Message_text)
                                        .hideIcon(true)
                                        .build();
                                mChatView.receive(receivedMessage);
                            }


//                        }
//                        else
//                            Log.d("msg", "Error occured here!");
//
//


                    }
                    else
                        Log.d("msg", "sender then does not equal to current user");

                }
                else {
                    Log.d("msg", "chats do not match!!");
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               // Toast.makeText(ActivityChat.this, "child chnaged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
              //  Toast.makeText(ActivityChat.this, "child removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
              //  Toast.makeText(ActivityChat.this, "Child moved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ActivityChat.this, "Database error!", Toast.LENGTH_SHORT).show();

            }
        });


        mChatView = (ChatView)findViewById(R.id.chat_view);
        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms= mChatView.getInputText();
                mChatView.setInputText("");
                Date d= new Date();
                ChatMessage send_msg=new ChatMessage(user, msgto, chattype,d.getTime(), sms, msgfrom,land_id);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(send_msg);
                if(who.equals("client"))
                {
                    insert ( user,  land_id);

                }

            }



        });



    }

public void insert (final String username, final String land_id)
{
    class GetJSON extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHandler rh = new RequestHandler();
            HashMap<String, String> paramms = new HashMap<>();
            paramms.put("username", username);
            paramms.put("land_id", land_id);
            String s = rh.sendPostRequest(URLs.main+"save.php", paramms);
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ActivityChat.this, "Message sent!", Toast.LENGTH_SHORT).show();

        }


    }
    GetJSON jj = new GetJSON();
    jj.execute();


}
}

