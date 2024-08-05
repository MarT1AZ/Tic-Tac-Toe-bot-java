import java.util.Random;
import java.util.ArrayList;

public class xobot {
    

    private int[][] boardState;
    private int initialTurn;
    private Random randomizer;


    public xobot(int t){
        this.initialTurn = t;
        this.randomizer = new Random();
    }



    public void observeBoard(int[][] board){
        this.boardState = board;
    }


    public int calculateNextMove(){
        // empty cell -> 0
        // player cell -> 1
        // bot cell -> -1
        int weight = 0;
        int[][] weightArray = new int[3][3];
        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){

                if(boardState[y][x] != 0){
                    continue;
                }

                //calculate vertical cells
                for(int i = 0;i <= 2;i++){
                    weight += boardState[i][x];
                }
                weightArray[y][x] = weight;
                weight = 0;

                //calculate horizontal cells
                for(int i = 0;i <= 2;i++){
                    weight += boardState[y][i];
                }
                weightArray[y][x] = weight > weightArray[y][x] ? weight : weightArray[y][x];
                weight = 0;

                //calculate diagonal cells if possible

                if((x == 0 && y == 0) || (x == 2 && y == 2)){
                    weight += boardState[0][0];
                    weight += boardState[1][1];
                    weight += boardState[2][2];
                    weightArray[y][x] = weight > weightArray[y][x] ? weight : weightArray[y][x];
                }

                if((x == 0 && y == 2) || (x == 2 && y == 0)){
                    weight += boardState[0][2];
                    weight += boardState[1][1];
                    weight += boardState[2][0];
                    weightArray[y][x] = weight > weightArray[y][x] ? weight : weightArray[y][x];
                }
                

                if(x == 1 && y == 1){
                    weight += boardState[0][0];
                    weight += boardState[1][1];
                    weight += boardState[2][2];
                    weightArray[y][x] = weight > weightArray[y][x] ? weight : weightArray[y][x];
                    weight = 0;
                    weight += boardState[2][0];
                    weight += boardState[1][1];
                    weight += boardState[0][2];
                    weightArray[y][x] = weight > weightArray[y][x] ? weight : weightArray[y][x];
                }
                weight = 0;


            }
        }

        int max = -2;

        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){
                if(boardState[y][x] != 0){
                    continue;
                }
                if(max < weightArray[y][x]){
                    max = weightArray[y][x];
                }
            }
        }

        ArrayList<Integer> potentialNextMoves = new ArrayList<Integer>();

        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){
                if(boardState[y][x] != 0){
                    continue;
                }
                if(max == weightArray[y][x]){
                    potentialNextMoves.add(3 * y + x);
                }
            }
        }

        // for(int y = 0; y <= 2;y++){
        //     for(int x = 0; x <= 2;x++){
        //         if(boardState[y][x] == 1){
        //             System.out.print("o ");
        //         }else if(boardState[y][x] == -1){
        //             System.out.print("x ");
        //         }else{
        //             System.out.print(weightArray[y][x] + " ");
        //         }
        //     }
        //     System.out.print("\n\n");
        // }
        // for(int i : potentialNextMoves){
        //     System.out.print(i + 1);
        // }

        // System.out.print("\n\n");

        if(potentialNextMoves.size() == 1){
            return potentialNextMoves.get(0);
        }else if(potentialNextMoves.size() == 0){
            return -1;
        }
        return potentialNextMoves.get(randomizer.nextInt(potentialNextMoves.size()));
    }





}
