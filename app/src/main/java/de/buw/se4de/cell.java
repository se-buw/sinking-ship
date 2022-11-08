package de.buw.se4de;

public class cell{
    boolean dead;
    boolean hasShip;
    boolean shotShip;
    boolean NearShip;
    //Cell class that the grid is made up of
    public cell(){
        this.dead = false;
        this.hasShip = false;
        this.shotShip = false;
        this.NearShip = false;
    }
}
