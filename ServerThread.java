//Khanh Nguyen
import java.io.*;
import java.net.*;
import java.util.Random;

public class ServerThread extends Thread{
    private Socket client;
    private DataInputStream instream;
    private DataOutputStream outstream;
    private PrintWriter out;
    private BufferedReader in;
    private Random gen;
    private char[][] board;
    private int row, col;

    ServerThread(Socket socket){
        try{
            client = socket;
            gen = new Random();
            instream = new DataInputStream(client.getInputStream());
            outstream = new DataOutputStream(client.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(outstream), true);
            in = new BufferedReader(new InputStreamReader(instream));

            board = new char[4][4];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    board[i][j] = ' ';
                }
            }
            row = -1;
            col = -1;
        }catch(IOException e){
            System.out.println("Error in ServerThread!");
        }
    }
    public void run(){
        int counter = 0;
        String response = "";
        boolean gameover = false;
        int randNum = gen.nextInt();
        boolean turn;
        if(randNum % 2 == 0){
            turn = false;
        }else{
            turn = true;
        }

        if(turn){
            out.println("CLIENT");
        }
        // else{
        //     makeMove();
        //     counter++;
        //     printBoard();
        //     out.println("MOVE " + row + " " + col);
        // }

        while(!gameover){
            if(turn){
                //client turn
                try{
                    response = in.readLine();
                }catch(IOException e){
                    System.out.println("Read error!");
                }
                String[] data = response.split("\\s+");
                row = Integer.parseInt(data[1]);
                col = Integer.parseInt(data[2]);
                board[row][col] = 'O';
                System.out.println("\n\nClient move");
                printBoard();
                counter++;
                if(checkWin() || counter == 16){
                    gameover = true;
                    if(checkWin()){
                        out.println("MOVE -1 -1 WIN");
                    }else{
                        out.println("MOVE -1 -1 TIE");
                    }
                }
            }else{
                //server turn
                makeMove();
                counter++;
                board[row][col] = 'X';
                System.out.println("\n\nServer move");
                printBoard();
                if(checkWin() || counter == 16){
                    gameover = true;
                    if(checkWin()){
                        out.println("MOVE " + row + " " + col + " LOSS");
                    }else{
                        out.println("MOVE " + row + " " + col + " TIE");
                    }
                }else{
                    out.println("MOVE " + row + " " + col );
                }
            }
            turn = !turn;
        }

    }

    public void makeMove(){
        while(true){
            row = gen.nextInt(4);
            col = gen.nextInt(4);
            if(board[row][col] == ' '){
                break;
            }
        }
        // out.println("MOVE " + row + " " + col);
    }

    public void printBoard(){
        System.out.println("-----------------");
        for(int i = 0; i < 4; i++){
            System.out.print("| ");
            for(int j = 0; j < 4; j++){
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-----------------");
        }
    }

    public boolean checkWin(){
        //check horizonal
        for(int x = 0; x < 4; x++){
            if(board[x][0] == board[x][1] && board[x][1] == board[x][2] && board[x][2] == board[x][3] && board[x][3] != ' '){
                return true;
            }
        }
        //check vertical
        for(int y = 0; y < 4; y++){
            if(board[0][y] == board[1][y] && board[1][y] == board[2][y] && board[2][y] == board[3][y] && board[3][y] != ' '){
                return true;
            }
        }
        //check diagonal
        if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == board[3][3] && board[3][3] != ' '){
            return true;
        }
        //check anti-diagonal
        if (board[0][3] == board[1][2] && board[1][2] == board[2][1] && board[2][1] == board[3][0] && board[3][0] != ' ') {
        return true;
    }

        return false;
    }
}