package de.buw.se4de;

import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Scanner;

public class grid {
    int rows;
    int collums;
    Ship[] ships;
    cell[][] cells;

    Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};

    public grid(){
        this.rows = 10;
        this.collums = 10;
        this.ships= new Ship[5];
        this.cells= new cell[rows][collums];

        for(int i=0;i<rows;i++){
            for (int j=0;j<collums;j++){
                cells[i][j]= new cell();
            }
        }

        this.ships[0] = new Ship2();
        this.ships[1] = new Ship3();
        this.ships[2] = new Ship4();
        this.ships[3] = new Ship4();
        this.ships[4] = new Ship5();

        Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};
    }

    public void setSingleShip(Ship ship){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nThis is a ship of size: "+ship.len);
        System.out.println("\nHorizontal ships Bows always look to the west, and vertical ships bows look to the north");
        System.out.println("\nDo you want this ship horizontally? y/n");
        String hor = scanner.nextLine();
        if(hor.equals("n")){ship.isVert = true;}

        boolean condition = false;
        while(!condition) {
            System.out.println("\nwhich column do you want the ships bow to end? Letter");
            String column = scanner.nextLine();
            ship.BowCol = searchLetters(column);
            System.out.println("\nwhich row do you want the ships bow to end? Number");
            ship.BowRow = scanner.nextInt();


            if (ship.isVert && ship.BowRow+ship.len > 10 || !ship.isVert && ship.BowCol+ship.len > 10) {System.out.println("\nship exceeds boundaries, give new input");}
            else {
                for(int i=0;i < ship.len ;i++){
                    if(ship.isVert){
                        cells[ship.BowCol+i][ship.BowRow].hasShip = true;
                    }
                    else {
                        cells[ship.BowCol][ship.BowRow+i].hasShip = true;
                    }
                }
                condition = true;
            }
        }

    }

    public void setShips(){}

    public void shoot(String row, int collumn){
        int rowInt = searchLetters(row);
        if (cells[rowInt][collumn].hasShip){
            cells[rowInt][collumn].shotShip=true;
        }
        cells[rowInt][collumn].dead=true;
    }

    public boolean checkShip(cell cell){
        return cell.dead && cell.hasShip;
    }

    public void playSingleGame(){
        while (ships.length!=0){
            PrintEnemyGrid();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Where do you wish to shoot? Write a letter and then a Number ");
            String row = scanner.nextLine();
            int collumn = scanner.nextInt();
            shoot(row,collumn);
        }
    }

    public void PrintEnemyGrid() {

        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters[current_row]+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                cell current_cell = cells[current_row][current_col];
                //if cell has a shot ship
                if (checkShip(current_cell)){
                    System.out.print("= ");
                }//if cell is dead but no ship

                else if(current_cell.hasShip){
                    System.out.print("H ");
                }

                else if(current_cell.dead){
                    System.out.print("X ");
                }//if cell is not dead
                else {
                    System.out.print("~ ");
                }
            }
            System.out.println("|"+letters[current_row]);
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 9  ");
    }

    public int searchLetters(String s){
        for (int i=0;i< letters.length;++i) {
            char c = s.charAt(0);
            if (c==letters[i]){
                return i;
            }
        }
        return -1;
    }
}
