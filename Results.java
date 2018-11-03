public class Results {

    public int playerOneWin;
    public int playerTwoWin;
    public int noPlayerWin;
    public long totalTime;
    public int nodesGenerated;

    public Results(){
        playerOneWin = 0;
        playerTwoWin = 0;
        noPlayerWin = 0;
    }

    public void PrintResults(){
        System.out.println("Player 1 Wins: " + playerOneWin);
        System.out.println("Player 2 Wins: " + playerTwoWin);
        System.out.println("No Player Wins: " + noPlayerWin);
    }
}
