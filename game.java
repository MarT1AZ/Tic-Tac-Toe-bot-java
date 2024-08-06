
import java.util.Scanner;

public class game{

    public static int[][] board;

    public static boolean checkIfBoardFull(int[][] board){
        for(int y = 0; y <= 2;y++){
            for(int x = 0; x <= 2;x++){
                if(board[y][x] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    public static int numberOfEmptyCell(int[][] board){

        int count = 0;
        for(int y = 0; y <= 2;y++){
            for(int x = 0; x <= 2;x++){
                if(board[y][x] == 0){
                    count++;
                }
            }
        }
        return count;

    }

    public static int checkWinner(int[][] board){ // this method is also used in xobot for finding out who is the winner

        // return value as 
        // 1 -> player wins
        // 0 -> no one has won yet
        // -1 bot wins

        int value = 0;
        int count;

        for(int y = 0;y <= 2;y++){
            count = 0;
            for(int x = 0;x <= 2;x++){
                value = board[y][x];
                if(value == 0){
                    continue;
                }
                for(int i = 0;i <= 2;i++){
                    if(board[y][i] != value){
                        break;
                    }
                    count++;
                }
                if(count == 3){
                    return value;
                }
                count = 0;
                for(int i = 0;i <= 2;i++){
                    if(board[i][x] != value){
                        break;
                    }
                    count++;
                }
                if(count == 3){
                    return value;
                }
                count = 0;

            }

        }

        value = board[1][1];
        if(value != 0 && board[0][0] == value && board[1][1] == value && board[2][2] == value){
            return value;
        }

        if(value != 0 && board[0][2] == value && board[1][1] == value && board[2][0] == value){
            return value;
        }


        return 0;
    }

    public static void drawBoard(int[][] board){

        // 0 represents empty cell
        // 1 represents player cell (o)
        // -1 represents bot cell (x)
        System.out.println("* * * * * * * * * * * * * ");
        for(int y = 0; y <= 2;y++){

            
            for(int x = 0; x <= 2;x++){ // first line
                System.err.print("* ");
                if(board[y][x] == 0){
                    System.out.print("      ");
                }else if(board[y][x] == 1){
                    System.out.print("/ --\\ ");
                }else{
                    // System.out.print("xxxxxx");
                    System.out.print("\\   / ");
                }
            }
            System.err.println("* ");
            for(int x = 0; x <= 2;x++){ // second line
                System.err.print("* ");
                if(board[y][x] == 0){
                    System.out.print("      ");
                }else if(board[y][x] == 1){
                    System.out.print("|   | ");
                }else{
                    // System.out.print("xxxxxx");
                    System.out.print("  |   ");
                }
            }
            System.err.println("* ");
            for(int x = 0; x <= 2;x++){ // third line
                System.err.print("* ");
                if(board[y][x] == 0){
                    System.out.print("      ");
                }else if(board[y][x] == 1){
                    System.out.print("\\ --/ ");
                }else{
                    // System.out.print("xxxxxx");
                    System.out.print("/   \\ ");
                }
            }
            System.err.println("* ");
            System.out.println("* * * * * * * * * * * * * ");
            
        }
    }

    
    public static void gameRun(xobot bot,int[][] board){
        Scanner inputScanner = new Scanner(System.in);
        int input;
        int botMove;
        int winner;
        while(true){

            drawBoard(board);
            System.out.println("");
            System.out.println("* * * * * * *");
            System.out.println("* 1 * 2 * 3 *");
            System.out.println("* * * * * * *");
            System.out.println("* 4 * 5 * 6 *");
            System.out.println("* * * * * * *");
            System.out.println("* 7 * 8 * 9 *");
            System.out.println("* * * * * * *\n");
            System.out.println("Please enter the desire location to place your O\n");
            System.out.print("Your input : ");
            input = inputScanner.nextInt() - 1;
            System.out.print("\n\n");

            while(!(0 <= input && input <= 8 && board[input / 3][input % 3] == 0)){
                System.out.println("Invalid position!\n");
                System.out.print("please choose again : ");
                input = inputScanner.nextInt() - 1;
                System.out.print("\n\n");
            }

            board[input / 3][input % 3] = 1;
            // player's turn ends

            //check if player wins
            winner = checkWinner(board);
            if(winner != 0){
                System.out.print("\n\nwinner is found!\n\n");
                drawBoard(board);
                break;
            }
            
            // bot's turn starts if player has not won
            bot.observeBoard(board);
            botMove = bot.advanceCalculateNextMove();
            if(botMove != -1){
                board[botMove / 3][botMove % 3] = -1;
            }

            //check if bot wins
            winner = checkWinner(board);
            if(winner != 0){
                System.out.print("\n\nwinner is found!\n\n");
                drawBoard(board);
                break;
            }

        }

        // player first then bot's turn

        

    }

    public static void main(String[] args){
        int[][] board = new int[3][3];
        xobot bot = new xobot(1);
        gameRun(bot,board);
    }

}