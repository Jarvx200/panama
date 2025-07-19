package personal.jarvx;

import personal.jarvx.shared.model.Message;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Client extends Thread {
    private final Socket clientSocket;
    private final InetAddress ip;



    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.ip = clientSocket.getInetAddress();
    }

    @Override
    public void run() {

        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cmd = {"notify-send", "Hello!", Message.deserialize(line.getBytes()).getContent()};
                Runtime.getRuntime().exec(cmd);

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
    }}
