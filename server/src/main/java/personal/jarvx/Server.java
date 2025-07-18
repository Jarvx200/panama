package personal.jarvx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static Server instance = new Server();
    private static ArrayList<Client> clients = new ArrayList<Client>();

    private final static int PORT = 2910;
    private ServerSocket serverSocket;

    private final Thread acceptClientsThread;

    private volatile boolean running = true;


    public void startListening(){
        try{

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on: " + PORT);
        } catch (Exception e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
    }

    private Server() {
        startListening();
        running = true;

        acceptClientsThread = new Thread(() -> {
            while(running){
                try {
                    Socket clientSocket = serverSocket.accept();
                    clients.add(new Client(clientSocket));
                    clients.getLast().start();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        acceptClientsThread.start();
    }

    public synchronized static Server getInstance() {
        if(instance == null) {
            instance = new Server();
        }
        return instance;
    }

}
