package com.example.sachinnagar.kuchbhi;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus,mProfielFriendsCount;
    private Button mProfileSendRequestBtn;

    private DatabaseReference mUserDatabase;

    private DatabaseReference mFriendReqDatabase;

    private FirebaseUser current_user;

    private String current_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        final String usrId = getIntent().getStringExtra("usrId");

//        naam = (TextView) findViewById(R.id.name);
//        naam.setText(usrId);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(usrId);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        current_user = FirebaseAuth.getInstance().getCurrentUser();

         mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfielFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);
        mProfileSendRequestBtn = (Button) findViewById(R.id.profile_send_req_btn);

        current_state =  "not_friends";


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.get().load(image).placeholder(R.drawable.avatar).into(mProfileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mProfileSendRequestBtn.setEnabled(false);

   //  -------------------NOT FRIEND STATE --------------------
                if(current_state.equals("not_friends"))
                {

                      mFriendReqDatabase.child(current_user.getUid()).child(usrId).child("request_type").
                      setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                                  mFriendReqDatabase.child(usrId).child(current_user.getUid()).child("request_type")
                                  .setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void aVoid) {
                                          mProfileSendRequestBtn.setEnabled(true);
                                          current_state = "req_state";
                                          mProfileSendRequestBtn.setText("Cancel Friend Request");
                                        //  Toast.makeText(Profile.this,"aaanh... chli gyi",Toast.LENGTH_LONG).show();
                                      }
                                  });
                              }else
                              {
                                  Toast.makeText(Profile.this,"Aap se nhi ho payega",Toast.LENGTH_LONG).show();
                              }
                          }
                      });
                }

    //  -----------------CANCEL REQUEST STATE ----------------------------
                if(current_state.equals("req_sent"))
                {
                    mFriendReqDatabase.child(current_user.getUid()).child(usrId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(usrId).child(current_user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    mProfileSendRequestBtn.setEnabled(true);
                                    current_state = "not_friends";
                                    mProfileSendRequestBtn.setText("Send Friend Request");



                                }
                            });


                        }
                    });
                }
            }
        });
    }
}
