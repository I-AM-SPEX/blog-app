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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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


    ImageModel imgModel;
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

        if (user != null) {
            currentUserId = user.getUid();
            currentUserRef = usersRef.child(currentUserId);

            currentUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userModel = snapshot.getValue(User.class);
                    if (userModel != null) {
                        fullName = userModel.getFullName();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ContentPostPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("myTag", "user is not logged");
        }

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

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                launcher.launch(galleryIntent);
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post(v);
            }
        });
    }

    private boolean isPostValid() {
        String str = post.getEditText().getText().toString();

        if (str.isEmpty()) {
            post.setError("Field cant be empty");
            return false;
        } else {
            post.setError(null);
            post.setEnabled(false);
            return true;
        }
    }

    private boolean isTitleValid() {
        String str = postTitle.getEditText().getText().toString();
        if (str.isEmpty()) {
            postTitle.setError("Field cant be empty");
            return false;
        } else {
            postTitle.setError(null);
            postTitle.setEnabled(false);
            return true;
        }
    }

    private void post(View v) {
        String content = post.getEditText().getText().toString();
        String title = postTitle.getEditText().getText().toString();
        String contentId = refPosts.push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        if (!isPostValid() || !isTitleValid()) {
            return;
        }

        if (imageUri != null) {
            uploadToFireBase(imageUri, contentId, title, content, date);
        } else {
            createPost(contentId, title, content, date, "https://images.unsplash.com/photo-1502082553048-f009c37129b9?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
            Intent i = new Intent(ContentPostPage.this, ContentPage.class);
            startActivity(i);
        }
    }

    private void uploadToFireBase(Uri uri, String contentId, String title, String content, String date) {
        StorageReference fileRef = storeRef.child(System.currentTimeMillis() + "." + getFileExe(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUri = uri.toString();
                        createPost(contentId, title, content, date, downloadUri);
                        contentUploadProgressBar.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(ContentPostPage.this, ContentPage.class);
                        startActivity(i);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                contentUploadProgressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                contentUploadProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ContentPostPage.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPost(String contentId, String title, String content, String date, String downloadUri) {
        ContentModel contentModel = new ContentModel(title, content, fullName, date, ContentModel.readTime(content), contentId, currentUserId, downloadUri);
        refPosts.child(contentId).setValue(contentModel);
    }

    private String getFileExe(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}