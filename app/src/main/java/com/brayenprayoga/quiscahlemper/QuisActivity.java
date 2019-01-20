package com.brayenprayoga.quiscahlemper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.brayenprayoga.quiscahlemper.StartActivity.KEY_HIGHSCORE_EASY;
import static com.brayenprayoga.quiscahlemper.StartActivity.KEY_HIGHSCORE_HARD;
import static com.brayenprayoga.quiscahlemper.StartActivity.KEY_HIGHSCORE_MEDIUM;
import static com.brayenprayoga.quiscahlemper.StartActivity.SHARED_PREFS;

public class QuisActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE1 = "extraScore1";
    public static final String EXTRA_SCORE2 = "extraScore2";
    public static final String EXTRA_SCORE3 = "extraScore3";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewDifficulty;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int scoreEASY;
    private int scoreMEDIUM;
    private int scoreHARD;
    private boolean answered;
    private long backPressedTime;
    public String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quis);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
        difficulty = intent.getStringExtra(StartActivity.EXTRA_DIFFICULTY);

        textViewDifficulty.setText("Difficulty: " + difficulty);

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuisActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
                ONCLICK(v);
            }
        });

        if (savedInstanceState == null) {
            QuisDbHelper dbHelper = new QuisDbHelper(this);
            questionList = dbHelper.getQuestions(difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            scoreEASY = savedInstanceState.getInt(KEY_SCORE);
            scoreMEDIUM = savedInstanceState.getInt(KEY_SCORE);
            scoreHARD = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }
    }

    public void ONCLICK(View V){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonsound);
        mp.start();
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz(difficulty);
        }
    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        TextView hyhy = (TextView) findViewById(R.id.text_view_difficulty);
        String dif = hyhy.getText().toString();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        switch (dif) {
            case "Difficulty: Easy":
                if (answerNr == currentQuestion.getAnswerNr()) {
                    scoreEASY++;
                    textViewScore.setText("Score: " + scoreEASY);
                }
                break;
            case "Difficulty: Medium":
                if (answerNr == currentQuestion.getAnswerNr()) {
                    scoreMEDIUM++;
                    textViewScore.setText("Score: " + scoreMEDIUM);
                }
                break;
            case "Difficulty: Hard":
                if (answerNr == currentQuestion.getAnswerNr()) {
                    scoreHARD++;
                    textViewScore.setText("Score: " + scoreHARD);
                }
                break;
                default: Toast.makeText(this, "DEFAULT", Toast.LENGTH_SHORT).show();
        }

        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Jawaban nya adalah A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Jawaban nya adalah B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Jawaban nya adalah C");
                break;
        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");

        }
    }
    private void finishQuiz(String dif) {
//        Intent resultIntent = new Intent(QuisActivity.this,StartActivity.class);
////            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
////            SharedPreferences.Editor editor = prefs.edit();
////            if(dif.equals("Easy")){
////
////                editor.putInt(KEY_HIGHSCORE_EASY, scoreEASY);
////            }else if (dif.equals("Medium")){
////
////                editor.putInt(KEY_HIGHSCORE_MEDIUM, scoreMEDIUM);
////            }else {
////                editor.putInt(KEY_HIGHSCORE_HARD, scoreHARD);
////            }
////            editor.commit();
////            startActivity(resultIntent);
////            finish();

        Intent resultIntent = new Intent();
        if(dif.equals("Easy")){
                resultIntent.putExtra(EXTRA_SCORE1, scoreEASY);
        }else if (dif.equals("Medium")){
                resultIntent.putExtra(EXTRA_SCORE2, scoreMEDIUM);
        }else if(dif.equals("Hard")){
                resultIntent.putExtra(EXTRA_SCORE3, scoreHARD);
            }
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz(difficulty);
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, scoreEASY);
        outState.putInt(KEY_SCORE, scoreMEDIUM);
        outState.putInt(KEY_SCORE, scoreHARD);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
