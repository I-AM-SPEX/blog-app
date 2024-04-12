package com.example.blog.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.models.ContentModel;
import com.example.blog.models.User;
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
import java.util.Collections;


public class ContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference rootNode;

    DatabaseReference refPosts, refImages, usersRef, currentUserRef;



    ArrayList<ContentModel> contentModels;
    Content_Adapter adapter;

    TextView  userName;
    TextView userEmail;

    String currentUserId, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_page);
         rootNode = FirebaseDatabase.getInstance().getReference("Posts");
         currentUserRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navMenu = navigationView.getMenu();
        MenuItem addArticleMenuItem = navMenu.findItem(R.id.AddArticle);





        if (user != null) {
            String userId = user.getUid();
            System.out.println("!!!!!!"+userId);
            DatabaseReference userDetail = currentUserRef.child(userId);

            userDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userModel = snapshot.getValue(User.class);
                    System.out.println("!!!!!!! user model "+userModel);
                    if(userModel != null) {
                        if((boolean) userModel.isValidUser()) {
                            addArticleMenuItem.setVisible(true);
                        }else {
                            addArticleMenuItem.setVisible(false);
                        }
                    }else {
                        addArticleMenuItem.setVisible(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            contentModels = new ArrayList<>();

            adapter = new Content_Adapter(this, contentModels);
            recyclerView.setAdapter(adapter);
            rootNode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    contentModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ContentModel contentModel = dataSnapshot.getValue(ContentModel.class);
                        contentModels.add(contentModel);

                    }
                    // Reverse the order of the list
                    Collections.reverse(contentModels);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            addArticleMenuItem.setVisible(false);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            contentModels = new ArrayList<>();

            adapter = new Content_Adapter(this, contentModels);
            recyclerView.setAdapter(adapter);
            rootNode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //here nigga
                    contentModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ContentModel contentModel = dataSnapshot.getValue(ContentModel.class);
                        contentModels.add(contentModel);

                    }
                    // Reverse the order of the list
                    Collections.reverse(contentModels);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

//    @Override
//    public void onItemClick(ContentModel contentModel) {
//        System.out.println("Pressed");
//        // Handle item click here
//
//        // For example, you can open a new activity with details of the clicked item
//        Intent intent = new Intent(this, DetailPage.class);
//        intent.putExtra("data", contentModel);
//        intent.putExtra("all", contentModels);
//        startActivity(intent);
//    }









    @Override
    public void onBackPressed() {
       if(false){
           super.onBackPressed();
       }
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        // Handle navigation view item clicks here
        int id = item.getItemId();

        if (id == R.id.logOut){
            mAuth.signOut();
            Intent i = new Intent(this, LoginPage.class);
            startActivity(i);
        }

        if (id == R.id.AddArticle) {
            // Open ContentPostPage activity
            startActivity(new Intent(this, ContentPostPage.class));
            return true;
        }

        // Handle other navigation items here if needed

        return false;
    }
}