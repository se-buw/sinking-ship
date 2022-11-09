package de.buw.se4de;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Character;

public class GameEngine {
    grid EnemyGrid = new grid();
    grid PlayerGrid = new grid();
    String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};

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

        //play till one player has no ship left
        while (PlayerGrid.aliveCells!=0||EnemyGrid.aliveCells!=0){
            int shots = 3;
            while(shots != 0) {
                EnemyGrid.PrintEnemyGrid();

                String first = "";
                int helpInt = 0;
                String second = "";


                System.out.println("Where do you wish to shoot? Write a letter and a number.");

                first = scanner.nextLine();
                second = scanner.nextLine();

                if(searchNumbers(first)){
                    if(searchNumbers(second)){System.out.println("Both cant be numbers");break;}
                }

                else if(searchNumbers(first)){
                    if(second.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(first);
                        EnemyGrid.shoot(second,helpInt);
                        --shots;
                    }
                }

                else if(searchNumbers(second)){
                    if(first.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(second);
                        EnemyGrid.shoot(first,helpInt);
                        --shots;
                    }
                }

                else if(first.matches("[ABCDEFGHIJ]")){
                    if(second.matches("[ABCDEFGHIJ]")){System.out.println("Both cant be letters");break;}
                }

                else{System.out.println("Invalid input.");}
            }
        }
        if(PlayerGrid.aliveCells!=0){System.out.println("Cool, you have won :3");}
        if(EnemyGrid.aliveCells!=0){System.out.println("Oh no, you havent not wontnt :(");}
    }

    public boolean searchNumbers(String s){
        for (int i=0;i< numbers.length;++i) {
            if (s.equals(numbers[i])){
                return true;
            }
        }
        return false;
    }
}
