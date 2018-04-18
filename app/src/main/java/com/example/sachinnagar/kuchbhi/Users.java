package com.example.sachinnagar.kuchbhi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sachinnagar.kuchbhi.R.id.parent;

public class Users extends AppCompatActivity {

    private RecyclerView rv;
    private RecyclerView.Adapter adptr;
    private RecyclerView.LayoutManager lm;
    private DatabaseReference mUsersDatabase;

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private Toolbar usrBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        usrBar = (Toolbar) findViewById(R.id.userBar);
        setSupportActionBar(usrBar);
        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        rv = (RecyclerView)findViewById(R.id.rvUser);
      //  rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").equalTo("name");
       // System.out.print("ggdfdd---------------->" + query);
        FirebaseRecyclerOptions<Usr> options =
                new FirebaseRecyclerOptions.Builder<Usr>().setQuery(mUsersDatabase,Usr.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Usr, UsrViewHolder>(options)
        {
            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onBindViewHolder(@NonNull UsrViewHolder usrViewHolder, int i, @NonNull final Usr usr) {
                   usrViewHolder.setName(usr.getName());
                   usrViewHolder.setStatus(usr.getStatus());
                   usrViewHolder.setDp(usr.getImage(),getApplicationContext());

                   final String usrId =  getRef(i).getKey();

                   usrViewHolder.v.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent profile = new Intent(Users.this,Profile.class);
                           profile.putExtra("usrId",usrId);
                           startActivity(profile);
                       }
                   });
            }

            @NonNull
            @Override
            public UsrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.usr_layout, viewGroup, false);

                return new UsrViewHolder(view);


            }
        };
         rv.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class UsrViewHolder extends RecyclerView.ViewHolder
    {

        View v;
        public UsrViewHolder(View itemView) {
            super(itemView);
            v = itemView;
        }

        public void setName(String name)
        {
            TextView naam = (TextView) v.findViewById(R.id.nam);
            naam.setText(name);
        }

        public void setStatus(String status)
        {
            TextView stats = (TextView) v.findViewById(R.id.stat);
            stats.setText(status);
        }

        public void setDp(String image, Context ctx)
        {
            CircleImageView civ = (CircleImageView)v.findViewById(R.id.circleImageView);
            Picasso.get().load(image).placeholder(R.drawable.avatar).into(civ);
        }
    }
}

