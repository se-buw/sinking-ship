package de.buw.se4de;

import com.google.common.collect.Table;
import java.util.ArrayList;

public class grid {
    int rows = 10;
    int collums = 10;
    Ship[] ships= new Ship[5];
    cell[][] cells= new cell[rows][collums];

    boolean allShipsSunk(){
        for (Ship ship:ships){
            if (!ship.isSunk){
                return false;
            }
        }
        //ships.len=0??
        return  true;
    }

    public void print() {
        ArrayList<String> letters = new ArrayList<>();
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        letters.add("E");
        letters.add("F");
        letters.add("G");
        letters.add("H");
        letters.add("I");
        letters.add("J");

        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters.get(current_row)+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                if(cells[current_row][current_col]==null){
                    System.out.print("~ ");
                } else if(cells[current_row][current_col].dead){
                    System.out.print("X ");
                } else {
                    System.out.print(cells[current_row][current_col]);
                }
            }
            System.out.println("|"+letters.get(current_row));
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 9  ");
    }
}
