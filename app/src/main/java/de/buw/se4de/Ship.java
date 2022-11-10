package de.buw.se4de;

public class Ship {
    int len;
    int BowColStart;
    int BowRowStart;
    int hitAmount;
    boolean isSunk;
    boolean isVert;

    public Ship(){
        this.len=0;
        this.BowColStart=-1;
        this.BowRowStart=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }

}

