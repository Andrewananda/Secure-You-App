package com.example.andrew.secureyou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e4_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = findViewById(R.id.editText4);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }



    @SuppressWarnings("deprecation")
    public void goToPasswordActivity(View v)
    {
        if(e4_email.getText().toString().trim().equals("")){
          Toast.makeText(getApplicationContext(),"Please fill the email",Toast.LENGTH_SHORT).show();
        }else{

            //checking if the user already exists in the database
            dialog.setMessage("Checking your email address");
            dialog.show();
            //cheking if the email is registered or not
            auth.fetchProvidersForEmail(e4_email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if (task.isSuccessful())
                            {
                                dialog.dismiss();
                                boolean check = !task.getResult().getProviders().isEmpty();
                                if (!check)
                                {
                                    //then the email will not be complete
                                    Intent myIntent = new Intent(RegisterActivity.this,PasswordActivity.class);
                                    myIntent.putExtra("email",e4_email.getText().toString());
                                    startActivity(myIntent);

                                    finish();


                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Sorry this email is already registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


        }



    }
}
