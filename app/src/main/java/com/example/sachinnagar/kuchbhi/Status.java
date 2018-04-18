package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Status extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout status;
    private Button change;

    private DatabaseReference statusData;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        toolbar = (Toolbar) findViewById(R.id.status_appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status badl lo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Text recieved through intent with value----> value
        String offstatus = getIntent().getStringExtra("value");

         currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        statusData = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        status = (TextInputLayout) findViewById(R.id.stat);
        change = (Button) findViewById(R.id.change);

        //Intent recieved text shows
        status.getEditText().setText(offstatus);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String cngStatus = status.getEditText().getText().toString();
               statusData.child("status").setValue(cngStatus);
                Intent hogya = new Intent(Status.this,Settings.class);
                startActivity(hogya);
                finish();
            }
        });
    }
}
