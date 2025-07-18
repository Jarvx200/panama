package personal.jarvx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Client extends Thread {
    private Socket clientSocket;


    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Client says: " + line);
            }
        } catch (IOException e) {
            System.err.println("Connection with client lost: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Closed connection to client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
