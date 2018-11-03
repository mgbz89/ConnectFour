public class PlayGame {

    public static void main(String [ ] args)
    {

        Results results = new Results();

        int[][] board =     {{-1,-1,-1,-1,-1,-1},
                            {-1,-1,-1,-1,-1,-1},
                            {-1,-1,-1,-1,-1,-1},
                            {-1,-1,-1,-1,-1,-1},
                            {-1,-1,-1,-1,-1,-1},
                            {-1,-1,-1,-1,-1,-1}};

        long time = System.currentTimeMillis();
        int count = 0;
        for(int i = 0; i < 1; i++) {
            BoardState curr = new BoardState(board, true, 4);
            while (!curr.GetWin() && !curr.GetFullBoard()) {
                count++;
                curr.PrintBoard();
                curr = curr.GetMoveFourPly();
                //curr = curr.GetMoveTwoPly();
            }
            curr.PrintBoard();
            if(curr.GetFullBoard()) {
                System.out.println("No Player Won");
                results.noPlayerWin++;
            }
            else
                if(!curr.GetMove()) {
                    System.out.println("Player 2 Win");
                    results.playerTwoWin++;
                }
                else {
                    System.out.println("Player 1 Win");
                    results.playerOneWin++;
                }
        }
        System.out.println("\n\nResults\n-------");
        System.out.println("Total time: " + (System.currentTimeMillis() - time));
        System.out.println("Total moves: " + count);

        results.PrintResults();
    }
}