package de.buw.se4de;

import java.util.Scanner;

public class GameEngine {
    grid EnemyGrid = new grid();
    grid PlayerGrid = new grid();

    //A standard round battleships(not done, rounds and winning/losing condition has to be implemented)
    public void standardGame(){
        Scanner scanner = new Scanner(System.in);
        //Begin input
        PlayerGrid.PrintPlayerGrid();
        while (PlayerGrid.shiplength.size()!=0){
            PlayerGrid.PlacingPlayerShip();
            PlayerGrid.PrintPlayerGrid();
        }

        EnemyGrid.PlacingEnemyShip();
        EnemyGrid.PrintEnemyGrid();

        //play till one player has no ship left
        while (PlayerGrid.shiplength.size()!=0||EnemyGrid.shiplength.size()!=0){
            EnemyGrid.PrintEnemyGrid();
            System.out.println("Where do you wish to shoot? Write a letter, press enter and then write a number ");
            String row = scanner.nextLine();
            int column = scanner.nextInt();
            EnemyGrid.shoot(row,column);
        }
    }
}
