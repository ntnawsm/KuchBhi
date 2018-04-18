package com.example.sachinnagar.kuchbhi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private CircleImageView dp;
    private TextView set_name,set_status;
    private Button cngstatus,cngimg;

    private static final int GALLERY_PICK = 1;

    //Firebase storage
    private StorageReference imgStore;

    private DatabaseReference settingdata;
    private FirebaseUser currentUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dp = (CircleImageView)findViewById(R.id.dp);
        set_name = (TextView) findViewById(R.id.name);
        set_status = (TextView) findViewById(R.id.status);
        cngstatus = (Button) findViewById(R.id.changestatus);
        cngimg = (Button) findViewById(R.id.changeImage);

        imgStore = FirebaseStorage.getInstance().getReference();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUid = currentUser.getUid();
        settingdata = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        settingdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(Settings.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumbimage = dataSnapshot.child("thumb_image").getValue().toString();

                set_name.setText(name);
                set_status.setText(status);

                if(!image.equals("default")) {

                    Picasso.get().load(image).placeholder(R.drawable.avatar).into(dp);
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cngstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent status = new Intent(Settings.this,Status.class);
                // Intent Value transferred with Value----> value
                status.putExtra("value",set_status.getText().toString());
                startActivity(status);

            }
        });

        cngimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"chun lo"),GALLERY_PICK);

                // start picker to get image for cropping and then use the image in cropping activity
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(Settings.this);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK &&  resultCode == RESULT_OK) {

            Uri imguri = data.getData();

            CropImage.activity(imguri)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(500,500)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();



                String current_user_id = currentUser.getUid();



                StorageReference path = imgStore.child("dp").child(random()+".jpg");

                path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            String dwnload_Uri = task.getResult().getDownloadUrl().toString();
                            settingdata.child("image").setValue(dwnload_Uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Settings.this, "ho gya", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else{
                            Toast.makeText(Settings.this, "nhi hua", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(Settings.this, "nhi hua", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static String random()
    {
        Random generator = new Random();
        StringBuilder randomSB = new StringBuilder();
        int randomlen = generator.nextInt(10);
        char temp;
        for(int i = 0; i<randomlen;i++)
        {
            temp = (char) (generator.nextInt(96)+32);
            randomSB.append(temp);
        }
        return randomSB.toString();
    }
}
