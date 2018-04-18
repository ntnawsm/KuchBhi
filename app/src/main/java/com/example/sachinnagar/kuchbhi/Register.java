package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private TextInputLayout name,email,pass;
    private Button create;
    private FirebaseAuth mAuth;
    private Toolbar tbR;

    private DatabaseReference fbdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        name = (TextInputLayout)findViewById(R.id.name);
        email = (TextInputLayout) findViewById(R.id.email);
        pass = (TextInputLayout) findViewById(R.id.pass);
        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = name.getEditText().getText().toString();
                String displayEmail = email.getEditText().getText().toString();
                String password = pass.getEditText().getText().toString();
                if(!TextUtils.isEmpty(displayName)||!TextUtils.isEmpty(displayEmail)||!TextUtils.isEmpty(password))
                {
                    register_user(displayName, displayEmail , password);   }
                else
                {
                    Toast.makeText(Register.this,"lgta h koi problem h!",Toast.LENGTH_LONG).show();

                }
            }
        });


        tbR = (Toolbar)findViewById(R.id.appbarRegister);
        setSupportActionBar(tbR);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void register_user(final String displayName,  String displayEmail, String password)
    {
      mAuth.createUserWithEmailAndPassword(displayEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful())
              {


                  FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                  String uid = current_user.getUid();
                  System.out.print(uid);
                  fbdata = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                  String device_token = FirebaseInstanceId.getInstance().getToken();

                  HashMap<String, String> userMap = new HashMap<>();
                  userMap.put("name", displayName);
                  userMap.put("status", "Yeh chl rha h aaj kl!");
                  userMap.put("image", "default");
                  //userMap.put("thumb_image", "default");
               //   userMap.put("device_token", device_token);

                  fbdata.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {

                          if(task.isSuccessful()){


                              Intent mainIntent = new Intent(Register.this, Login.class);
                              mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(mainIntent);
                              finish();

                          }

                      }
                  });


              }else{
                  Toast.makeText(Register.this,"Failed",Toast.LENGTH_LONG).show();
              }
          }
      });
    }
}
