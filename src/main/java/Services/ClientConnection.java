package Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private static ClientConnection clientConnection = null;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ClientConnection(){}

    /*
        This is a constructor for a singleton class of the socket connection
    */
    public static synchronized ClientConnection getInstance(){
        if(clientConnection == null){
            clientConnection = new ClientConnection();
        }
        return clientConnection;
    }


    /*
        This methods creates a new socket connection in order to client connect to a server
    */

    public boolean socketConnect() {
        if (socket == null /*|| !socket.isClosed()*/) {
            System.out.println("Socket is not connected, connecting socket");
            try {
                socket = new Socket("localHost", 8080);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);

            }
        }
        return true;
    }

    /*This method close the socket connection*/
    public boolean socketAbort() {
        try {

            socket.close();
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /*This method passes needed command to a server*/
    public String passToServer(String command) {
        if(socketConnect()){
            try {
                out.println(command);
                String serverResponse = in.readLine();
                if (serverResponse.equalsIgnoreCase("false")) return "false";
                return serverResponse;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "false";
    }

}
