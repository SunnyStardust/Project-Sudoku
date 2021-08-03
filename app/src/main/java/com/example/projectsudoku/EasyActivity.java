package com.example.projectsudoku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class EasyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_easy);

        LinearLayout layout = findViewById(R.id.Sudoku);
        LinearLayout numberLayout = findViewById(R.id.numberLayout);
        EasySudoku.getEasySudoku().createGrid(layout, numberLayout, this);
        EasySudoku.getEasySudoku().setText(this);
        EasySudoku.getEasySudoku().makePuzzle(this);
    }

    public void restart(View view) {
        EasySudoku.getEasySudoku().restart(view);
    }

    public void newGame(View view) {
        EasySudoku.getEasySudoku().newGame(view);
    }

    public void submit(View view) {
        if (EasySudoku.getEasySudoku().checkAnswer(view) && !EasySudoku.getEasySudoku().isSolved()) {
            EasySudoku.getEasySudoku().setSolved(true);
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            int num = sp.getInt("easyNum", 0);
            editor.putInt("easyNum", ++num);
            editor.commit();
        }
    }
}
