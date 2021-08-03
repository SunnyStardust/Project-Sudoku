package com.example.projectsudoku;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.os.IResultReceiver;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class Sudoku {

    private Random rand = new Random();

    public static final int EASY = 3;
    public static final int DIFFICULT = 9;

    private ArrayList<ArrayList<Integer>> board;
    private final Integer type;
    private int selectedNum = -1;
    private boolean solved = false;
    private ArrayList<ArrayList<Integer>> answer;
    private Set<Integer> disappear = new HashSet<>();
    private Set<Integer> inputs = new HashSet<>();

    public Sudoku(int type) {
        if (type != EASY && type != DIFFICULT) {
            throw new IllegalArgumentException("type not supported");
        }
        this.type = type;
        this.board = generateSudoku(type);
        generatePuzzle();
    }

    /**
     * Set the texts in the board cells with the generated Sudoku board.
     */
    public void setText(Context context) {
        for (int i = 0; i < this.type; ++i) {
            for (int j = 0; j < this.type; ++j) {
                TextView txt = ((Activity)context).findViewById(i*this.type+j);
                txt.setText("" + this.board.get(i).get(j));
            }
        }
    }

    /**
     * Make certain cells disappear.
     * @param context
     */
    public void makePuzzle(Context context) {
        for (int id : this.disappear) {
            TextView txt = ((Activity)context).findViewById(id);
            txt.setText("");
            setColor(context);
        }
    }

    public void setColor(Context context) {
        for (int i = 0; i < type*type; ++i) {
            TextView txt = ((Activity)context).findViewById(i);
            if (this.disappear.contains(i) || this.inputs.contains(i)) {
                txt.setTextColor(BLUE);
            } else {
                txt.setTextColor(BLACK);
            }
        }
    }

    /**
     * Create the Sudoku board and the helper number cells.
     * @param layout: the board layout
     * @param numberLayout: the helper layout
     * @param context: difficult/easy
     */
    public void createGrid(LinearLayout layout, LinearLayout numberLayout, Context context) {
        for (int i = 0; i < this.type; ++i) {
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBackgroundColor(BLACK);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.CENTER;
            row.setLayoutParams(layoutParams);
            // insert the Sudoku cells
            for (int j = 0; j < this.type; ++j) {
                TextView txt = new TextView(context);
                int side = this.type == Sudoku.EASY ? 300 : 100;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        side,
                        side
                );
                // set the border
                if (i % 3 == 0 && j % 3 == 0) {
                    params.setMargins(16, 16, 4, 4);
                } else if (i % 3 == 0) {
                    if (j == 8 && type == Sudoku.DIFFICULT || j == 2 && type == Sudoku.EASY) {
                        params.setMargins(4,16,16,4);
                    } else {
                        params.setMargins(4, 16, 4, 4);
                    }
                } else if (j % 3 == 0) {
                    params.setMargins(16,4,4,4);
                } else if (i == 8 && j == 8 && type == Sudoku.DIFFICULT || i == 2 && j == 2 && type == Sudoku.EASY) {
                    params.setMargins(4, 4, 16,16);
                } else if (j == 8 && type == Sudoku.DIFFICULT || j == 2 && type == Sudoku.EASY) {
                    params.setMargins(4,4,16,4);
                } else if (i == 8 && type == Sudoku.DIFFICULT || i == 2 && type == Sudoku.EASY) {
                    params.setMargins(4,4,4,16);
                } else {
                    params.setMargins(4,4,4,4);
                }
                txt.setLayoutParams(params);
                txt.setId(i*type + j);
                txt.setBackgroundColor(WHITE);
                txt.setGravity(Gravity.CENTER);
                int textSize = type == Sudoku.EASY? 20 : 10;
                txt.setTextSize(textSize);
                txt.setTypeface(Typeface.DEFAULT_BOLD);
                // If it was originally an empty cell, turn it blue.
                if (this.inputs.contains(txt.getId())) {
                    txt.setTextColor(BLUE);
                }
                txt.setOnClickListener(this::inputNum);
                row.addView(txt);
            }
            layout.addView(row);
        }
        // set the helper number cells to be selected
        for (int i = 0; i < type; ++i) {
            TextView textView  = new TextView(context);
            int side = type == Sudoku.EASY ? 150 : 60;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    side, side
            );
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setId(100 + i);
            textView.setText(("" + (i+1)));
            textView.setOnClickListener(this::selectNum);
            numberLayout.addView(textView);
        }
    }

    /**
     * Select a number from the helper number cells.
     * @param view
     */
    public void selectNum(View view) {
        TextView textView = (TextView)view;
        selectedNum = Integer.parseInt(textView.getText().toString());
        for (int i = 100; i < 100 + type; ++i) {
            TextView text = ((Activity)view.getContext()).findViewById(i);
            text.setBackgroundColor(WHITE);
        }
        textView.setBackgroundColor(YELLOW);
    }

    /**
     * Replace a cell of the board with the number of the selected helper number cell.
     * @param view
     */
    public void inputNum(View view) {
        TextView txt = (TextView) view;
        if ((this.disappear.contains(txt.getId()) ||
                this.inputs.contains(txt.getId())) && selectedNum != -1) {
            txt.setText("" + selectedNum);
            this.disappear.remove(txt.getId());
            this.inputs.add(txt.getId());
            int i = (int)txt.getId() / this.type;
            int j = (int)txt.getId() % this.type;
            this.answer.get(i).set(j, selectedNum);
        }
    }

    /**
     * Initialize the arraylist that stores the input answer.
     */
    public void generatePuzzle() {
        int i = 0;
        while (i < (this.type == EASY ? 5 : 50)) {
            int num = rand.nextInt(type * type);
            if (!this.disappear.contains(num)) {
                this.disappear.add(num);
                ++i;
            }
        }
        this.answer = initializeAnswer(this.type);
    }

    public ArrayList<ArrayList<Integer>> initializeAnswer(int type) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        // Initialize the values to 0.
        for (int i = 0; i < type; ++i) {
            ArrayList<Integer> row = new ArrayList<>(type);
            for (int j = 0; j < type; ++j) {
                row.add(0);
            }
            res.add(row);
        }
        // Copy the values of the board to the answer.
        for (int i = 0; i < type; ++i) {
            for (int j = 0; j < type; ++j) {
                if (!this.disappear.contains(i*type+j)) {
                    res.get(i).set(j, this.board.get(i).get(j));
                }
            }
        }
        return res;
    }

    public ArrayList<ArrayList<Integer>> generateSudoku(int type) {
        ArrayList<ArrayList<Integer>> nums = allPermutations(type);
        ArrayList<ArrayList<Integer>> board = new ArrayList<>();
        for (int i = 0; i < type; ++i) {
            board.add(nums.get(rand.nextInt(nums.size())));
            loop1:
            for (int j = 0; j < type; ++j) {
                for (int k = 0; k < i; ++k) {
                    if (board.get(i).get(j).equals(board.get(k).get(j))) {
                        board.remove(i--);
                        break loop1;
                    }
                }
                if (type == DIFFICULT) {
                    loop2:
                    for (int m = 3 * (i / 3); m < i; ++m) {
                        for (int n = 3 * (j / 3); n < 3 * (j / 3) + 3; ++n) {
                            if (board.get(i).get(j).equals(board.get(m).get(n))) {
                                board.remove(i--);
                                break loop2;
                            }
                        }
                    }
                }
            }
        }
        return board;
    }

    public static ArrayList<ArrayList<Integer>> allPermutations(int type) {
        ArrayList<Integer> nums = type == EASY ? new ArrayList<>(Arrays.asList(1, 2, 3)) :
                new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        boolean[] visited = new boolean[type];
        createPermutation(nums, visited, new ArrayList<>(), res);
        return res;
    }

    public static void createPermutation(ArrayList<Integer> nums, boolean[] visited,
                                         ArrayList<Integer> per, ArrayList<ArrayList<Integer>> res) {
        if (per.size() == nums.size()) {
            res.add(new ArrayList<>(per));
            return;
        }
        for (int num : nums) {
            if (!visited[num - 1]) {
                visited[num - 1] = true;
                per.add(num);
                createPermutation(nums, visited, per, res);
                visited[num - 1] = false;
                per.remove(per.size() - 1);
            }
        }
    }

    public boolean fullAnswer() {
        for (int i = 0; i < this.type; ++i) {
            for (int j = 0; j < this.type; ++j) {
                if (this.answer.get(i).get(j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Submit the answer.
     * @param view
     */
    public boolean checkAnswer(View view) {
        if (!fullAnswer()) {
            Alert.alertIncompleteMessage(view.getContext());
            return false;
        }
        boolean[] numbers = new boolean[this.type];
        // check rows
        for (int i = 0; i < this.type; ++i) {
            for (int j = 0; j < this.type; ++j) {
                if (numbers[answer.get(i).get(j)-1]) {
                    Alert.alertFailMessage(view.getContext());
                    return false;
                } else {
                    numbers[answer.get(i).get(j)-1] = true;
                }
            }
            numbers = new boolean[this.type];
        }
        // check columns
        for (int j = 0; j < this.type; ++j) {
            for (int i = 0; i < this.type; ++i) {
                if (numbers[this.answer.get(i).get(j)-1]) {
                    Alert.alertFailMessage(view.getContext());
                    return false;
                } else {
                    numbers[this.answer.get(i).get(j)-1] = true;
                }
            }
            numbers = new boolean[this.type];
        }
        // 9*9 special case
        if (this.type == DIFFICULT) {
            for (int i = 0; i < 9; i+= 3) {
                System.out.println("wow" + i);
                for (int j = i; j < i + 3; ++j) {
                    for (int k = 0; k < 3; ++k) {
                        if (numbers[this.answer.get(j).get(k)-1]) {
                            Alert.alertFailMessage(view.getContext());
                            return false;
                        } else {
                            numbers[this.answer.get(j).get(k)-1] = true;
                        }
                    }
                }
                numbers = new boolean[this.type];
                for (int j = i; j < i + 3; ++j) {
                    for (int k = 3; k < 6; ++k) {
                        if (numbers[this.answer.get(j).get(k)-1]) {
                            Alert.alertFailMessage(view.getContext());
                            return false;
                        } else {
                            numbers[this.answer.get(j).get(k)-1] = true;
                        }
                    }
                }
                numbers = new boolean[this.type];
                for (int j = i; j < i + 3; ++j) {
                    for (int k = 6; k < 9; ++k) {
                        if (numbers[this.answer.get(j).get(k)-1]) {
                            Alert.alertFailMessage(view.getContext());
                            return false;
                        } else {
                            numbers[this.answer.get(j).get(k)-1] = true;
                        }
                    }
                }
                numbers = new boolean[this.type];
            }
        }
        Alert.alertSuccessMessage(view.getContext());
        return true;
    }

    /**
     * Restart the game.
     * @param view
     */
    public void restart(View view) {
        this.disappear.addAll(this.inputs);
        this.inputs.clear();
        this.answer = initializeAnswer(this.type);
        setText((Activity)view.getContext());
        makePuzzle((Activity)view.getContext());
    }

    /**
     * Reset the board and start a new game.
     * @param view
     */
    public void newGame(View view) {
        this.disappear.clear();
        this.inputs.clear();
        this.board = generateSudoku(this.type);
        this.solved = false;
        generatePuzzle();
        setText((Activity)view.getContext());
        makePuzzle((Activity)view.getContext());
    }

    public ArrayList<ArrayList<Integer>> getBoard() {
        return this.board;
    }

    public int getType() {
        return this.type;
    }

    public Set<Integer> getDisappear() {
        return this.disappear;
    }

    public Set<Integer> getInputs() {
        return inputs;
    }

    public ArrayList<ArrayList<Integer>> getAnswer() {
        return answer;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
