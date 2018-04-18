package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class chat extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "chat";
    private Toolbar tb;
    private ViewPager chatViewPager;
    private SectionsPagerAdapter sectionpager;
    private TabLayout chattabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        tb = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Aageg kya hoga");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent intent = new Intent(chat.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };

        //ViewPager Tabs
        chatViewPager = (ViewPager)findViewById(R.id.tabpager);
        sectionpager  = new SectionsPagerAdapter(getSupportFragmentManager());
        chatViewPager.setAdapter(sectionpager);

        chattabs = (TabLayout) findViewById(R.id.mainTab);
        chattabs.setupWithViewPager(chatViewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sign_out)
            signOut();


        if(item.getItemId() == R.id.AccSet) {
            Intent setting = new Intent(chat.this, Settings.class);
            startActivity(setting);

        }

        if(item.getItemId() == R.id.allUsr){
            Intent usr = new Intent(chat.this,Users.class);
            startActivity(usr);
        }



        return true;
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(chat.this,Splash.class));
                        finish();
                    }
                });
    }

}
