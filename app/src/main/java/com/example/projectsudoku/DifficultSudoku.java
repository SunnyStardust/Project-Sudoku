package com.example.projectsudoku;

public class DifficultSudoku extends Sudoku {

    public DifficultSudoku() {
        super(DIFFICULT);
    }

    static public DifficultSudoku getDifficultSudoku() {
        return difficultSudoku;
    }

    static private DifficultSudoku difficultSudoku = new DifficultSudoku();
}
