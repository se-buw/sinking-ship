package de.buw.se4de;

import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Scanner;

public class grid {
    int rows = 10;
    int collums = 10;
    Ship[] ships= new Ship[5];
    cell[][] cells= new cell[rows][collums];
    Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};


    public void print() {
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters[current_row]+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                cell current_cell = cells[current_row][current_col];
                if(current_cell==null){
                    System.out.print("~ ");
                }else if (checkShip(current_cell)){
                    System.out.println("O ");
                }else if(current_cell.dead){
                    System.out.print("X ");
                }else if(current_cell.hasShip){
                    System.out.print("~ ");
                }else{
                    System.out.println("E ");
                }
            }
            System.out.println("|"+letters[current_row]);
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 9  ");
    }
    public void shoot(String row, int collum){
        int rowInt = searchLetters(row);
        if (cells[rowInt][collum].hasShip){
            cells[rowInt][collum].shotShip=true;
        }
        cells[rowInt][collum].dead=true;
    }
    public boolean checkShip(cell cell){
        if (!cell.dead){
            return false;
        }if(!cell.hasShip){
            return false;
        }
        return true;
    }

    public void playsinglegame(){
        while (ships.length!=0){
            print();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Where do you wish to shoot? Letter Number ");
            String row = scanner.nextLine();
            int collum = scanner.nextInt();
            shoot(row,collum);

        }
    }

    public int searchLetters(String s){
        for (int i =0;i< letters.length;++i) {
            char c = s.charAt(0);
            if (c==letters[i]){
                return i;
            }
        }
        return -1;
    }
}
