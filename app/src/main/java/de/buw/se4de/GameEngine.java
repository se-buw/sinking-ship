package de.buw.se4de;

import java.util.Scanner;

public class GameEngine {
    grid EnemyGrid = new grid();
    grid PlayerGrid = new grid();


    public void standardGame(){
        Scanner scanner = new Scanner(System.in);
        while (PlayerGrid.shiplength.size()!=0){
            PlayerGrid.PlacingPlayerShip();
            PlayerGrid.PrintPLayerGrid();
        }
        while (PlayerGrid.shiplength.size()!=0||EnemyGrid.shiplength.size()!=0){
            EnemyGrid.PrintEnemyGrid();
            System.out.println("Where do you wish to shoot? Write a letter and then a Number ");
            String row = scanner.nextLine();
            int column = scanner.nextInt();
            EnemyGrid.shoot(row,column);
        }
    }
}
