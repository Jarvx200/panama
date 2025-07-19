package personal.jarvx;

import personal.jarvx.model.Notification;
import personal.jarvx.shared.model.Message;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Client extends Thread {
    private final Socket clientSocket;
    private final Inet4Address ip;



    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.ip = null;
    }

    @Override
    public void run() {

        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                Notification notification = new Notification(ip, Message.deserialize(line.getBytes()));
                notification.send();
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
