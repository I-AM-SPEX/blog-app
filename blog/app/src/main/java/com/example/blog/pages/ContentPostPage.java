package com.example.blog.pages;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blog.R;
import com.example.blog.models.ContentModel;
import com.example.blog.models.ImageModel;
import com.example.blog.models.User;
import com.google.android.material.textfield.TextInputLayout;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContentPostPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase rootNode;
    StorageReference storeRef;
    DatabaseReference refPosts, refImages, usersRef, currentUserRef;

    TextInputLayout post, postTitle;
    ImageView uploadImage;
    ProgressBar contentUploadProgressBar;
    Button postBtn;
    Uri imageUri;
    ActivityResultLauncher<Intent> launcher;

    String currentUserId, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_post_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        rootNode = FirebaseDatabase.getInstance();
        refPosts = rootNode.getReference("Posts");
        refImages = rootNode.getReference("Images");
        storeRef = FirebaseStorage.getInstance().getReference();
        usersRef = rootNode.getReference("Users");

        postTitle = findViewById(R.id.contentPostTitle);
        post = findViewById(R.id.contentPost_id);
        uploadImage = findViewById(R.id.contentImageUpload);
        postBtn = findViewById(R.id.contentPostButton);
        contentUploadProgressBar = findViewById(R.id.contentProgressBar);

        contentUploadProgressBar.setVisibility(View.INVISIBLE);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        uploadImage.setImageURI(imageUri);
                    }
                }
        );

        uploadImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            launcher.launch(galleryIntent);
        });

        postBtn.setOnClickListener(v -> post());
    }

    private void post() {

        String content = post.getEditText().getText().toString();
        String title = postTitle.getEditText().getText().toString();

        if (content.isEmpty()) {
            post.setError("Field can't be empty");
            return;
        }

        if (title.isEmpty()) {
            postTitle.setError("Field can't be empty");
            return;
        }

        String contentId = refPosts.push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String downloadUri = "https://revenuearchitects.com/wp-content/uploads/2017/02/Blog_pic-300x170.png";

        if (user != null) {
            currentUserId = user.getUid();
            currentUserRef = usersRef.child(currentUserId);

            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userModel = snapshot.getValue(User.class);

                    if (userModel != null) {
                        fullName = userModel.getFirstName() + " "+ userModel.getLastName();

                    }
                    uploadImageToFirebase(content, title, contentId, date, downloadUri);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ContentPostPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(String content, String title, String contentId, String date, String downloadUri) {
        if (imageUri != null) {
            contentUploadProgressBar.setVisibility(View.VISIBLE);
            StorageReference fileRef = storeRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String updatedDownloadUri = uri.toString();
                                    postContent(content, title, contentId, date, updatedDownloadUri);
                                })
                                .addOnFailureListener(e -> {
                                    contentUploadProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(ContentPostPage.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        contentUploadProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ContentPostPage.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            postContent(content, title, contentId, date, downloadUri);
        }
    }


    private void postContent(String content, String title, String contentId, String date, String downloadUri) {
        ContentModel contentModel = new ContentModel(title, content, fullName, date, ContentModel.readTime(content), contentId, currentUserId, downloadUri);
        refPosts.child(contentId).setValue(contentModel)
                .addOnSuccessListener(aVoid -> {
                    contentUploadProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ContentPostPage.this, "Content posted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    contentUploadProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ContentPostPage.this, "Failed to post content", Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
