//Khanh Nguyen
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private static String hostname = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket toServerSocket;
    private static char[][] board;
    private static int row, col;

    public static void main(String[] args){
        
        try{
            toServerSocket = new Socket(hostname, 27887);
            inputStream = new DataInputStream(toServerSocket.getInputStream());
            outputStream = new DataOutputStream(toServerSocket.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(outputStream), true);
            in = new BufferedReader(new InputStreamReader(inputStream));

            // row = -1;
            // col = -1;
            createEmptyBoard();

            playgame(in, out);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void playgame(BufferedReader in, PrintWriter out) throws IOException{
        Scanner inp = new Scanner(System.in);
        String response;
        boolean turn = false;
        boolean gameover = false;

        while(!gameover){
            if(turn){
                //user turn
                String[] move;
                do{
                    System.out.println("\nEnter your move (row and column, MUST in format: row[space]col): ");
                    move = inp.nextLine().split("\\s+");
                    if(move.length == 2){
                        row = Integer.parseInt(move[0]);
                        col = Integer.parseInt(move[1]);
                    }
                }while(row < 0 || row > 3 || col < 0 || col > 3 || board[row][col] != ' ' || move.length != 2);

                board[row][col] = 'O';
                out.println("MOVE " + row + " " + col);
            }
            else{
                //server turn
                response = in.readLine();
                if(!response.equals("CLIENT")){
                    String[] args = response.split("\\s+");
                    System.out.println("\n\nSERVER MOVE");
                    System.out.println("response: " + response);
                    if(args.length > 3){
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        if (!args[3].equals("WIN") && row != -1) {
                            board[row][col] = 'X';
                        }
                        switch(args[3]){
                            case "WIN":
                                System.out.println("\n\nCongratulations!!! You WON the game");
                                break;
                            case "TIE":
                                System.out.println("\nThe game was a TIE!");
                                break;
                            case "LOSS":
                                System.out.println("\nSORRY! You LOST the game!");
                                break;
                        }
                        gameover = true;
                    }else{
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        board[row][col] = 'X';
                    }
                }else{
                    System.out.println("\nYOU MOVE FIRST");
                }
            }
            printBoard();
            turn = !turn;
        }
        System.out.println("\n\nHere is the final game board");
        printBoard();
    }

    public static void printBoard(){
        System.out.println("\n-----------------");
        for(int i = 0; i < 4; i++){
            System.out.print("| ");
            for(int j = 0; j < 4; j++){
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-----------------");
        }
    }

    public static void createEmptyBoard(){
        row = -1;
        col = -1;
        board = new char[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                board[i][j] = ' ';
            }
        }
    }
}