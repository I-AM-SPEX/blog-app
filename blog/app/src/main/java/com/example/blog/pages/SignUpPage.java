package com.example.blog.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Pair;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.R;
import com.example.blog.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signUpHaveAcc,signUpBtn;
    TextView signUpLogo, signUpMotto;
    TextInputLayout signUpEmail,signUpPassword,signUpFirstName,signUpLastName,signUpUserName;
    ImageView signUpImage;

    FirebaseDatabase rootNode;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        signUpHaveAcc = findViewById(R.id.alreadHaveAcc);
        signUpEmail = findViewById(R.id.signUp_email);
        signUpPassword = findViewById(R.id.signUp_password);
        signUpLogo = findViewById(R.id.signUp_logo);
        signUpMotto = findViewById(R.id.signUp_motto);
        signUpImage = findViewById(R.id.signUp_image);
        signUpBtn = findViewById(R.id.singUp_button);
        signUpFirstName  = findViewById(R.id.signUp_firstName);
        signUpLastName  = findViewById(R.id.signUp_lastName);
        signUpUserName = findViewById(R.id.signUp_userName);


        signUpHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(signUpImage,"splashScreenImaageAnim");
                pairs[1] = new Pair<View,String>(signUpEmail,"loginEmail_trans");
                pairs[2] = new Pair<View,String>(signUpPassword,"loginPassword_trans");
                pairs[3] = new Pair<View,String>(signUpBtn,"login_buttonTrans");
                pairs[4] = new Pair<View,String>(signUpMotto,"login_descText");
                pairs[5] = new Pair<View,String>(signUpHaveAcc,"loginSignUp_trans");
                pairs[6] = new Pair<View,String>(signUpLogo,"logoText_Animation");
                ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(SignUpPage.this,pairs);
                startActivity(intent,opt.toBundle());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser(v);
            }
        });
    }

    private boolean isFirstNameValid() {
        String str =  signUpFirstName.getEditText().getText().toString();
        if (str.isEmpty()) {
            signUpFirstName.setError("Field cant be blank");
            return false;
        }else {
            signUpFirstName.setError(null);
            return true;
        }
    }
    private boolean isLastNameValid() {
        String str =  signUpLastName.getEditText().getText().toString();
        if (str.isEmpty()) {
            signUpLastName.setError("Field cant be blank");
            return false;
        }else {
            signUpLastName.setError(null);
            return true;
        }
    }
    private boolean isUserNameValid() {
        String str = signUpUserName.getEditText().getText().toString();


        if(str.isEmpty()) {
            signUpUserName.setError("Field cant be blank");
            return false;
        }else if (str.length() > 10) {
            signUpUserName.setError("User name too long");
            return false;
        }else {
            signUpUserName.setError(null);
            signUpUserName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isEmailValid() {
        String str = signUpEmail.getEditText().getText().toString();

        if(str.isEmpty()) {
            signUpEmail.setError("Field cant be blank");
            return false;
        }else {
            signUpEmail.setError(null);
            signUpEmail.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isPasswordValid() {
        String str = signUpPassword.getEditText().getText().toString();

        if(str.isEmpty()) {
            signUpPassword.setError("Field cant be blank");
            return false;
        }else  {
            signUpPassword.setError(null);
            signUpPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void signUpUser(View view) {
        if(!isFirstNameValid()||!isLastNameValid()||!isUserNameValid()||!isEmailValid()||!isPasswordValid()) {
            return;
        }

        String firstName = signUpFirstName.getEditText().getText().toString();
        String lastName = signUpLastName.getEditText().getText().toString();
        String userName = signUpUserName.getEditText().getText().toString();
        String email = signUpEmail.getEditText().getText().toString();
        String password = signUpPassword.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String refId = mAuth.getCurrentUser().getUid().toString();
                        User user = new User(firstName,lastName,userName,email,password, refId);

                        ref.child(refId).setValue(user);
                        Toast.makeText(SignUpPage.this, "Registration Succesfull", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, LoginPage.class );
                        startActivity(i);
                    }else{
                        Toast.makeText(SignUpPage.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
}