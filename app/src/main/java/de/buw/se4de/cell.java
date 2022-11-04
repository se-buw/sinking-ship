package de.buw.se4de;

public class cell{
    boolean dead;
    boolean hasShip;
    boolean shotShip;
    boolean NearShip;

    public cell(){
        this.dead = false;
        this.hasShip = false;
        this.shotShip = false;
        this.NearShip = false;
    }
}
