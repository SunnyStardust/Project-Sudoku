package com.example.projectsudoku;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class Alert {

    public static void alertFailMessage(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Try again.")
                .setTitle("Oops, your answer is not correct.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context1 = context.getApplicationContext();
                        CharSequence text = "Good luck!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1, text, duration);
                        toast.show();
                    }
                });
        alertDialogBuilder.create().show();
    }

    public static void alertSuccessMessage(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("You did it!")
                .setTitle("Congratulations!")
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context1 = context.getApplicationContext();
                        CharSequence text = "Try different levels!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1, text, duration);
                        toast.show();
                    }
                });
        alertDialogBuilder.create().show();
    }

    public static void alertIncompleteMessage(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Don't give up!")
                .setTitle("You haven't finished yet.")
                .setPositiveButton("Continue", null);
        alertDialogBuilder.create().show();
    }

}
