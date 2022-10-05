/**
 * Sudoku.java
 * 
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.
 *
 * Amruth Niranjan, Boston University
 */

import java.io.*;   // allows us to read from a file
import java.util.*;

public class Sudoku {    
    // Current contents of the cells of the puzzle 
    private int[][] grid;
    
    /*
     * Indicates whether the value in a given cell is fixed 
     * valIsFixed[r][c] is true if the value in the cell 
     * at row r, column c is fixed, and false otherwise.
     */
    private boolean[][] valIsFixed;
    
    /*
     * 3-D array allows us to determine if a given subgrid
     * already contains a given value:
     *
     *    (0,0)   (0,1)   (0,2)
     *
     *    (1,0)   (1,1)   (1,2)
     * 
     *    (2,0)   (2,1)   (2,2)
     * 
     * ex. subgridHasVal[0][2][5] will be true if the subgrid
     * in the upper right-hand corner already has a 5 in it, 
     * and false otherwise
     */
    private boolean[][][] subgridHasVal;
    

    private boolean[][] colValue;
    private boolean[][] rowValue;
    

    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];     
        
        this.subgridHasVal = new boolean[3][3][10];        

        this.colValue = new boolean[9][10];
        this.rowValue = new boolean[9][10];
    }
    
    /*
     * places the specified value in the cell with the specified
     * coordinates and updates the state of the puzzle
     */
    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row/3][col/3][val] = true;
        /*** UPDATE YOUR ADDITIONAL FIELDS HERE. ***/
        
        this.colValue[col][val] = true;
        this.rowValue[row][val] = true;
    }
        
    /*
     * removes the specified value from the cell with the specified
     * coordinates and updates the state of the puzzle
     */
    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row/3][col/3][val] = false;
        
        /*** UPDATE YOUR ADDITIONAL FIELDS HERE. ***/
        this.colValue[col][val] = false;
        this.rowValue[row][val] = false;
    }  
        
    /*
     * reads the initial config of the puzzle from the Scanner, 
     * and uses that config to initialize state of the puzzle
     */
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
                
    /*
     * Displays the current state of the puzzle
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.grid[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }
        
    // private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }
    

    public boolean isSafe(int val, int row, int col){
        if(!this.colValue[col][val] && !this.rowValue[row][val] && !this.subgridHasVal[row/3][col/3][val]){
            return true;
        }
        return false;
    }
         
    /*
     * recursive-backtracking method - Returns true if
     * a solution has already been found and false otherwise
     */
    private boolean solveRB(int n) {
        if(n == 81){
            return true;
        }
        int row = n / 9;
        int col = n%9;
        if(this.valIsFixed[row][col]){
                return solveRB(n+1);
        }

        for(int i = 0; i < 10; i++){
            if(this.isSafe(i, row, col)){
                this.placeVal(i, row, col);
                if(solveRB(n+1)){
                    return true;
                }
                        // anything below this comment is only reached when we backtrack
            this.removeVal(i, row, col);       
            }
        }
        return false;
    } 
    
    /*
     * wrapper method for solveRB()
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
        
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }    
}
