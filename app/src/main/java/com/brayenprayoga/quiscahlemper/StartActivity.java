package com.brayenprayoga.quiscahlemper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class StartActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE_EASY = "keyHighscoreEasy";
    public static final String KEY_HIGHSCORE_MEDIUM = "keyHighscoreMedium";
    public static final String KEY_HIGHSCORE_HARD = "keyHighscoreHard";

    private TextView textHighscoreEasy;
    private TextView textHighscoreMedium;
    private TextView textHighscoreHard;
    private Spinner spinnerDifficulty;

    private int highscoreEasy, highscoreMedium, highscoreHard;
    MediaPlayer audio;
    private Switch myswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonsound);
        audio = MediaPlayer.create(this, R.raw.pinguins);
        audio.setLooping(true);
        audio.setVolume(1,1);
        audio.start();

        myswitch = (Switch) findViewById(R.id.myswitch);
        myswitch.setChecked(true);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(myswitch.isChecked()){
                    audio.setVolume(1,1);
                }
                else {
                    audio.setVolume(0,0);
            }

            }
        });

        textHighscoreEasy = findViewById(R.id.highscoreeasy);
        textHighscoreMedium = findViewById(R.id.highscoremedium);
        textHighscoreHard = findViewById(R.id.highscorehard);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);

        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);


        loadHighscore();

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
                mp.start();
            }
        });
    }

    private void startQuiz() {
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        Intent intent = new Intent(StartActivity.this, QuisActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int scoreEasy = data.getIntExtra(QuisActivity.EXTRA_SCORE1, 0);
                int scoreMedium = data.getIntExtra(QuisActivity.EXTRA_SCORE2, 0);
                int scoreHard = data.getIntExtra(QuisActivity.EXTRA_SCORE3, 0);
                if (scoreEasy > highscoreEasy) {
                    updateHighscore(scoreEasy);
                }
                if (scoreMedium > highscoreMedium) {
                    updateHighscoreMedium(scoreMedium);
                }
                if (scoreHard > highscoreHard) {
                    updateHighscoreHard(scoreHard);
                }
            }
        }
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscoreEasy = prefs.getInt(KEY_HIGHSCORE_EASY, 0);
        highscoreMedium = prefs.getInt(KEY_HIGHSCORE_MEDIUM, 0);
        highscoreHard = prefs.getInt(KEY_HIGHSCORE_HARD, 0);
        textHighscoreEasy.setText("Highscore Easy: " + highscoreEasy);
        textHighscoreMedium.setText("Highscore Medium: " + highscoreMedium);
        textHighscoreHard.setText("Highscore Hard: " + highscoreHard);
    }

    private void updateHighscore(int highscore) {
        highscoreEasy = highscore;
        textHighscoreEasy.setText("Highscore Easy: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE_EASY, highscore);
        editor.apply();
    }

    private void updateHighscoreHard(int highscore) {
        highscoreHard = highscore;
        textHighscoreHard.setText("Highscore Hard: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE_HARD, highscore);
        editor.apply();
    }

    private void updateHighscoreMedium(int highscore) {
        highscoreMedium = highscore;
        textHighscoreMedium.setText("Highscore Medium: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE_MEDIUM, highscore);
        editor.apply();
    }

    public void onBackPressed(){
        audio.stop();
        StartActivity.this.finish();
    }
}
