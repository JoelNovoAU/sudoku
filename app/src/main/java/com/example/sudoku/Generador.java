package com.example.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Generador {

    private final Random random = new Random();

     public int[][] generateFullBoard() {
        int[][] board = new int[9][9];
        fillBoard(board);
        return board;
    }

    public int[][] generatePuzzle(int empties) {
        int[][] board = generateFullBoard();

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 81; i++) positions.add(i);
        Collections.shuffle(positions, random);

        int removed = 0;
        for (int pos : positions) {
            if (removed >= empties) break;
            int r = pos / 9;
            int c = pos % 9;
            int backup = board[r][c];
            board[r][c] = 0;

            int[][] copy = copyBoard(board);
            int solutions = solveCount(copy, 2);
            if (solutions != 1) {
                board[r][c] = backup;
            } else {
                removed++;
            }
        }

        return board;
    }

    private boolean fillBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    List<Integer> nums = new ArrayList<>();
                    for (int n = 1; n <= 9; n++) nums.add(n);
                    Collections.shuffle(nums, random);

                    for (int num : nums) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public int solveCount(int[][] board, int limit) {
        return solveCountHelper(board, limit, 0);
    }

    private int solveCountHelper(int[][] board, int limit, int foundSoFar) {
        if (foundSoFar >= limit) return foundSoFar;

        int row = -1, col = -1;
        boolean emptyFound = false;
        for (int r = 0; r < 9 && !emptyFound; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    row = r;
                    col = c;
                    emptyFound = true;
                    break;
                }
            }
        }
        if (!emptyFound) {
            return foundSoFar + 1;
        }

        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                foundSoFar = solveCountHelper(board, limit, foundSoFar);
                board[row][col] = 0;

                if (foundSoFar >= limit) return foundSoFar;
            }
        }
        return foundSoFar;
    }

    private boolean isSafe(int[][] board, int r, int c, int num) {
        for (int i = 0; i < 9; i++) if (board[r][i] == num) return false;
        for (int i = 0; i < 9; i++) if (board[i][c] == num) return false;
        int sr = (r / 3) * 3;
        int sc = (c / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        return true;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(board[i], 0, c[i], 0, 9);
        return c;
    }
}
