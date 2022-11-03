package de.buw.se4de;

import com.google.common.collect.Table;

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


}
