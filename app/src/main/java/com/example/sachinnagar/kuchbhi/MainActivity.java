package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageButton fem;
    ImageView pic;
    Button letsroll;


    private final int REQUEST_LOGIN=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //toolbar.setTitle("Firebase Auth");
        setSupportActionBar(toolbar);
        pic = (ImageView)findViewById(R.id.imageView2);
        fem = (ImageButton)findViewById(R.id.imageButton2);
        fem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pic.setImageResource(R.drawable.a206864);
            }
        });

        letsroll = (Button) findViewById(R.id.letsroll);
        letsroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this,chat.class);
                startActivity(next);
                finish();
            }
        });


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                startActivity(new Intent(this, SignIn.class)
                        .putExtra("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()));

            };
            finish();

        }


        else
        {
            startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder().setAvailableProviders(
                        Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                    )).build(),REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (requestCode == RESULT_OK) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                    startActivity(new Intent(this, SignIn.class).putExtra("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
                    finish();
                    return;
                }
                else
                {
                    if(response == null) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                        Toast.makeText(this,"Unkown Error",Toast.LENGTH_LONG).show();
                        return;
                    }

                }

                Toast.makeText(this,"Unkown SignIn Error",Toast.LENGTH_LONG).show();
            }
        }
    }


}
