package com.example.msq.clgspacetask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.text.TextUtils.isEmpty;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password;
    private Button login, signUp;
    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean credentialsValid() {

        if (username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {

            Toast.makeText(loginActivity.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!isValidEmail(username.getText().toString().trim())) {

            Toast.makeText(loginActivity.this, "Invalid EmailId !", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameL);
        password = findViewById(R.id.passwordL);

        login = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signUpBtn);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(loginActivity.this, homeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loginBtn:
                if(credentialsValid()){
                    userLogin();
                }
                break;
            case R.id.signUpBtn:
                startActivity(new Intent(loginActivity.this, signUpActivity.class));
                finish();
        }
    }

    private void userLogin(){
        String email = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        progressDialog = new ProgressDialog(loginActivity.this);
        progressDialog.setMessage("Signing In, Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //Toast.makeText(loginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginActivity.this, homeActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(loginActivity.this, "Login Failed, Check credentials and try again !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
