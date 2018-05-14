package com.example.andrew.secureyou;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    //create sessions to store/retrieve selections
    SharedPreferences sharedpreferences;

    SharedPreferences.Editor editor;
    public static final String USERPREFERENCES = "UserDetails" ;

    String name,email,password,date,isSharing,code;
    Uri imageUri;
    ProgressDialog progressDialog;


    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        //declare shared preferences
        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");

        if (myIntent != null) {
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            isSharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");
        }

        t1.setText(code);
    }

    public void registerUser(View v)
    {
       progressDialog.setMessage("Please wait. We are creating an account for you.");
       progressDialog.show();

       auth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful())
                       {
                           //inserting values in the database

                           CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","na");

                           user = auth.getCurrentUser();
                           userId = user.getUid();

                            // getting user data into the database using the child
                           reference.child(userId).setValue(createUser)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    //saveing the image of the user in firebase
                                                StorageReference sr =storageReference.child(user.getUid() + ".jpg");
                                                                sr.putFile(imageUri)
                                                                     .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                //uploading the users image to the database
                                                                                String download_image_path = task.getResult().getDownloadUrl().toString();
                                                                                reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                if (task.isSuccessful())
                                                                                                {
                                                                                                    progressDialog.dismiss();
                                                                                                    //Toast.makeText(InviteCodeActivity.this, "Checking email for authentification", Toast.LENGTH_SHORT).show();

                                                                                                    sendVerificationEmail();
                                                                                                    Intent myIntent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                                                                                    startActivity(myIntent);
                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                    progressDialog.dismiss();
                                                                                                    Toast.makeText(InviteCodeActivity.this, "An error occured", Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                         }
                                                                     });


                                                }

                                                else
                                                {
                                                    progressDialog.dismiss();

                                                    editor.putString("code", code);
                                                    editor.commit();

                                                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(InviteCodeActivity.this,LoginActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                       }
                                   });




                       }

                   }
               });

    }
    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(InviteCodeActivity.this, "Email sent for verification", Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }
                        else
                        {
                            Toast.makeText(InviteCodeActivity.this, "Could not send the email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
