package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();

    private Button Register,login;
    private Toolbar tbL;
    private TextInputLayout loginEmail,loginPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        tbL = (Toolbar)findViewById(R.id.appbarLogin);
        setSupportActionBar(tbL);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginEmail = (TextInputLayout)findViewById(R.id.email);
        loginPassword = (TextInputLayout) findViewById(R.id.password);

        Register = (Button) findViewById(R.id.new_acc);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getEditText().getText().toString();
                String pass = loginPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)||!(TextUtils.isEmpty(pass)))
                {
                    login_user(email,pass);
                    System.out.println("TAG = " + email + pass);
                }
            }
        });
    }

    private void login_user(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent mainIntent = new Intent(Login.this,chat.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this,"Kuch to gadbad h! Dya",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
