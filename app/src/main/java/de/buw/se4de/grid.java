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
        while (shiplength.size()!=0){
            PlacingPlayerShip();
            PrintEnemyGrid();
        }
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
    //let the enemy player place one ship
    public void PlacingPlayerShip(){
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
        boolean ShipInRow=false;
        boolean ShipInCol=false;
        String s="";   //string for row/column and which row input
        int c =-1; //integer for which column input
        while (NotValidShipPos){
            System.out.println("Where do you want to place your ship? Row (r) / Column (c)");
            s = scanner.nextLine();
            //double print here???
            if (s.equals("r")||s.equals("R")){
                NotValidShipPos = false;
                ShipInRow = true;

                boolean NotValidRow = true;
                while(NotValidRow) {
                    System.out.println("In which row do you want to place the Ship?");
                    s = scanner.nextLine();
                    if (searchLetters(s) == -1) {
                        System.out.println("Please enter valid row.");
                    }else {
                        NotValidRow = false;
                    }
                }
            }else if (s.equals("c") ||  s.equals("C")){
                NotValidShipPos =false;
                ShipInCol = true;

                boolean NotValidColumn = true;
                while (NotValidColumn) {
                    System.out.println("In which columns do you want to place the ship?");
                    c = scanner.nextInt();
                    if (0 <= c && c <= 9) {
                        NotValidColumn = false;
                    } else {
                        System.out.println("Please enter valid column.");
                    }
                }
            }else {
                System.out.println("Please enter r for row or c for column");
            }
        }

        //now  placing the ship
        NotValidShipPos = true;
        int StartInt;
        int EndInt;
        String StartString;
        String EndString;

        while(NotValidShipPos){
            if (ShipInRow){
                //Input Numbers
                System.out.println("Please enter a start and end number.");
                StartInt = scanner.nextInt();
                EndInt = scanner.nextInt();
                if(checkValidPlacingNumber(StartInt,EndInt,len)){
                    NotValidShipPos = false;
                    changingCellsRow(StartInt,EndInt,s);
                }
            }else if (ShipInCol){
                System.out.println("Please Enter start and end Row");
                StartString = scanner.nextLine();
                EndString = scanner.nextLine();
                int StartStringInt = searchLetters(StartString);
                int EndStringInt = searchLetters(EndString);
                if (checkValidPlacingNumber(StartStringInt,EndStringInt,len)){
                    NotValidShipPos = false;
                    changingCellsCol(StartString,EndString,c);
                }
            }
        }
    }

    public void changingCellsRow(int start, int end,String row){
        int rowint = searchLetters(row);
        //changing nearShip of nearby cells
        // top - left
        if (row.equals("A") && start == 0) {
            cells[rowint][end+1].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
            }
        }//only Top
        else if (row.equals("A")) {
            cells[rowint][start-1].NearShip = true;
            cells[rowint][end+1].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
            }
        }//Top - right
        else if (row.equals("A") && end == 9) {
            cells[rowint][start-1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
            }
        }//Bottom - left
        else if (row.equals("J") && start == 0 ) {
            cells[rowint][end+1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
            }
        }//Bottom
        else if (row.equals("J")) {
            cells[rowint][start-1].NearShip = true;
            cells[rowint][end+1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
            }
        }//Bottom - right
        else if(row.equals("J")&& end == 9){
                cells[rowint][start-1].NearShip = true;
                for (int i = start; i<=end; i++) {
                    cells[rowint][i].hasShip = true;
                    cells[rowint][i].NearShip = true;
                    cells[rowint - 1][i].NearShip = true;
                }
        }//everything else
        else {
            cells[rowint][start-1].NearShip = true;
            cells[rowint][end+1].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
            }
        }
    }
    public  void changingCellsCol(String startString, String endString, int col){
        //changing nearShip of nearby cells

        int start = searchLetters(startString);
        int end = searchLetters(endString);

        // left - top
        if (startString.equals("A") && col == 0) {
            cells[start][col+1].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col].NearShip = true;
            }
        }//Top - right
        else if (startString.equals("A") && end == 9) {
            cells[start-1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//only Top
        else if (startString.equals("A")) {
            cells[start-1][col].NearShip = true;
            cells[end+1][col].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//Bottom - left
        else if (endString.equals("J") && start == 0 ) {
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        }//Bottom - right
        else if(endString.equals("J")&& end == 9){
            cells[start-1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        }//Bottom
        else if (endString.equals("J")) {
            cells[start-1][col].NearShip = true;
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        }
        //everything else
        else {
            cells[start-1][col].NearShip = true;
            cells[end+1][col].NearShip = true;

            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }
    }
    public boolean checkValidPlacingNumber(int start, int end, int len){
        int diff = end - start;
        if (start<0||start>9 || end <0 || end >9){
            System.out.println("Please enter Numbers between 0 and 9 or Letters between A and J ");
            return false;
        }else if(diff!=len){
            return true;
        }else {
            System.out.println("Please enter Input in sync with your chosen ship length or Start must be smaller then End");
            return false;
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
