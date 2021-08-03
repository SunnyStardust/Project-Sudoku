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

public class DifficultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_difficult);

        LinearLayout layout = findViewById(R.id.Sudoku);
        LinearLayout numberLayout = findViewById(R.id.numberLayout);
        DifficultSudoku.getDifficultSudoku().createGrid(layout, numberLayout, this);
        DifficultSudoku.getDifficultSudoku().setText(this);
        DifficultSudoku.getDifficultSudoku().makePuzzle(this);
    }

    public void restart(View view) {
        DifficultSudoku.getDifficultSudoku().restart(view);
    }

    public void newGame(View view) {
        DifficultSudoku.getDifficultSudoku().newGame(view);
    }

    public void submit(View view) {
        if (DifficultSudoku.getDifficultSudoku().checkAnswer(view) && !DifficultSudoku.getDifficultSudoku().isSolved()) {
            DifficultSudoku.getDifficultSudoku().setSolved(true);
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            int num = sp.getInt("difficultNum", 0);
            editor.putInt("difficultNum", ++num);
            editor.commit();
        }
    }
}
