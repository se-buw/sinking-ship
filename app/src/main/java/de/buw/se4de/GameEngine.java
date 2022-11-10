package de.buw.se4de;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.lang.Character;

public class GameEngine {
    grid EnemyGrid = new grid();
    grid PlayerGrid = new grid();
    String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};

    //A standard round battleships with a more simple ai
    public void standardGame(){
        Scanner scanner = new Scanner(System.in);
        //Begin input
        while (PlayerGrid.shiplength.size()!=0){
            PlayerGrid.PrintPlayerGrid();
            PlayerGrid.PlacingPlayerShip();
        }

        EnemyGrid.PlacingEnemyShip();

        //play till one player has no ship left
        while (PlayerGrid.aliveCells>0 && EnemyGrid.aliveCells>0){

            boolean playerLostShot = false;
            boolean compLostShot = false;
            PlayerGrid.PrintPlayerGrid();
            EnemyGrid.PrintEnemyGrid();

            while(!playerLostShot) {

                String first;
                int helpInt;
                int helpIntHit;
                String second;


                System.out.println("Where do you wish to shoot? Write a letter and a number.");

                first = scanner.nextLine();
                second = scanner.nextLine();

                if(searchNumbers(first)){
                    if(searchNumbers(second)){System.out.println("Both cant be numbers");}
                }

                else if(searchNumbers(first)){
                    if(second.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(first);
                        helpIntHit = PlayerGrid.searchLetters(second);
                        EnemyGrid.shoot(second,helpInt);
                        //if the cell has no ship Player loses turn
                        if(!EnemyGrid.cells[helpIntHit][helpInt].hasShip){
                            playerLostShot = true;
                        }else if (EnemyGrid.aliveCells==0){
                            break;
                        }else{
                            EnemyGrid.PrintEnemyGrid();
                        }
                    }
                }

                else if(searchNumbers(second)){
                    if(first.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(second);
                        helpIntHit = EnemyGrid.searchLetters(first);
                        EnemyGrid.shoot(first,helpInt);
                        //if the cell has no ship Player loses turn
                        if(!EnemyGrid.cells[helpIntHit][helpInt].hasShip){
                            playerLostShot = true;
                        }else if (EnemyGrid.aliveCells==0){
                            break;
                        }else {
                            EnemyGrid.PrintEnemyGrid();
                        }
                    }
                }

                else if(first.matches("[ABCDEFGHIJ]")){
                    if(second.matches("[ABCDEFGHIJ]")){System.out.println("Both cant be letters");}
                }

                else{System.out.println("Invalid input.");}

            }
            //check if the player has already won if not the Computer shoots
            if (EnemyGrid.aliveCells!=0) {
                System.out.println("You lost your turn now the enemy shoots");

                while (!compLostShot) {
                    int[] cords = PlayerGrid.randomShoot();
                    int row = cords[0];
                    Character charRow = PlayerGrid.letters[row];
                    int col = cords[1];
                    System.out.println("The Computer shot at: " + charRow + col);
                    if (!PlayerGrid.cells[cords[0]][cords[1]].hasShip) {
                        compLostShot = true;
                    }
                }
                System.out.println("The Computer lost their turn now you can shoot again.");
            }
        }
        if(PlayerGrid.aliveCells!=0){victory();}
        if(EnemyGrid.aliveCells!=0){defeat();}
    }
    //A more unfair approach to battleship
    public void unfairGame(){
        Scanner s = new Scanner(System.in);
        //let the player and computer place his ship normally
        while (PlayerGrid.shiplength.size()!=0){
            PlayerGrid.PrintPlayerGrid();
            PlayerGrid.PlacingPlayerShip();
        }
        EnemyGrid.PlacingEnemyShip();

        //play till one of the competitors has no ship left
        while (PlayerGrid.aliveCells>0&&EnemyGrid.aliveCells>0){
            boolean playerLostShot = false;
            boolean compLostShot = false;
            PlayerGrid.PrintPlayerGrid();
            EnemyGrid.PrintEnemyGrid();

            //player round is exactly the same as in standard play
            while(!playerLostShot) {

                String first;
                int helpInt;
                int helpIntHit;
                String second;


                System.out.println("Where do you wish to shoot? Write a letter and a number.");

                first = s.nextLine();
                second = s.nextLine();

                if(searchNumbers(first)){
                    if(searchNumbers(second)){System.out.println("Both cant be numbers");}
                }

                else if(searchNumbers(first)){
                    if(second.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(first);
                        helpIntHit = PlayerGrid.searchLetters(second);
                        EnemyGrid.shoot(second,helpInt);
                        //if the cell has no ship Player loses turn
                        if(!EnemyGrid.cells[helpIntHit][helpInt].hasShip){
                            playerLostShot = true;
                        }else if (EnemyGrid.aliveCells==0){
                            break;
                        }else{
                            EnemyGrid.PrintEnemyGrid();
                        }
                    }
                }

                else if(searchNumbers(second)){
                    if(first.matches("[ABCDEFGHIJ]")){
                        helpInt = Integer.parseInt(second);
                        helpIntHit = EnemyGrid.searchLetters(first);
                        EnemyGrid.shoot(first,helpInt);
                        //if the cell has no ship Player loses turn
                        if(!EnemyGrid.cells[helpIntHit][helpInt].hasShip){
                            playerLostShot = true;
                        }else if (EnemyGrid.aliveCells==0){
                            break;
                        }else {
                            EnemyGrid.PrintEnemyGrid();
                        }
                    }
                }

                else if(first.matches("[ABCDEFGHIJ]")){
                    if(second.matches("[ABCDEFGHIJ]")){System.out.println("Both cant be letters");}
                }

                else{System.out.println("Invalid input.");}

            }
            if (EnemyGrid.aliveCells>0){
                while (!compLostShot){
                    Random random = new Random();
                    int hit = random.nextInt(100);
                    if (hit <50){
                        int[] cords = PlayerGrid.randomShoot();
                        int row = cords[0];
                        Character charRow = PlayerGrid.letters[row];
                        int col = cords[1];
                        System.out.println("The Computer shot at: " + charRow + col);
                        if (PlayerGrid.aliveCells<=0){
                            break;
                        }
                        int reroll = random.nextInt(20);
                        if (reroll>=13){
                            compLostShot = true;
                        }
                    }else {
                        System.out.println("The computer is loafing around.");
                    }
                }
            }
        }
        if(PlayerGrid.aliveCells!=0){victory();}
        if(EnemyGrid.aliveCells!=0){EnemyGrid.PrintEnemyGrid();defeat();}
    }

    //A helper function to check if a string input equals any of the numbers in the "numbers" array
    public boolean searchNumbers(String s){
        for (int i=0;i< numbers.length;++i) {
            if (s.equals(numbers[i])){
                return true;
            }
        }
        return false;
    }
    //A victory screen
    public void victory(){
        System.out.println("VVVVVVVV           VVVVVVVVIIIIIIIIII      CCCCCCCCCCCCCTTTTTTTTTTTTTTTTTTTTTTT     OOOOOOOOO     RRRRRRRRRRRRRRRRR   YYYYYYY       YYYYYYY !!!");
        System.out.println("V::::::V           V::::::VI::::::::I   CCC::::::::::::CT:::::::::::::::::::::T   OO:::::::::OO   R::::::::::::::::R  Y:::::Y       Y:::::Y!!:!!");
        System.out.println("V::::::V           V::::::VI::::::::I CC:::::::::::::::CT:::::::::::::::::::::T OO:::::::::::::OO R::::::RRRRRR:::::R Y:::::Y       Y:::::Y!:::!");
        System.out.println("V::::::V           V::::::VII::::::IIC:::::CCCCCCCC::::CT:::::TT:::::::TT:::::TO:::::::OOO:::::::ORR:::::R     R:::::RY::::::Y     Y::::::Y!:::!");
        System.out.println("V:::::V           V:::::V   I::::I C:::::C       CCCCCCTTTTTT  T:::::T  TTTTTTO::::::O   O::::::O  R::::R     R:::::RYYY:::::Y   Y:::::YYY!:::!");
        System.out.println("V:::::V         V:::::V    I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R::::R     R:::::R   Y:::::Y Y:::::Y   !:::!");
        System.out.println("V:::::V       V:::::V     I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R::::RRRRRR:::::R     Y:::::Y:::::Y    !:::!");
        System.out.println("V:::::V     V:::::V      I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R:::::::::::::RR       Y:::::::::Y     !:::!");
        System.out.println("V:::::V   V:::::V       I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R::::RRRRRR:::::R       Y:::::::Y      !:::!");
        System.out.println("V:::::V V:::::V        I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R::::R     R:::::R       Y:::::Y       !:::!");
        System.out.println("V:::::V:::::V         I::::IC:::::C                      T:::::T        O:::::O     O:::::O  R::::R     R:::::R       Y:::::Y       !!:!!");
        System.out.println("V:::::::::V          I::::I C:::::C       CCCCCC        T:::::T        O::::::O   O::::::O  R::::R     R:::::R       Y:::::Y        !!! ");
        System.out.println("V:::::::V         II::::::IIC:::::CCCCCCCC::::C      TT:::::::TT      O:::::::OOO:::::::ORR:::::R     R:::::R       Y:::::Y");
        System.out.println("V:::::V          I::::::::I CC:::::::::::::::C      T:::::::::T       OO:::::::::::::OO R::::::R     R:::::R    YYYY:::::YYYY     !!!");
        System.out.println("V:::V           I::::::::I   CCC::::::::::::C      T:::::::::T         OO:::::::::OO   R::::::R     R:::::R    Y:::::::::::Y    !!:!!");
        System.out.println("VVV            IIIIIIIIII      CCCCCCCCCCCCC      TTTTTTTTTTT           OOOOOOOOO     RRRRRRRR     RRRRRRR    YYYYYYYYYYYYY     !!!");
    }
    //A defeat screen
    public void defeat(){
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("███▀▀▀██┼███▀▀▀███┼███▀█▄█▀███┼██▀▀▀");
        System.out.println("██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼█┼┼┼██┼██┼┼┼");
        System.out.println("██┼┼┼▄▄▄┼██▄▄▄▄▄██┼██┼┼┼▀┼┼┼██┼██▀▀▀");
        System.out.println("██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██┼┼┼");
        System.out.println("███▄▄▄██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██▄▄▄");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("███▀▀▀███┼▀███┼┼██▀┼██▀▀▀┼██▀▀▀▀██▄┼");
        System.out.println("██┼┼┼┼┼██┼┼┼██┼┼██┼┼██┼┼┼┼██┼┼┼┼┼██┼");
        System.out.println("██┼┼┼┼┼██┼┼┼██┼┼██┼┼██▀▀▀┼██▄▄▄▄▄▀▀┼");
        System.out.println("██┼┼┼┼┼██┼┼┼██┼┼█▀┼┼██┼┼┼┼██┼┼┼┼┼██┼");
        System.out.println("███▄▄▄███┼┼┼─▀█▀┼┼─┼██▄▄▄┼██┼┼┼┼┼██▄");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼██┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼██┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼████▄┼┼┼▄▄▄▄▄▄▄┼┼┼▄████┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼▀▀█▄█████████▄█▀▀┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼█████████████┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼██▀▀▀███▀▀▀██┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼██┼┼┼███┼┼┼██┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼█████▀▄▀█████┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼███████████┼┼┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼▄▄▄██┼┼█▀█▀█┼┼██▄▄▄┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼▀▀██┼┼┼┼┼┼┼┼┼┼┼██▀▀┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼▀▀┼┼┼┼┼┼┼┼┼┼┼▀▀┼┼┼┼┼┼┼┼┼┼┼");
        System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
    }
}
