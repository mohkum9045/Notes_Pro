package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText Email, Password;
    MaterialButton Login;
    ProgressBar Bar;
    TextView CreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.edtEmail);
        Password = findViewById(R.id.edtPassword);
        Login = findViewById(R.id.btnLogin);
        Bar = findViewById(R.id.progressBar);
        CreateAccount = findViewById(R.id.txtCreateAccount);

        Login.setOnClickListener(v-> funLogin());
        CreateAccount.setOnClickListener(v-> startActivity(new Intent(Login.this,CreateAccount.class)));

    }

    void funLogin(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        boolean isValidated = ValidateData(email,password);
        if(!isValidated){
            return;
        }
        LoginAccountFirebase(email,password);
    }

    void LoginAccountFirebase(String email, String Password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeinProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgress(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    }else{
                        utility.showtoast(Login.this,"Email is not verified yet, Please verify your email");
                    }
                }
                else{
                    utility.showtoast(Login.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void changeinProgress(boolean inProgress){
        if(inProgress){
            Bar.setVisibility(View.VISIBLE);
            Login.setVisibility(View.GONE);
        }else{
            Bar.setVisibility(View.GONE);
            Login.setVisibility(View.VISIBLE);
        }
    }

    Boolean ValidateData(String email, String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please enter correct Email address");
            return false;
        }
        if(Password.length()<6){
            Password.setError("Please enter correct Password");
            return false;
        }
        return true;
    }

}