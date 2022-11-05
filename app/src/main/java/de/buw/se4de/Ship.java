package de.buw.se4de;

public class Ship {
    int len;
    int BowCol;
    int BowRow;
    int hitAmount;
    boolean isSunk;
    boolean isVert;

    public Ship(){
        this.len=0;
        this.BowCol=0;
        this.BowRow=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }

}

class Ship2 extends Ship{

    public Ship2(){
        this.len=2;
        this.BowCol=0;
        this.BowRow=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }
}

class Ship3 extends Ship{

    public Ship3(){
        this.len=3;
        this.BowCol=0;
        this.BowRow=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }
}

class Ship4 extends Ship{

    public Ship4(){
        this.len=4;
        this.BowCol=0;
        this.BowRow=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }
}

class Ship5 extends Ship{

    public Ship5(){
        this.len=5;
        this.BowCol=0;
        this.BowRow=0;
        this.hitAmount=0;
        this.isSunk=false;
        this.isVert=false;
    }
}
