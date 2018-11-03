import java.util.ArrayList;

public class BoardState {

    private int[][] board;
    private int heuristic;
    private Boolean maxMove;
    private ArrayList<BoardState> possibleMoves;
    private int lastMoveX;
    private int lastMoveY;
    private Boolean win;
    private int treeValue;
    private Boolean fullBoard;
    int oneSideThreeForMax = 0;
    int oneSideThreeForMin = 0;
    int twoSideThreeForMax = 0;
    int twoSideThreeForMin = 0;
    int twoForMax = 0;
    int twoForMin = 0;

    public BoardState(int [][] board, Boolean maxMove, int ply){

        this.win = false;
        this.fullBoard = false;
        this.possibleMoves = new ArrayList<>();
        this.board = this.CopyBoard(board);
        this.maxMove = maxMove;
        this.heuristic = this.GetHeuristic(this.board);
        this.treeValue = this.heuristic;
        this.InitializePossibleMoves(ply);

    }

    private int[][] CopyBoard(int[][] board){
        int[][] newBoard = new int[6][6];

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    private void InitializePossibleMoves(int ply) {
        if(ply == 0)
            return;
        for(Integer i = 0; i < 6; i++){
            for(Integer j = 0; j < 6; j++){
                if(this.board[i][j] == -1){
                    int[][] newBoard = CopyBoard(this.board);
                    if(!this.maxMove)
                        newBoard[i][j] = 1;
                    else
                        newBoard[i][j] = 0;

                    BoardState newState = new BoardState(newBoard, !this.maxMove, ply -1);
                    newState.lastMoveX = j;
                    newState.lastMoveY = i;
                    this.possibleMoves.add(newState);
                }
            }
        }
    }

    public Integer GetHeuristic(int[][] board){
        int score = 0;

        for(Integer i = 0; i < 10; i++){
            if(i < 6) {
                score += EvaluateLine('r', i, board);
                score += EvaluateLine('c', i, board);
            }

            score += EvaluateLine('d', i, board);
        }

        if(this.win == true && this.maxMove)
            return 10000 + score;
        if(this.win == true && !this.maxMove)
            return -10000 + score;

        return getScore();
    }

    private int EvaluateLine(char type, int num, int[][]board){

        int min = 0;
        int max = 0;
        int score = 0;
        boolean startWithBlank = false;
        int space = -1;
        int i;


        for(i = 0; i < 6; i++){
            switch(type){

                case 'r':
                    space = board[num][i];
                    break;

                case 'c':
                    space = board[i][num];
                    break;

                case 'd':
                    switch(num){
                        case 0:
                            if(i > 3){
                                space = 2;
                                break;
                            }
                            space = board[3 - i][i];
                            break;

                        case 1:
                            if(i > 4){
                                space = 2;
                                break;
                            }
                            space = board[4 - i][i];
                            break;

                        case 2:
                            space = board[5 - i][i];
                            break;

                        case 3:
                            if(i > 4){
                                space = 2;
                                break;
                            }
                            space = board[5 - i][i + 1];
                            break;

                        case 4:
                            if(i > 3){
                                space = 2;
                                break;
                            }
                            space = board[5 - i][i + 2];
                            break;

                        case 5:
                            if(i > 3){
                                space = 2;
                                break;
                            }
                            space = board[i + 2][i];
                            break;

                        case 6:
                            if(i > 4){
                                space = 2;
                                break;
                            }
                            space = board[i + 1][i];
                            break;

                        case 7:
                            space = board[i][i];
                            break;

                        case 8:
                            if(i > 4){
                                space = 2;
                                break;
                            }
                            space = board[i][i + 1];
                            break;

                        case 9:
                            if(i > 3){
                                space = 2;
                                break;
                            }
                            space = board[i][i + 2];
                            break;

                    }

                default:
                    break;

            }

            switch(space){
                case -1:

                    if(startWithBlank == true) {
                        if (max == 3)
                            this.twoSideThreeForMax++;
                        if (min == 3)
                            this.twoSideThreeForMin++;
                    }
                    else {
                        if (max == 3)
                            this.oneSideThreeForMax++;
                        if (min == 3)
                            this.oneSideThreeForMin++;
                    }
                    if (max == 2 && startWithBlank == false)
                        this.twoForMax++;
                    if (min == 2 && startWithBlank == false)
                        this.twoForMin++;
                    startWithBlank = true;
                    max = 0;
                    min = 0;
                    break;

                case 0:
                    if(max > 0){
                        startWithBlank = false;
                    }
                    min++;
                    if(min == 4){
                        this.win = true;
                        return getScore();
                    }
                    if(type == 'd') {
                        if (num == 0 || num == 4 || num == 5 || num == 9) {
                            if (i == 3 && startWithBlank == true && min == 2) {
                                this.twoForMin++;
                                break;
                            }
                            if (i == 3 && startWithBlank == true && min == 3) {
                                this.oneSideThreeForMin++;
                                break;
                            }
                        }
                        if (num == 1 || num == 3 || num == 6 || num == 8){
                            if (i == 4 && startWithBlank == true && min == 2) {
                                this.twoForMin++;
                                break;
                            }
                            if (i == 4 && startWithBlank == true && min == 3) {
                                this.oneSideThreeForMin++;
                                break;
                            }
                        }
                    }
                    if(i == 5 && startWithBlank == true && min == 3)
                        this.oneSideThreeForMin++;
                    if(startWithBlank == true && min == 2)
                        this.twoForMin++;


                    max = 0;
                    break;

                case 1:
                    if(min > 0) {
                        startWithBlank = false;
                    }
                    max++;
                    if(max == 4){
                        this.win = true;
                        return getScore();
                    }
                    if(type == 'd') {
                        if (num == 0 || num == 4 || num == 5 || num == 9) {
                            if (i == 3 && startWithBlank == true && max == 2) {
                                this.twoForMax++;
                                break;
                            }
                            if (i == 3 && startWithBlank == true && max == 3) {
                                this.oneSideThreeForMax++;
                                break;
                            }
                        }
                        if (num == 1 || num == 3 || num == 6 || num == 8){
                            if (i == 4 && startWithBlank == true && max == 2) {
                                this.twoForMax++;
                                break;
                            }
                            if (i == 4 && startWithBlank == true && max == 3) {
                                this.oneSideThreeForMax++;
                                break;
                            }
                        }
                    }

                    if(i == 5 && startWithBlank == true && max == 3)
                        this.oneSideThreeForMax++;
                    if(startWithBlank == true && max == 2)
                        this.twoForMax++;


                    min = 0;
                    break;

                default:
                    return getScore();
            }
        }

        return getScore();
    }

    private int getScore(){

        if(!this.maxMove){
            return 5 * this.twoSideThreeForMax - 10 * this.twoSideThreeForMin + 3 * this.oneSideThreeForMax - 6 * this.oneSideThreeForMin + this.twoForMax - this.twoForMin;
        }
        else
            return 10 * this.twoSideThreeForMax - 5 * this.twoSideThreeForMin + 6 * this.oneSideThreeForMax - 3 * this.oneSideThreeForMin + this.twoForMax - this.twoForMin;
    }


    public BoardState GetMoveTwoPly() {
        BoardState thisState = new BoardState(this.board, this.maxMove, 2);

        if(thisState.possibleMoves.size() == 1){
            if(thisState.possibleMoves.get(0).win)
                return thisState.possibleMoves.get(0);
            thisState.possibleMoves.get(0).fullBoard = true;
            return thisState.possibleMoves.get(0);
        }

        for(BoardState move: thisState.possibleMoves){
            if(move.win)
                return move;
            move.FindBestChildren();
        }

        return thisState.FindBestChildren();
    }

    public BoardState GetMoveFourPly(){
        BoardState thisState = new BoardState(this.board, this.maxMove, 4);

        if(thisState.possibleMoves.size() == 1){
            if(thisState.possibleMoves.get(0).win)
                return thisState.possibleMoves.get(0);
            this.possibleMoves.get(0).fullBoard = true;
            return this.possibleMoves.get(0);
        }
        if(thisState.possibleMoves.size() == 2){
            return GetMoveTwoPly();
        }
        if(thisState.possibleMoves.size() == 3){
            for(BoardState child: thisState.possibleMoves){
                if(child.win)
                    return child;
                for(BoardState grandChild: child.possibleMoves){
                    grandChild.FindBestChildren();
                }
                child.FindBestChildren();
            }

            return thisState.FindBestChildren();
        }
        else {
            for(BoardState child: thisState.possibleMoves){
                if(child.win)
                    return child;
                for(BoardState grandChild: child.possibleMoves){
                    for(BoardState greatGrandChild: grandChild.possibleMoves){
                        greatGrandChild.FindBestChildren();
                    }
                    grandChild.FindBestChildren();
                }
                child.FindBestChildren();
            }
            return thisState.FindBestChildren();

        }
    }

    private BoardState FindBestChildren(){
        ArrayList<BoardState> bestChildren = new ArrayList<>();

        bestChildren.add(this.possibleMoves.get(0));

        for (BoardState child:this.possibleMoves){
            if(child.treeValue == bestChildren.get(0).treeValue) {
                bestChildren.add(child);
                continue;
            }
            if(!this.maxMove){
                if(child.treeValue > bestChildren.get(0).treeValue){
                    bestChildren.clear();
                    bestChildren.add(child);
                }
            }
            else {
                if(child.treeValue < bestChildren.get(0).treeValue){
                    bestChildren.clear();
                    bestChildren.add(child);
                }
            }
        }

        this.treeValue = bestChildren.get(0).treeValue;


        return bestChildren.get((int) (Math.random() * bestChildren.size()) % bestChildren.size());
    }


    public void PrintBoard(){
        System.out.println("");
        if(this.maxMove){
            System.out.println("Max Move:");
        }
        else
            System.out.println("Min Move:");
        for(int i = 0; i < 6; i++){
            System.out.print(" | ");
            for(Integer j = 0; j < 6; j++){

                switch (this.board[i][j]){
                    case 1:
                        System.out.print("X");
                        break;

                    case 0:
                        System.out.print("O");
                        break;

                    case -1:
                        System.out.print("_");
                        break;

                    default:
                        break;
                }
                if(i == lastMoveY && j == lastMoveX)
                    System.out.print("'| ");
                else
                    System.out.print(" | ");
            }
            System.out.println("");
        }
        System.out.println("Heuristic value: " + this.heuristic);
    }

    public Boolean GetWin() {
        return this.win;
    }

    public Boolean GetFullBoard() {
        return fullBoard;
    }

    public Boolean GetMove() {
        return maxMove;
    }
}
