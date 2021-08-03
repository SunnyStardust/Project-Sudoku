package com.example.projectsudoku;

public class EasySudoku extends Sudoku{

    public EasySudoku() {
        super(EASY);
    }

    static public EasySudoku getEasySudoku() {
        return easySudoku;
    }

    static private EasySudoku easySudoku = new EasySudoku();
}
