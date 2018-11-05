package com.example.administrator.brainexercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button goBtn;
    TextView myProblemTV;
    ArrayList<Integer> answers = new ArrayList<>();
    TextView myResponseTV;
    TextView myScoreTV;
    TextView myHighScoreTV;
    TextView myTimerTV;
    int locationOfCorrectAnswer;
    int score;
    int totalQuestion = 0;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button myPlayAgainBtn;
    RelativeLayout myLayout2;
    int highScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myResponseTV = (TextView) findViewById(R.id.myResponseTV);
        myScoreTV = (TextView) findViewById(R.id.myScoreTV);
        myTimerTV = (TextView) findViewById(R.id.myTimerTV);
        button0 = (Button) findViewById(R.id.myChoice1);
        button1 = (Button) findViewById(R.id.myChoice2);
        button2 = (Button) findViewById(R.id.myChoice3);
        button3 = (Button) findViewById(R.id.myChoice4);
        myPlayAgainBtn = (Button) findViewById(R.id.myPlayAgainBtn);
        myPlayAgainBtn.setVisibility(View.INVISIBLE);
        myLayout2 = (RelativeLayout) findViewById(R.id.myLayout2);
        goBtn = (Button) findViewById(R.id.myStartBtn);
        myProblemTV = (TextView) findViewById(R.id.myProblemTV);
        myHighScoreTV = (TextView) findViewById(R.id.myHighScoreTV);

        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("com.example.administrator.brainexercise",Context.MODE_PRIVATE);
        //เก็บค่าในตัวแปร username แต่ถ้าหาก key username ไม่มี ก็ให้ใช้ค่า "" เปง default แทน
        highScore  = sharedPreferences.getInt("highScore",0);
        myHighScoreTV.setText("High Score: " + highScore);
    }



    public void start(View view) {
        goBtn.setVisibility(View.INVISIBLE);
        myLayout2.setVisibility(View.VISIBLE);
        playAgain(findViewById(R.id.myPlayAgainBtn));
        myHighScoreTV.setVisibility(View.INVISIBLE);
    }

    public void choose(View view) {
        //Log.i("Tag", (String) view.getTag());

        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            Log.i("Correct", Integer.toString(locationOfCorrectAnswer));
            score++;
            myResponseTV.setText("Correct");
        } else {
            myResponseTV.setText("Incorrect");
        }
        totalQuestion++;
        myScoreTV.setText(score + "/" + totalQuestion);
        generateQuestion();
    }

    public void generateQuestion() {
        answers.clear();
        Random r = new Random();
        int a = r.nextInt(21);
        int b = r.nextInt(21);
        myProblemTV.setText(Integer.toString(a) + " + " + Integer.toString(b));
        locationOfCorrectAnswer = r.nextInt(4);
        int incorrectAnswer;
        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(a + b);
            } else {
                incorrectAnswer = r.nextInt(41);
                while (incorrectAnswer == locationOfCorrectAnswer) {
                    incorrectAnswer = r.nextInt(41);
                }

                answers.add(incorrectAnswer);
            }
        }
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void playAgain(View v) {
        score = 0;
        totalQuestion = 0;
        myTimerTV.setText("30s");
        myResponseTV.setText("");
        myScoreTV.setText("0/0");
        myPlayAgainBtn.setVisibility(View.INVISIBLE);
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        generateQuestion();

        new CountDownTimer(20100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.i("countdown", String.valueOf(millisUntilFinished / 1000));
                myTimerTV.setText(Integer.toString((int) millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                //Log.i("COuntdown", "finished");
                myResponseTV.setText("Your Score:" + " " + score);
                myTimerTV.setText("0s");
                myPlayAgainBtn.setVisibility(View.VISIBLE);
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                if (score>highScore){
                    SharedPreferences sharedPreferences =
                            getApplicationContext().getSharedPreferences("com.example.administrator.brainexercise",Context.MODE_PRIVATE);
                    sharedPreferences.edit().putInt("highScore",score).apply();
                    //เก็บค่าในตัวแปร username แต่ถ้าหาก key username ไม่มี ก็ให้ใช้ค่า "" เปง default แทน
                    //String username = sharedPreferences.getString("username","");
                    Toast.makeText(getApplicationContext(),"You got High Score: " + score,Toast.LENGTH_LONG).show();

                }
            }
        }.start();
    }

}
