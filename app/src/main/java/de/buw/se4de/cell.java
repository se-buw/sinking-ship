package de.buw.se4de;

public class cell{
    int x;
    int y;
    boolean dead;
    boolean hasShip;
    boolean shotShip;
    boolean NearShip;
    boolean sunkShip;
    //Cell class that the grid is made up of
    public cell(){
        this.dead = false;
        this.hasShip = false;
        this.shotShip = false;
        this.NearShip = false;
        this.sunkShip = false;
    }
}
