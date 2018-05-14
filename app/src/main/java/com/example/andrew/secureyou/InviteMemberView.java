package com.example.andrew.secureyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InviteMemberView extends AppCompatActivity {
    Button btnSend;
    TextView txtCode;
    EditText txtPhone;

    //shared preference used to manage user sessions
    SharedPreferences sharedpreferences;
    public static final String USERPREFERENCES = "UserDetails" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member_view);


        //check if user has already logged in
        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);

        btnSend = findViewById(R.id.btnSend);
        txtCode = findViewById(R.id.txtCode);
        txtPhone = findViewById(R.id.txtPhone);

        if(sharedpreferences.contains("code")){
            txtCode.setText(sharedpreferences.getString("code",null));
        }else{
            txtCode.setText("Code not Set");
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtPhone.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter phone number",Toast.LENGTH_SHORT).show();
                }else{

                   // sendSMS(sharedpreferences.getString("code",null),txtPhone.getText().toString().trim());

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+txtPhone.getText().toString().trim()));
                    i.putExtra("sms_body",sharedpreferences.getString("code",null));

                    startActivity(i);
                }

            }
        });

    }




}
