//Khanh Nguyen
import java.net.ServerSocket;
import java.net.Socket;
// import java.util.*;
// import java.net.*;

public class Dispatcher{
    private static ServerSocket port;
    public static void main(String[] args){
        ServerThread server_thread;
        Socket clientSocket;
        try{
            port = new ServerSocket(27887);
            while(true){
                clientSocket = port.accept();
                server_thread = new ServerThread(clientSocket);
                server_thread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}