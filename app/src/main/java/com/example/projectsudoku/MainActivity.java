package com.example.projectsudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onEasy(View view) {
        Intent intent = new Intent(this, EasyActivity.class);
        startActivity(intent);
    }

    public void onDifficult(View view) {
        Intent intent = new Intent(this, DifficultActivity.class);
        startActivity(intent);
    }

    public void onScore(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
}