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

    public int advanceCalculateNextMove(){
        int[] result = minimax(boardState,true,0);
        return result[0];
    }

    public boolean checkIfCellShouldBeBlocked(int[][] boardState,int cellNumber){
        int cellX,cellY;
        // translate cellNumber into cellX,cellY
        cellX = cellNumber/3;
        cellY = cellNumber % 3;

        int weight = 0;
        int finalWeight = 0;
        for(int i = 0; i <= 2;i++){
            weight += boardState[i][cellX];
        }
        finalWeight = weight;
        weight = 0;
        
        for(int i = 0; i <= 2;i++){
            weight += boardState[cellY][i];
        }
        finalWeight = weight > finalWeight?weight:finalWeight;
        weight = 0;

        if(cellX == cellY){
            weight = boardState[0][0] + boardState[1][1] + boardState[2][2];
            finalWeight = weight > finalWeight?weight:finalWeight;
            weight = 0;
        }


        if((cellX == 0 && cellY == 2) || (cellX == 1 && cellY == 1) || (cellX == 2 && cellY == 0)){
            weight = boardState[0][2] + boardState[1][1] + boardState[2][0];
            finalWeight = weight > finalWeight?weight:finalWeight;
            weight = 0;
        }

        // cell
        ////////////////////////////////////////////////////////////
        return finalWeight == 2;
    }

    public int[] minimax(int[][] boardState,boolean isBotTurn,int depth){
        // the first call is alway true
        
        //find empty position
        ArrayList<Integer> emptyPositions = new ArrayList<Integer>();
        
        for(int y = 0;y <= 2;y++){
            for(int x = 0; x <= 2;x++){
                if(boardState[y][x] == 0){
                    emptyPositions.add(3 * y + x);
                }
            }
        }

        int[][] simulatedBoardState = new int[3][3];



        int winner = 0;
        int[] moveAndFinalScore = new int[2];

        ArrayList<Integer> nextMoves = new ArrayList<Integer>();
        ArrayList<Integer> scores = new ArrayList<Integer>();


        for(int ep : emptyPositions){

            for(int y = 0;y <= 2;y++){ // reset simulatedstate as boardstate
                for(int x = 0;x <= 2;x++){
                    simulatedBoardState[y][x] = boardState[y][x];
                }
            }

            simulatedBoardState[ep / 3][ep % 3] = isBotTurn? -1 : 1;

            winner = game.checkWinner(simulatedBoardState);

            if(winner == -1){
                nextMoves.add(ep);
                scores.add(10 - depth);
            }else if(winner == 1){
                nextMoves.add(ep);
                scores.add(depth - 10);
            }else if(emptyPositions.size() > 1){
                moveAndFinalScore = minimax(simulatedBoardState, !isBotTurn, depth + 1);
                nextMoves.add(ep);
                scores.add(moveAndFinalScore[1]);
            }else{
                nextMoves.add(ep);
                scores.add(0);
            }

        }

        // if(depth == 0){ //////////////////// FOR DEBUGING

        //     for(int i = 0;i < nextMoves.size();i++){
        //         System.out.print(nextMoves.get(i) + " ");
        //     }
        //     System.out.print("\n\n");
        //     for(int i = 0;i < scores.size();i++){
        //         System.out.print(scores.get(i) + " ");
        //     }
        //     System.out.print("\n\n");
        // } //////////////////// FOR DEBUGING

        for(int i = 0;i < nextMoves.size() - 1;i++){
            for(int j = 0,tmp;j < nextMoves.size() - 1 - i;j++){
                if((scores.get(j) > scores.get(j + 1) && !isBotTurn) || (scores.get(j) < scores.get(j + 1) && isBotTurn)){
                    tmp = scores.get(j);
                    scores.set(j,scores.get(j + 1));
                    scores.set(j + 1,tmp);
                    tmp = nextMoves.get(j);
                    nextMoves.set(j,nextMoves.get(j + 1));
                    nextMoves.set(j + 1,tmp);
                }
            }
        }

        int idealScore = scores.get(0);
        int potentialMovesCount = nextMoves.size();
        for(int i = 0;i < nextMoves.size();i++){
            if(idealScore != scores.get(i)){
                potentialMovesCount = i;
                break;
            }
        }

        

        int finalIndex = -1;
        // if(potentialMovesCount > 1){
        //     // check if anywhere should be block //////////////////////////////////////////////////////////////
        //     for(int i = 0; i < potentialMovesCount;i++){
        //         if(checkIfCellShouldBeBlocked(boardState, nextMoves.get(i))){
        //             moveAndFinalScore[0] = nextMoves.get(i);
        //             moveAndFinalScore[1] = scores.get(i);
        //             finalIndex = i;
        //             if(depth == 0){
        //                 System.out.println("BLOCK!");
        //             }
        //             break;
        //         }
        //     }

        // }
        
        if(finalIndex == -1){
            //if no cell should be block to stop the player from wining,  randomized
            finalIndex = randomizer.nextInt(potentialMovesCount);
        }
        moveAndFinalScore[0] = nextMoves.get(finalIndex);
        moveAndFinalScore[1] = scores.get(finalIndex);

        if(depth == 0){ //////////////////// FOR DEBUGING

            System.out.println("choices " + potentialMovesCount + " value " + idealScore + "\n");
            System.out.println(moveAndFinalScore[0] + "\n");
            System.out.println(moveAndFinalScore[1] + "\n");

            for(int i = 0;i < nextMoves.size();i++){
                System.out.print(nextMoves.get(i) + " ");
            }
            System.out.print("\n\n");
            for(int i = 0;i < scores.size();i++){
                System.out.print(scores.get(i) + " ");
            }
            System.out.print("\n\n");
        } //////////////////// FOR DEBUGING

        return moveAndFinalScore;



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
