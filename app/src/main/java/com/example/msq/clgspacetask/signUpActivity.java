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

import static android.text.TextUtils.isEmpty;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password;
    private Button signUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean credentialsValid() {

        if (username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {

            Toast.makeText(signUpActivity.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!isValidEmail(username.getText().toString().trim())) {

            Toast.makeText(signUpActivity.this, "Invalid EmailId !", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.usernameR);
        password = findViewById(R.id.passwordR);

        signUp = findViewById(R.id.signMeUpBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(credentialsValid()) {

                    String emailID = username.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    progressDialog = new ProgressDialog(signUpActivity.this);
                    progressDialog.setMessage("Signing Up, Please wait...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(emailID, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(signUpActivity.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();

                                        signUpActivity.this.startActivity(new Intent(signUpActivity.this, homeActivity.class));
                                        finish();

                                    } else {
                                        Toast.makeText(signUpActivity.this, "Sign Up failed, try Again !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

    }
    @Override
    public void onClick(View view) {

    }
}
