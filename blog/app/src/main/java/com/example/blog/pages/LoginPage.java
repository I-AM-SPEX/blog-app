package com.example.blog.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blog.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signUpBtn, loginBtn;
    TextView logo, motto;
    ImageView loginimage;
    TextInputLayout loginEmail,loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();

        signUpBtn = findViewById(R.id.login_signUp);
        loginEmail= findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginimage = findViewById(R.id.loginImage);
        logo = findViewById(R.id.loginWelcome);
        motto = findViewById(R.id.loginDescText);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(loginimage,"splashScreenImaageAnim");
                pairs[1] = new Pair<View,String>(loginEmail,"loginEmail_trans");
                pairs[2] = new Pair<View,String>(loginPassword,"loginPassword_trans");
                pairs[3] = new Pair<View,String>(loginBtn,"login_buttonTrans");
                pairs[4] = new Pair<View,String>(motto,"login_descText");
                pairs[5] = new Pair<View,String>(signUpBtn,"loginSignUp_trans");
                pairs[6] = new Pair<View,String>(logo,"logoText_Animation");
                ActivityOptions opt = ActivityOptions.makeSceneTransitionAnimation(LoginPage.this,pairs);
                startActivity(intent,opt.toBundle());
            }

        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }

    public void login(View v) {
        if(!isEamilValid()||!isPasswordValid()) {
            return;
        }

        String email = loginEmail.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null) {
                            Intent intent = new Intent(LoginPage.this, ContentPage.class);
                            startActivity(intent);
                        }

                    }else {
                        Toast.makeText(LoginPage.this, "Login failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean isEamilValid() {
        String str = loginEmail.getEditText().getText().toString();

        if(str.isEmpty()) {
            loginEmail.setError("Field cant be blank");
            return false;
        }else {
            loginEmail.setError(null);
            loginEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isPasswordValid() {
        String str = loginPassword.getEditText().getText().toString();

        if(str.isEmpty()) {
            loginPassword.setError("Field cant be blank");
            return false;
        }else {
            loginPassword.setError(null);
            loginPassword.setErrorEnabled(false);
            return true;
        }
    }

    
}