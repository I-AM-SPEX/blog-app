package com.example.blog.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.models.ContentModel;
import com.example.blog.utils.Content_Adapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Posts");


    ArrayList<ContentModel> contentModels;
    Content_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_page);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);

            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if (user != null) {
                Log.d("myTag", "user is logged in");
            } else {
                Log.d("myTag", "user is not logged");
            }

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            contentModels = new ArrayList<>();

            adapter = new Content_Adapter(this, contentModels);
            recyclerView.setAdapter(adapter);
            rootNode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ContentModel contentModel = dataSnapshot.getValue(ContentModel.class);
                        contentModels.add(contentModel);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {




        }


    }




    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        // Handle navigation view item clicks here
        int id = item.getItemId();

        if (id == R.id.AddArticle) {
            // Open ContentPostPage activity
            startActivity(new Intent(this, ContentPostPage.class));
            return true;
        }

        // Handle other navigation items here if needed

        return false;
    }
}