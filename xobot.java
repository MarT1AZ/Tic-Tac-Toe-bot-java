import java.util.Random;
import java.util.ArrayList;

public class xobot {
    

    private int[][] boardState;
    // private int initialTurn; // this.initialTurn = t; not used now
    
    private Random randomizer;


    public xobot(int t){
        // this.initialTurn = t; // not used now
        this.randomizer = new Random();
    }



    public void observeBoard(int[][] board){
        // recieve board as state
        this.boardState = board;
    }


    public int calculateNextMove(){

        // empty cell -> 0
        // player cell -> 1
        // bot cell -> -1
        int weight = 0;
        int[][] minimumWeightArray = new int[3][3];
        int[][] maximumWeightArray = new int[3][3];

        //perform weight calculation both maximum and minimum
        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){

                if(boardState[y][x] != 0){
                    continue;
                }

                //calculate vertical cells
                for(int i = 0;i <= 2;i++){
                    weight += boardState[i][x];
                }
                minimumWeightArray[y][x] = weight;
                maximumWeightArray[y][x] = weight;
                weight = 0;

                //calculate horizontal cells
                for(int i = 0;i <= 2;i++){
                    weight += boardState[y][i];
                }
                maximumWeightArray[y][x] = weight > maximumWeightArray[y][x] ? weight : maximumWeightArray[y][x];
                minimumWeightArray[y][x] = weight < minimumWeightArray[y][x] ? weight : minimumWeightArray[y][x];
                weight = 0;

                //calculate diagonal cells if possible

                if((x == 0 && y == 0) || (x == 2 && y == 2)){
                    weight += boardState[0][0];
                    weight += boardState[1][1];
                    weight += boardState[2][2];
                    maximumWeightArray[y][x] = weight > maximumWeightArray[y][x] ? weight : maximumWeightArray[y][x];
                    minimumWeightArray[y][x] = weight < minimumWeightArray[y][x] ? weight : minimumWeightArray[y][x];
                }

                if((x == 0 && y == 2) || (x == 2 && y == 0)){
                    weight += boardState[0][2];
                    weight += boardState[1][1];
                    weight += boardState[2][0];
                    maximumWeightArray[y][x] = weight > maximumWeightArray[y][x] ? weight : maximumWeightArray[y][x];
                    minimumWeightArray[y][x] = weight < minimumWeightArray[y][x] ? weight : minimumWeightArray[y][x];
                }
                

                if(x == 1 && y == 1){
                    weight += boardState[0][0];
                    weight += boardState[1][1];
                    weight += boardState[2][2];
                    maximumWeightArray[y][x] = weight > maximumWeightArray[y][x] ? weight : maximumWeightArray[y][x];
                    minimumWeightArray[y][x] = weight < minimumWeightArray[y][x] ? weight : minimumWeightArray[y][x];
                    weight = 0;
                    weight += boardState[2][0];
                    weight += boardState[1][1];
                    weight += boardState[0][2];
                    maximumWeightArray[y][x] = weight > maximumWeightArray[y][x] ? weight : maximumWeightArray[y][x];
                    minimumWeightArray[y][x] = weight < minimumWeightArray[y][x] ? weight : minimumWeightArray[y][x];
                }
                weight = 0;


            }
        }

        // print weight
        // for(int y = 0; y <= 2;y++){
        //     for(int x = 0; x <= 2;x++){
        //         if(boardState[y][x] == 1){
        //             System.out.print("o ");
        //         }else if(boardState[y][x] == -1){
        //             System.out.print("x ");
        //         }else{
        //             System.out.print(minimumWeightArray[y][x] + " ");
        //         }
        //     }
        //     System.out.print("\n\n");
        // }

        // for(int y = 0; y <= 2;y++){
        //     for(int x = 0; x <= 2;x++){
        //         if(boardState[y][x] == 1){
        //             System.out.print("o ");
        //         }else if(boardState[y][x] == -1){
        //             System.out.print("x ");
        //         }else{
        //             System.out.print(maximumWeightArray[y][x] + " ");
        //         }
        //     }
        //     System.out.print("\n\n");
        // }
        // print weight


        //using find cell with weight of -2 to find the winning move

        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){
                if(boardState[y][x] != 0){
                    continue;
                }
                if(-2 == minimumWeightArray[y][x]){
                    return 3 * y + x;
                }
            }
        }

        // if the wining move could not be found, find the next move that prevent the player from wining

        int max = -2;

        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){
                if(boardState[y][x] != 0){
                    continue;
                }
                if(max < maximumWeightArray[y][x]){
                    max = maximumWeightArray[y][x];
                }
            }
        }

        ArrayList<Integer> potentialNextMoves = new ArrayList<Integer>();

        for(int y = 0;y <= 2;y++){
            for(int x = 0;x <= 2;x++){
                if(boardState[y][x] != 0){
                    continue;
                }
                if(max == maximumWeightArray[y][x]){
                    potentialNextMoves.add(3 * y + x);
                }
            }
        }


        if(potentialNextMoves.size() == 1){ // return the only move
            return potentialNextMoves.get(0);
        }else if(potentialNextMoves.size() == 0){ // no potential move, return -1
            return -1;
        }
        return potentialNextMoves.get(randomizer.nextInt(potentialNextMoves.size())); // pick one move by randomizing
    }





}
