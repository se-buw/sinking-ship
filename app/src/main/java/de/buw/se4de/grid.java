package de.buw.se4de;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class grid {
    int rows;
    int columns;
    Ship[] ships;
    cell[][] cells;
    ArrayList<cell> alreadyShot = new ArrayList<cell>();
    Scanner scanner = new Scanner(System.in);
    Character[] letters ={'A','B','C','D','E','F','G','H','I','J'};
    ArrayList<Integer> shiplength = new ArrayList<>(Arrays.asList(5,4,4,3,3,2));
    int shipAmount = shiplength.size();
    int aliveCells;


    //Constructor of the grid the game is played on, with its ships and cells
    public grid(){
        this.rows = 10;
        this.columns = 10;
        this.ships= new Ship[6];
        this.cells= new cell[rows][columns];

        for(int i=0;i<rows;i++){
            for (int j = 0; j< columns; j++){
                cells[i][j]= new cell();
                cells[i][j].x = i;
                cells[i][j].y = j;
            }
        }

        for (int i =0;i< ships.length;i++){
            ships[i]=new Ship();
            ships[i].len = shiplength.get(i);
        }
        this.aliveCells = 0;
        for (int i: shiplength){
            this.aliveCells+=i;
        }

    }

    //Methods of player shooting, and AI shooting
    //---------------------------------------------------------------------------------
    //Simple shoot function
    public void shoot(String row, int column){
        int rowInt = searchLetters(row);
        //checking if cell has ship and was not shot yet
        if (cells[rowInt][column].hasShip&&!cells[rowInt][column].dead){
            cells[rowInt][column].shotShip=true;
            aliveCells--;
        }
        cells[rowInt][column].dead=true;
    }
    //just shoots on a random cell
    public int[] randomShoot(){
        Random rand = new Random();
        boolean NotValidShot= true;

        //get random cords
        int randRow=-1;
        int randCol=-1;

        while (NotValidShot) {
            randRow = rand.nextInt(rows);
            randCol = rand.nextInt(columns);
            //check if cords already got shot
            if (!cells[randRow][randCol].dead){
                //check if cell has a ship
                if (cells[randRow][randCol].hasShip){
                    cells[randRow][randCol].shotShip = true;
                    --aliveCells;
                }
                //marking cell as shot
                cells[randRow][randCol].dead = true;
                NotValidShot = false;
            }

        }
        return new int[]{randRow,randCol};
    }

    //AI Hunt and Target Method
    public int[] hunt_target_shoot(int[] field) {
        Random rand = new Random();
        boolean no_valid_target = true;
        int x = -1;
        int y = -1;
        int i;
        for (i = 0; i < 100; i++) {
            if (field[i] == 2) {
                int k = i;
                if (!(i % 10 == 0) && field[i - 1] == 0) {
                    x = (i - 1) / 10;
                    y = (i - 1) % 10;
                    if (cells[x][y].hasShip) {
                        cells[x][y].shotShip = true;
                        --aliveCells;
                        assign_values1(field, i, k);
                    } else {
                        field[i - 1] = 1;
                    }
                    cells[x][y].dead = true;
                    return new int[]{x, y};
                } else if (!(i % 10 == 9) && (field[i + 1] == 0)) {
                    x = (i + 1) / 10;
                    y = (i + 1) % 10;
                    System.out.print(y);
                    if (!cells[x][y].hasShip) {
                        cells[x][y].shotShip = true;
                        --aliveCells;
                        assign_values2(field, i, k);
                    } else {
                        field[i + 1] = 1;
                    }
                    cells[x][y].dead = true;
                    return new int[]{x, y};
                } else if (!(i / 10 == 9) && (field[i + 10] == 0)) {
                    x = (i + 10) / 10;
                    y = (i + 10) % 10;
                    if (!cells[x][y].hasShip) {
                        cells[x][y].shotShip = true;
                        --aliveCells;
                        assign_values3(field, i, k);
                    } else {
                        field[i + 10] = 1;
                    }
                    cells[x][y].dead = true;
                    return new int[]{x, y};
                } else if (!(i / 10 == 0) && (field[i - 10] == 0)) {
                    x = (i - 10) / 10;
                    y = (i - 10) % 10;
                    if (!cells[x][y].hasShip) {
                        cells[x][y].shotShip = true;
                        --aliveCells;
                        assign_values4(field, i, k);
                    } else {
                        field[i - 10] = 1;
                    }
                    return new int[]{x, y};
                }
            }
        }
        while (no_valid_target) {
                i = rand.nextInt(100);
                if (field[i] == 0) {
                    x = (i - 1) / 10;
                    y = (i - 1) % 10;
                    if (!cells[x][y].hasShip) {
                        cells[x][y].shotShip = true;
                        --aliveCells;
                        field[i] = 2;
                    } else {
                        field[i] = 1;
                    }
                    no_valid_target = false;
                }
        }
        cells[x][y].dead = true;
        return new int[]{x, y};
    }

    public void assign_values1(int[] field, int i, int k) {
        while (!(k == 99) && !(field[k] == 0) && !(field[k] == 1)) {
            k++;
        }
        if (((i - 1 == 0) || (!((i - 1) % 10 == 0) && !(cells[(i - 1) / 10][((i - 1) % 10) - 1].hasShip)))
                && ((k == 99) || (!(k % 10 == 9) && !(cells[(k + 1) / 10][(k + 1) % 10].hasShip)))) {
            field[i - 1] = 3;
            while (!(k - i < 0)) {
                field[k - 1] = 3;
                k--;
            }
        } else {
            field[i - 1] = 2;
        }
    }
    public void assign_values2(int[] field, int i, int k) {
        while (!(k == 0) && !(field[k] == 0) && !(field[k] == 1)) {
            k--;
        }
        if (((i + 1 == 99) || (!((i + 1) % 10 == 9) && !(cells[(i - 1) / 10][((i - 1) % 10) + 1].hasShip)))
                && ((k == 0) || (!(k % 10 == 0) && !(cells[(k - 1) / 10][(k - 1) % 10].hasShip)))) {
            field[i + 1] = 3;
            while (!(i - k < 0)) {
                field[k - 1] = 3; // check if that is correct
                k++;
            }
        } else {
            field[i + 1] = 2;
        }
    }

    public void assign_values3(int[] field, int i, int k) {
        while (!(k/10 == 0) && !(field[k] == 0) && !(field[k] == 1)) {
            k = k-10;
        }
        if ((((i + 10)/10 == 9) || (!((i + 10) % 10 == 9) && !(cells[((i + 10) / 10) + 1][(i + 10) % 10].hasShip)))
                && (((k/10) == 0) || (!(k / 10 == 0) && !(cells[(k - 10) / 10][(k - 10) % 10].hasShip)))) {
            field[i + 10] = 3;
            while (!(i == k)) {
                field[k] = 3; // check if that is correct
                k = k + 10;
            }
        } else {
            field[i + 10] = 2;
        }
    }

    public void assign_values4(int[] field, int i, int k) {
        while (!(k/10 == 9) && !(field[k] == 0) && !(field[k] == 1)) {
            k = k + 10;
        }
        if ((((i - 10) % 10 == 0) || (!((i - 10) % 10 == 0) && !(cells[((i - 10) / 10) + 1][(i - 10) % 10].hasShip)))
                && ((k == 99) || (!(k / 10 == 9) && !(cells[(k - 10) / 10][(k - 10) % 10].hasShip)))) {
            field[i - 10] = 3;
            while (!(i == k)) {
                field[k] = 3;
                k = k - 10;
            }
        } else {
            field[i - 10] = 2;
        }
    }


    //---------------------------------------------------------------------------------
    //Methods of visualizing and placing player and enemy ships
    //---------------------------------------------------------------------------------
    //Prints player grid with ships seen
    public void PrintPlayerGrid(){
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9  ");
        for(int current_row=0; current_row < this.rows;current_row++) {
            System.out.print(letters[current_row]+"|");
            for (int current_col=0; current_col<this.cells[current_row].length;current_col++){
                cell current_cell = cells[current_row][current_col];
                //if cell has a shot ship
                if (checkShip(current_cell)){
                    System.out.print("Z ");
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
    //Prints the enemy grid without their ships seen
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
    //Lets a player place all ships, all with decidable length
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
            System.out.println("Do you want your ship horizontally or vertically? horizontal (h) / vertical (v)");
            s = scanner.nextLine(); //clearing keyboard input buffer
            s = scanner.nextLine();
            if (s.equals("h")||s.equals("H")){

                boolean NotValidRow = true;
                while(NotValidRow) {
                    System.out.println("In which row do you want to place the Ship? Write a letter");
                    s = scanner.nextLine();
                    if (searchLetters(s) == -1) {
                        System.out.println("Please enter valid row.");
                    }else {
                        //input is valid row, now place ship in row
                        NotValidRow = false;
                        System.out.println("Which column do you want your ship to start. Write a number");
                        StartInt = scanner.nextInt();
                        EndInt = StartInt+len-1;
                        if(checkValidPlacingNumber(StartInt,len) &&
                                checkValidPlacement(searchLetters(s),StartInt,searchLetters(s),EndInt)){
                            changingCellsRow(StartInt,len,s);
                            NotValidShipPos=false;

                            //initialising the ship
                            Ship ship = findShip(len);
                            ship.BowColStart = StartInt;
                            ship.BowRowStart = searchLetters(s);
                            ship.isVert = false;

                        }else{System.out.println("Not viable input.");}
                    }
                }
            }else if (s.equals("v") ||  s.equals("V")){

                boolean NotValidColumn = true;
                while (NotValidColumn) {
                    System.out.println("In which column do you want to place the ship? Write a number.");
                    c = scanner.nextInt();
                    if (0 <= c && c <= 9) {
                        //input is valid column now placing ship
                        NotValidColumn = false;
                        System.out.println("Which row do you want your ship to start. Write a letter.");
                        StartString = scanner.nextLine();//clearing keyboard input
                        StartString = scanner.nextLine();

                        int StartStringInt = searchLetters(StartString);
                        int EndStringInt = StartStringInt+len-1;
                        if (checkValidPlacingNumber(StartStringInt, len) &&
                                checkValidPlacement(searchLetters(StartString),c,EndStringInt,c)){
                            changingCellsCol(StartString,len,c);
                            NotValidShipPos =false;

                            //initialising the ship
                            Ship ship = findShip(len);
                            ship.BowColStart = c;
                            ship.BowRowStart = searchLetters(StartString);
                            ship.isVert = true;

                        } else {System.out.println("Not viable input.");}
                    } else {System.out.println("Please enter valid column.");}
                }
            }else {System.out.println("Please enter h for horizontal or v for vertical.");}
        }
    }
    //Places enemy ships randomly, according to the rules
    public void PlacingEnemyShip(){
        for(Integer len : shiplength){
            int Vert = ThreadLocalRandom.current().nextInt(0, 2); //output 0 or 1 to know if the ship is vertical or horizontal
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

                    Ship ship = findShip(len);
                    ship.BowColStart = randCol;
                    ship.BowRowStart = searchLetters(randRowString);
                    ship.isVert = true;
                }

                else if(checkValidPlacement(randRow,randCol,randRow,endCol) && Vert == 1){
                    String randRowString = letters[randRow].toString();
                    changingCellsRow(randCol, len, randRowString);
                    NotValidPlacement = false;

                    Ship ship = findShip(len);
                    ship.BowColStart = randCol;
                    ship.BowRowStart = searchLetters(randRowString);
                    ship.isVert = false;
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

    //---------------------------------------------------------------------------------
    //Helper functions
    //---------------------------------------------------------------------------------

    //A function to change the "nearShip" status of cells near ships, in a row
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
        }//Left
        else if (start==0){
            cells[rowint][end+1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
            }
        }//right
        else if (end == columns-1){
            cells[rowint][start-1].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[rowint][i].hasShip = true;
                cells[rowint][i].NearShip = true;
                cells[rowint - 1][i].NearShip = true;
                cells[rowint + 1][i].NearShip = true;
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
    //A function to change the "nearShip" status of cells near ships, in a column
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
        else if (col == columns-1) {
            cells[start-1][col].NearShip = true;
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
            }
        }//checking if top
        else if(startString.equals("A")){
            cells[end+1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//checking if bottom
        else if (endString.equals("J")){
            cells[start-1][col].NearShip = true;
            for (int i = start; i<=end; i++) {
                cells[i][col].hasShip = true;
                cells[i][col].NearShip = true;
                cells[i][col-1].NearShip = true;
                cells[i][col+1].NearShip = true;
            }
        }//everything else
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
    //Checks if the cells between two points have the "nearShip" status, and returns false if yes
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
    //Checks if input number would exceed the map barriers with given ship length
    public boolean checkValidPlacingNumber(int start,int len){
        return start >= 0 && start < rows - len + 1;
    }
    //Checks if cell was shot and has a ship, so it can display the ship
    public boolean checkShip(cell cell){
        return cell.dead && cell.hasShip;
    }
    //A simple search function to change the letters of the map into their according numbers
    public int searchLetters(String s){
        char c = s.charAt(0);
        for (int i=0;i< letters.length;++i) {
            if (c==letters[i]){
                return i;
            }
        }
        return -1;
    }
    //Search function to find correct ship in the grids "ships" array
    public Ship findShip(int len){
        Ship def = new Ship();
        for (Ship s: ships){
            //checking if ship has correct len and ist still default
            if (s.len==len && s.BowColStart ==-1){
                return s;
            }
        }
        //returning default ship else - should not happen;
        return def;
    }
    //A ship length checker
    public boolean lengthCheck(int len){
        for (int i : shiplength){
            if(len==i){
                return true;
            }
        }
        return false;
    }
    //Prints remaining ship lengths
    public void printShipLengths(){
        for (int i=0;i<shiplength.size()-1;i++){
            System.out.print(shiplength.get(i) +", ");
        }
        System.out.println(shiplength.get(shiplength.size()-1));
    }

    //---------------------------------------------------------------------------------
    /*
    The reasoning to putting a big amount of our code in the grid class, is because it is the central point of the game
    and makes it way easier for us to code it in here, than to overcomplicate every other class with a grid parameter.
     */
}
