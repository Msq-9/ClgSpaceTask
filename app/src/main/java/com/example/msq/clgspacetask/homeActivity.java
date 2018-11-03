package com.example.msq.clgspacetask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class homeActivity extends AppCompatActivity {

    private TextView prevScore;
    private Button goQuiz;
    private String pScore;


    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pScore = null;

        prevScore = findViewById(R.id.prevScore);
        goQuiz = findViewById(R.id.goQuiz);
        final ProgressDialog progressDialog = new ProgressDialog(homeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("score");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    pScore = postSnapshot.getValue(String.class);
                }
                //Toast.makeText(homeActivity.this, "score: " + pScore, Toast.LENGTH_SHORT).show();
                if(pScore != null){
                    prevScore.setText(pScore);
                }
                else if(pScore.equals("0")){
                    prevScore.setText(0);
                }
                else{
                    String txt = "Give a try first !";
                    prevScore.setText(txt);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        });

        goQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("score");

                myRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                        } else {
                        }
                    }

                });

                startActivity(new Intent(homeActivity.this, quizActivity.class));
            }
        });
    }
}
