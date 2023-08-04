package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.BoringLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    EditText Email,Password,ConfirmPassword;
    MaterialButton CreateAccount;
    ProgressBar PrBar;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Email = findViewById(R.id.edtEmail);
        Password = findViewById(R.id.edtPassword);
        ConfirmPassword = findViewById(R.id.edtConPassword);
        CreateAccount = findViewById(R.id.btnCreateAccount);
        PrBar = findViewById(R.id.progressBar);
        txtLogin = findViewById(R.id.txtLogin);

        CreateAccount.setOnClickListener(v-> funCreateAccount());
        txtLogin.setOnClickListener(v-> finish());

    }

    void funCreateAccount(){
        String email = Email.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();
        String password = Password.getText().toString();

        boolean isValidated = ValidateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountFirebase(email,password);
    }

    void createAccountFirebase(String email, String password){
        changeinProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeinProgress(false);
                        if(task.isSuccessful()){
                            utility.showtoast(CreateAccount.this,"Account created succesfully");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                        }else{
                            utility.showtoast(CreateAccount.this,task.getException().getLocalizedMessage());
                        }
                    }
                });

    }

    void changeinProgress(boolean inProgress){
        if(inProgress){
            PrBar.setVisibility(View.VISIBLE);
            CreateAccount.setVisibility(View.GONE);
        }else{
            PrBar.setVisibility(View.GONE);
            CreateAccount.setVisibility(View.VISIBLE);
        }
    }

    Boolean ValidateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter correct Email");
            return false;
        }
        if (password.length() < 6) {
            Password.setError("Lenght of the Password should be 6");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            ConfirmPassword.setError("Confirm Password doesn't matches the Password");
            return false;
        }
        return true;
    }

}