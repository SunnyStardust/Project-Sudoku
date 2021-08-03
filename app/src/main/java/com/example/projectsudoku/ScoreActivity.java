package com.example.projectsudoku;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ScoreActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        String[] sudokus = {"EASY", "DIFFICULT"};
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ItemAdaptor adaptor = new ItemAdaptor(this, R.layout.dropdown_item,
                R.id.sudokuType, sudokus);
        adaptor.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adaptor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.clearFocus();
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String sudoku = (String) parent.getSelectedItem();
                TextView descript = findViewById(R.id.descript);
                TextView number = findViewById(R.id.score);
                TextView cong = findViewById(R.id.cong);
                int score = 0;
                if (sudoku.equals("EASY")) {
                    descript.setText("Total solved easySudoku:");
                    score = sp.getInt("easyNum", 0);
                } else if (sudoku.equals("DIFFICULT")) {
                    descript.setText("Total solved difficultSudoku:");
                    score = sp.getInt("difficultNum", 0);
                }
                number.setText(""+score);
                if (score < 5) {
                    cong.setText("Good start!");
                } else if (score < 10) {
                    cong.setText("You are really something!");
                } else {
                    cong.setText("You are a legendary!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
