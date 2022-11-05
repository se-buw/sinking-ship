package de.buw.se4de;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class grid {
    int rows;
    int collums;
    Ship[] ships;
    cell[][] cells;
    Scanner scanner = new Scanner(System.in);
    Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};
    ArrayList<Integer> shiplength= new ArrayList<>(Arrays.asList(5,4,4,3,3,2));

    public grid(){
        this.rows = 10;
        this.collums = 10;
        this.ships= new Ship[6];
        this.cells= new cell[rows][collums];

        for(int i=0;i<rows;i++){
            for (int j=0;j<collums;j++){
                cells[i][j]= new cell();
            }
        }

        for (int i =0;i< ships.length;i++){
            ships[i]=new Ship();
        }/*
        this.ships[0] = new Ship2();
        this.ships[1] = new Ship3();
        this.ships[2] = new Ship4();
        this.ships[3] = new Ship4();
        this.ships[4] = new Ship5();
        */
    }

    public void SetPlayerShip(){
        //checking wich ship to place
        boolean NotValidLength = true;
        int len=0;
        while (NotValidLength) {
            System.out.println("You have the following ship lengths remaining: ");
            printShipLengths();
            System.out.println("Which ship do you want to place? (in length)");
            len = scanner.nextInt();
            //checking for valid len, still need to catch non-number inputs
            if (!lengthCheck(len)) {
                System.out.println("Please try again with a valid length.");
            }else {
                NotValidLength = false;
                shiplength.remove(len);
            }
        }
        Ship ship = new Ship();
        ship.len = len;
        System.out.println("\nThis is a ship of size: "+ship.len);
        System.out.println("\nHorizontal ships Bows always look to the west, and vertical ships bows look to the north");
        System.out.println("\nDo you want this ship horizontally? y/n");
        String hor = scanner.nextLine();
        if(hor.equals("n")){ship.isVert = true;}
        //need to rename condition
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

    public void shoot(String row, int column){
        int rowInt = searchLetters(row);
        if (cells[rowInt][column].hasShip){
            cells[rowInt][column].shotShip=true;
        }
        cells[rowInt][column].dead=true;
    }

    //checks if cell was shot and has a ship, so it can display the ship
    public boolean checkShip(cell cell){
        return cell.dead && cell.hasShip;
    }

    public void playSingleGame(){
        while (ships.length!=0){
            PrintEnemyGrid();

            System.out.println("Where do you wish to shoot? Write a letter and then a Number ");
            String row = scanner.nextLine();
            int column = scanner.nextInt();
            shoot(row,column);
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
                    System.out.print("0 ");
                }//if cell has a ship (testing)
                else if(current_cell.hasShip){
                    System.out.print("H ");
                }//cell was shot but missed ships
                else if(current_cell.dead){
                    System.out.print("X ");
                }//if cell was not touched yet
                else {
                    System.out.print("~ ");
                }
            }
            System.out.println("|"+letters[current_row]);
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 9  ");
    }
    //let the enemy place one ship
    //just hardcoding some ships
    public void placingShip(){
        //checking wich ship to place
        boolean NotValidLength = true;
        int len=0;
        while (NotValidLength) {
            System.out.println("You have the following ship lengths remaining: ");
            printShipLengths();
            System.out.println("Which ship do you want to place? (in length)");
            len = scanner.nextInt();
            //checking for valid len, still need to catch non-number inputs
            if (!lengthCheck(len)) {
                System.out.println("Please try again with a valid length.");
            }else {
                NotValidLength = false;
                shiplength.remove(len);
            }
        }
        //where to place ship: row or column
        boolean NotValidShipPos = true;
        boolean ShipInRow;
        while (NotValidShipPos){
            System.out.println("Where do you want to place your ship? Row (r) / Column (c)");
            String s = scanner.nextLine();
            if (s.equals("r")||s.equals("R")){
                NotValidShipPos = false;
                ShipInRow = true;
            }else if (s.equals("c") ||  s.equals("C")){
                NotValidShipPos =false;
                System.out.println("In which columns do you want to place the ship? start NextLine end");
                int start =  scanner.nextInt();
                int end = scanner.nextInt();
                int diff = end-start;
                if(diff!=len){
                    System.out.println("Please enter Ship with valid length.");
                }else{

                }
                ShipInRow = false;
            }else {
                System.out.println("Please enter r for row or c for column");
            }
        }
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
    public int searchNumbers(int i){
        for (int j=0;j<shiplength.size();++j){
            if (i==shiplength.get(j)){
                return i;
            }
        }
        return -1;
    }

    public boolean lengthCheck(int len){
        for (int i : shiplength){
            if(len==i){
                return true;
            }
        }
        return false;
    }
    //print remaining ship lengths
    public void printShipLengths(){
        for (int i:shiplength){
            System.out.print(i+", ");
        }
    }
}
