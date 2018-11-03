package com.example.msq.clgspacetask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class quizActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnClickListener, Animation.AnimationListener {

    private LinearLayout options[] = new LinearLayout[4];
    private Button option_[] = new Button[4];
    private Button option[] = new Button[4];
    private TextView answers[] = new TextView[4];
    private TextView question, qNo, score;
    private GestureDetector gestureDetector;

    private String[] questions = new String[5];
    private String[][] givenOptions = new String[5][4];
    private String[] answer = new String[5];

    private int index = 0;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    Animation quesFadeIn, quesFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("score");

        questions[0] = "Which of the followings is/are automatically added to every class, if we do not write our own ?";
        answer[0] = "4";
        givenOptions[0][0] = "Copy Constructor";
        givenOptions[0][1] = "Assignment Operator";
        givenOptions[0][2] = "A constructor without any parameter";
        givenOptions[0][3] = "All of the above";

        questions[1] = "When a copy constructor may be called ?";
        answer[1] = "4";
        givenOptions[1][0] = "When an object of the class is passed (to a function) by value as an argument.";
        givenOptions[1][1] = "When an object is constructed based on another object of the same class";
        givenOptions[1][2] = "When compiler generates a temporary object.";
        givenOptions[1][3] = "All of the above";

        questions[2] = "Given the basic ER and relational models, which of the following is INCORRECT ?";
        answer[2] = "3";
        givenOptions[2][0] = "An attribute of an entity can have more than one value";
        givenOptions[2][1] = "An attribute of an entity can be composite";
        givenOptions[2][2] = "In a row of a relational table, an attribute can have more than one value";
        givenOptions[2][3] = "In a row of a relational table, an attribute can have exactly one value or a NULL value";

        questions[3] = "There are 25 horses among which you need to find out the fastest 3 horses. You can conduct race among at most 5 to find out their relative speed. At no point you can find out the actual speed of the horse in a race. Find out how many races are required to get the top 3 horses.";
        answer[3] = "3";
        givenOptions[3][0] = "5";
        givenOptions[3][1] = "6";
        givenOptions[3][2] = "7";
        givenOptions[3][3] = "8";

        questions[4] = "What is called when a function is defined inside a class?   ";
        answer[4] = "4";
        givenOptions[4][0] = "Module";
        givenOptions[4][1] = "Class";
        givenOptions[4][2] = "Another Function";
        givenOptions[4][3] = "Method";

        option_[0] = findViewById(R.id.option_A);
        option_[1] = findViewById(R.id.option_B);
        option_[2] = findViewById(R.id.option_C);
        option_[3] = findViewById(R.id.option_D);

        option[0] = findViewById(R.id.optionA);
        option[1] = findViewById(R.id.optionB);
        option[2] = findViewById(R.id.optionC);
        option[3] = findViewById(R.id.optionD);

        options[0] = findViewById(R.id.option1);
        options[1] = findViewById(R.id.option2);
        options[2] = findViewById(R.id.option3);
        options[3] = findViewById(R.id.option4);

        answers[0] = findViewById(R.id.answer_1);
        answers[1] = findViewById(R.id.answer_2);
        answers[2] = findViewById(R.id.answer_3);
        answers[3] = findViewById(R.id.answer_4);

        question = findViewById(R.id.question);
        qNo = findViewById(R.id.qNo);
        score = findViewById(R.id.score);

        question.setText(questions[0]);
        for(int i=0;i<4;i++)
            answers[i].setText(givenOptions[index][i]);

        option[0].setOnClickListener(this);
        option[1].setOnClickListener(this);
        option[2].setOnClickListener(this);
        option[3].setOnClickListener(this);

        option_[0].setOnClickListener(this);
        option_[1].setOnClickListener(this);
        option_[2].setOnClickListener(this);
        option_[3].setOnClickListener(this);

        quesFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ques_fade_in);
        quesFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ques_fade_out);

        quesFadeIn.setAnimationListener(this);
        quesFadeOut.setAnimationListener(this);

        gestureDetector = new GestureDetector(this);
        qNo.setText("1/5");

    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if(e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 100) {
                //Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
                if(index <= 5)
                    swipeLeft();
                return true;
            } else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 100) {
                //Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
                if(index >= 1)
                    swipeRight();
                return true;
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    private void swipeLeft() {

        //Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();

        for(int i=0;i<4;i++){
            option[i].setEnabled(true);
            option_[i].setEnabled(true);

            option[i].setBackgroundResource(R.drawable.options_btn);
            option_[i].setBackgroundResource(R.drawable.abcd_btn);
        }

        index++;
        if(index < 1)
            index = 1;

        if(index > 5) {
            index = 5;
            return;
        }

        qNo.setText(new StringBuilder().append(String.valueOf(index)).append("/").append(String.valueOf(5)));

        question.startAnimation(quesFadeIn);
        question.setText(questions[(index - 1)]);

        for (int i = 0; i < 4; i++) {
            answers[i].setText(givenOptions[index][i]);
            answers[i].startAnimation(quesFadeOut);
            answers[i].startAnimation(quesFadeIn);
        }

    }

    private void swipeRight() {

        //Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();

        for(int i=0;i<4;i++){
            option[i].setEnabled(true);
            option_[i].setEnabled(true);

            option[i].setBackgroundResource(R.drawable.options_btn);
            option_[i].setBackgroundResource(R.drawable.abcd_btn);
        }

        index--;
        if(index < 1){
            index = 1;
            return;
        }

        if(index > 5)
            index = 5;


        qNo.setText(new StringBuilder().append(String.valueOf(index)).append("/").append(String.valueOf(5)));

        question.startAnimation(quesFadeIn);

        question.setText(questions[(index - 1)]);

        for (int i = 0; i < 4; i++) {
            answers[i].setText(givenOptions[index][i]);
            answers[i].startAnimation(quesFadeOut);
            answers[i].startAnimation(quesFadeIn);
        }
    }

    @Override
    public void onClick(View view) {
        String qNum =  qNo.getText().toString();
        String[] parts = qNum.split("/");
        int currQnNo = Integer.valueOf(parts[0]);
        String ans = answer[currQnNo-1];
        int optNo = Integer.valueOf(ans);

        switch(view.getId()){
            case R.id.option_A:
                updateButton(optNo, 1);
                break;
            case R.id.optionA:
                updateButton(optNo, 1);
                break;

            case R.id.option_B:
                updateButton(optNo, 2);
                break;
            case R.id.optionB:
                updateButton(optNo, 2);
                break;

            case R.id.option_C:
                updateButton(optNo, 3);
                break;
            case R.id.optionC:
                updateButton(optNo, 3);
                break;

            case R.id.option_D:
                updateButton(optNo, 4);
                break;
            case R.id.optionD:
                updateButton(optNo, 4);
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void updateButton(int correctOptNo, int choosenOptNo){

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(correctOptNo != choosenOptNo){
            option[choosenOptNo-1].setBackgroundResource(R.drawable.options_btn_red);
            option_[choosenOptNo-1].setBackgroundResource(R.drawable.abcd_btn_red);

            options[choosenOptNo-1].startAnimation(shake);

            option[correctOptNo-1].setBackgroundResource(R.drawable.options_btn_green);
            option_[correctOptNo-1].setBackgroundResource(R.drawable.abcd_btn_green);

            for(int i=0;i<4;i++){
                option[i].setEnabled(false);
                option_[i].setEnabled(false);
            }

            //myRef.child("score").child(user.getUid()).setValue(String.valueOf(Integer.parseInt(score.getText().toString()) - 1));
            String currScore = String.valueOf(Integer.parseInt(score.getText().toString()) - 1);
            myRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(currScore).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                    } else {
                    }
                }

            });
            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) - 1));

        } else {
            option[choosenOptNo-1].setBackgroundResource(R.drawable.options_btn_green);
            option_[choosenOptNo-1].setBackgroundResource(R.drawable.abcd_btn_green);

            //myRef.child("score").child(user.getUid()).setValue(String.valueOf(Integer.parseInt(score.getText().toString()) + 1));
            String currScore = String.valueOf(Integer.parseInt(score.getText().toString()) + 1);
            myRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .setValue(currScore).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                    }
                    else{
                    }
                }

            });
            score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + 1));

            for(int i=0;i<4;i++){
                option[i].setEnabled(false);
                option_[i].setEnabled(false);
            }
        }

    }
}
