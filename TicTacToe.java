import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.lang.Math;
public class TicTacToe {
    public static int miniMax(char[][] board, char nowPlays, boolean isMaximizing, HashMap<String, Integer> gameSet){
        int bestScore;
        if (nowPlays == 'O'){
            if (!checkWinner(board, 'X').equals("null"))
                return gameSet.get(checkWinner(board, 'X'));
        }
        else{
            if (!checkWinner(board, 'O').equals("null"))
                return gameSet.get(checkWinner(board, 'O'));
        }

        if (isMaximizing){
            bestScore = Integer.MIN_VALUE;
            for (int i=0;i<board.length;i++){
                for (int j=0;j<board.length;j++){
                    if (board[i][j] == ' '){
                        board[i][j] = nowPlays;
                        int score = miniMax(board, 'O', false, gameSet);
                        board[i][j] = ' ';
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
        }
        else{
            bestScore = Integer.MAX_VALUE;
            for (int i=0;i<board.length;i++){
                for (int j=0;j<board.length;j++){
                    if (board[i][j] == ' '){
                        board[i][j] = nowPlays;
                        int score = miniMax(board, 'X', true, gameSet);
                        board[i][j] = ' ';
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }
        return bestScore;
    }
    public static void computerMove(char[][] board, char nowPlays, HashMap<String, Integer> gameSet){
        int row = -1;
        int col = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                if (board[i][j] == ' '){
                    board[i][j] = nowPlays;
                    int score = miniMax(board, 'O', false, gameSet);
                    board[i][j] = ' ';
                    if (score>bestScore){
                        bestScore = score;
                        row = i;
                        col = j;
                    }
                }
            }
        }
        if (row != -1 && col!=-1)
            board[row][col] = nowPlays;
    }
    public static void playerMove(char[][] board, char nowPlays, Scanner sc){
        int square;
        while(true){
            System.out.print("\nPlayer(0-8):");
            if (sc.hasNextInt()){
               square = sc.nextInt();
               if (board[square/3][square%3] == ' ') {
                   board[square / 3][square % 3] = nowPlays;
                   break;
               }
            }
            else {
                sc.next();
                System.out.print("Invalid Move");
            }
        }
    }
    public static void initializeBoard(char[][] board){
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++)
                board[i][j] = ' ';
        }
    }
    public static void printBoard(char[][] board){
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++) {
                System.out.print("| " + board[i][j] + " ");
                if (j == 2)
                    System.out.println('|');
            }
            if (i<2)
              System.out.println("-".repeat(13));
        }
    }
    public static String checkWinner(char[][] board, char playerSymbol){
        boolean flag = false;
        String winner = "";
        //check the columns
        for (int i=0;i<3;i++){
            if (!flag){
                if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] == playerSymbol){
                    flag = true;
                    break;
                }
            }
            else
                break;
        }
        //check the rows
        for (int i=0;i<3;i++){
            if (!flag){
                if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] == playerSymbol){
                    flag = true;
                    break;
                }
            }
            else
                break;
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] == playerSymbol)
            flag = true;
        if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[2][0] == playerSymbol)
            flag = true;
        //t means tie.There is not anyone who wins in this round
        winner += (flag?playerSymbol:isBoardFull(board)?"tie":"null");
        return winner;
    }
    public static boolean isBoardFull(char[][] board){
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board.length;j++){
                if (board[i][j] == ' ')
                    return false;
            }
        }
        return true;
    }
    public static void main(String[] args){
        char[][] board = new char [3][3];
        int coin;//

        //initialization of 3 states
        HashMap<String, Integer> gameSet = new HashMap<>();
        gameSet.put("X", 1);
        gameSet.put("O", -1);
        gameSet.put("tie", 0);

        Random r = new Random();
        initializeBoard(board);
        printBoard(board);

        //toss a coin if the player starts first or the computer.If the result of the coin
        // is 0 is player turn,otherwise is computer turn
        coin = r.nextInt(2);

        String gameResult = "";
        char nowPlays = (coin == 1?'X':'O');
        try(Scanner sc = new Scanner(System.in)){
            while(true){
                if (isBoardFull(board))
                    break;
                if (nowPlays == 'X'){
                    //isMaximizing turn
                    System.out.println("It's computer's turn.");
                    computerMove(board, nowPlays, gameSet);
                }
                else{
                    //is Player turn
                    System.out.println("It's player turn");
                    playerMove(board, nowPlays, sc);
                }
                printBoard(board);
                if (!checkWinner(board, nowPlays).equals("null"))
                    break;
                nowPlays = (nowPlays == 'X'?'O':'X');
            }
        }
        gameResult = checkWinner(board, nowPlays);
        if (gameResult.equals("tie"))
            System.out.println("Tie!");
        else
            System.out.println(gameResult + " wins!");
    }
}
