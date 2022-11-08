package de.buw.se4de;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class grid {
    int rows;
    int columns;
    Ship[] ships;
    cell[][] cells;
    Scanner scanner = new Scanner(System.in);
    Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};
    ArrayList<Integer> shiplength= new ArrayList<>(Arrays.asList(5,4,4,3,3,2));


    public grid(){
        this.rows = 10;
        this.columns = 10;
        this.ships= new Ship[6];
        this.cells= new cell[rows][columns];

        for(int i=0;i<rows;i++){
            for (int j = 0; j< columns; j++){
                cells[i][j]= new cell();
            }
        }

        for (int i =0;i< ships.length;i++){
            ships[i]=new Ship();
        }
    }
/* TODO:
*  Implement AI placement for ships
*  Implement AI to play against
* */
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

    public void PrintPLayerGrid(){
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters[current_row]+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                cell current_cell = cells[current_row][current_col];
                //if cell has a shot ship
                if (checkShip(current_cell)){
                    System.out.print("0 ");
                }//if cell has a ship that was not shot
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
    //difference to player grid is only that ships are hidden till shot
    public void PrintEnemyGrid() {
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters[current_row]+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                cell current_cell = cells[current_row][current_col];
                //if cell has a shot ship
                if (checkShip(current_cell)){
                    System.out.print("0 ");
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
        //checking which ship to place
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
                //removes index so first need to find index of len
                shiplength.remove(Integer.valueOf(len));
            }
        }
        //where to place ship: row or column
        //declaring some var for later use
        boolean NotValidShipPos = true;
        int StartInt;
        int EndInt;
        String StartString;

        String s;   //string for row/column and which row input
        int c; //integer for which column input
        while (NotValidShipPos){
            System.out.println("Where do you want to place your ship? Row (r) / Column (c)");
            s = scanner.nextLine(); //clearing keyboard input buffer
            s = scanner.nextLine();
            if (s.equals("r")||s.equals("R")){

                boolean NotValidRow = true;
                while(NotValidRow) {
                    System.out.println("In which row do you want to place the Ship? Letter");
                    s = scanner.nextLine();
                    if (searchLetters(s) == -1) {
                        System.out.println("Please enter valid row.");
                    }else {
                        //input is valid row, now place ship in row
                        NotValidRow = false;
                        System.out.println("Please enter a start number.");
                        StartInt = scanner.nextInt();
                        EndInt = StartInt+len-1;
                        if(checkValidPlacingNumber(StartInt,len) &&
                                checkValidPlacement(searchLetters(s),StartInt,searchLetters(s),EndInt)){
                            changingCellsRow(StartInt,len,s);
                            NotValidShipPos=false;
                        }else{
                            System.out.println("Invalid Number Input, please try again.");
                        }
                    }
                }
            }else if (s.equals("c") ||  s.equals("C")){

                boolean NotValidColumn = true;
                while (NotValidColumn) {
                    System.out.println("In which column do you want to place the ship? Number");
                    c = scanner.nextInt();
                    if (0 <= c && c <= 9) {
                        //input is valid column now placing ship
                        NotValidColumn = false;
                        System.out.println("Please Enter Row");
                        StartString = scanner.nextLine();//clearing keyboard input
                        StartString = scanner.nextLine();

                        int StartStringInt = searchLetters(StartString);
                        int EndStringInt = StartStringInt+len-1;
                        if (checkValidPlacingNumber(StartStringInt, len) &&
                                checkValidPlacement(searchLetters(StartString),c,EndStringInt,c)){
                            changingCellsCol(StartString,len,c);
                            NotValidShipPos =false;
                        }else {System.out.println("Invalid Input, please try again.");}
                    } else {System.out.println("Please enter valid column.");}
                }
            }else {System.out.println("Please enter r for row or c for column");}
        }
    }

    public void PlacingEnemyShip(){
        for(Integer len : shiplength){
            int Vert = ThreadLocalRandom.current().nextInt(0, 2); //output 0 or 1 to know which direction the ship is facinf
            int randCol = ThreadLocalRandom.current().nextInt(0, columns-len+1);
            int endCol = randCol+len-1;
            int randRow = ThreadLocalRandom.current().nextInt(0, rows-len+1);
            int endRow = randRow+len-1;

            boolean NotValidPlacement = true;
            while(NotValidPlacement){
                if (checkValidPlacement(randRow,randCol,endRow,randCol) && Vert == 0){
                    String randRowString = letters[randRow].toString();
                    changingCellsCol(randRowString,len,randCol);
                    NotValidPlacement = false;
                }

                else if(checkValidPlacement(randRow,randCol,randRow,endCol) && Vert == 1){
                    String randRowString = letters[randRow].toString();
                    changingCellsRow(randCol, len, randRowString);
                    NotValidPlacement = false;
                }

                else{
                    Vert = ThreadLocalRandom.current().nextInt(0, 2);
                    randCol = ThreadLocalRandom.current().nextInt(0, columns-len+1);
                    endCol = randCol+len-1;
                    randRow = ThreadLocalRandom.current().nextInt(0, rows-len+1);
                    endRow = randRow+len-1;
                }
            }
        }
    }


    public void changingCellsRow(int start,int len, String row){
        int end = start + len - 1;
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
        }//Top - right
        else if (row.equals("A") && end == rows-1) {
            cells[rowint][start-1].NearShip = true;
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
        }//Bottom - left
        else if (row.equals("J") && start == 0 ) {
            cells[rowint][end+1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
            }
        }//Bottom - right
        else if(row.equals("J")&& end == rows-1){
            cells[rowint][start-1].NearShip = true;
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
    public void changingCellsCol(String startString, int len, int col){
        //changing nearShip of nearby cells

        int start = searchLetters(startString);
        int end = start+len-1;
        String endString = letters[end].toString();
        // left - top
        if (col == 0 && startString.equals("A")) {
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//left - bottom
        else if (col == 0 && endString.equals("J")) {
            cells[start-1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//only left
        else if (col==0) {
            cells[start-1][col].NearShip = true;
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//right - top
        else if (col == columns -1 && startString.equals("A")) {
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        } //right - bottom
        else if(col == columns-1 && endString.equals("J")){
            cells[start-1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        }//Bottom
        else if (col == columns -1) {
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
    public boolean checkValidPlacement(int startrow, int startcol, int endrow,int endcol){
        for (int i = startrow;i <= endrow;i++){
            for (int j=startcol;j<=endcol;j++){
                if (cells[i][j].NearShip){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean checkValidPlacingNumber(int start,int len){
        return start >= 0 && start < rows - len - 1;
    }

    public int searchLetters(String s){
        char c = s.charAt(0);
        for (int i=0;i< letters.length;++i) {
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
        for (int i=0;i<shiplength.size()-1;i++){
            System.out.print(shiplength.get(i) +", ");
        }
        System.out.println(shiplength.get(shiplength.size()-1));
    }
}
