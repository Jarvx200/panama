package personal.jarvx;

import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class MessageBus extends Thread{

    private static final int RECON_TIME = 1;
    private final HashMap<MessageUrgency, Queue<Message>> urgencyQueues;
    private final Thread serverConnectThread;



    private Socket serverSocket = null;

    public MessageBus(@Nonnull String host, int port) {

        Timer popTimer = new Timer();


        serverConnectThread = new Thread(() -> {
            while (serverSocket == null) {
                try {
                    sleep(RECON_TIME * 1000);
                    serverSocket = new Socket(host, port);
                }
                catch (IOException e){
                    System.err.println("Could not connect to server, all messages will be cached locally!");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
       });

        serverConnectThread.start();

        urgencyQueues = new HashMap<>();

        for (MessageUrgency urgency : MessageUrgency.values()) {
            urgencyQueues.put(urgency, new LinkedList<>());
            TimerTask popTask = new TimerTask() {
                @Override
                public void run() {
                    serialPop(urgency);
                }
            };
            popTimer.schedule(popTask, 0, urgency.notifyData.delay());
        }
    }

    public void push(Message messageSuper){
        Message lastMessage = urgencyQueues.get(messageSuper.getUrgency()).peek();
        if(lastMessage != null){
            if(!lastMessage.equals(messageSuper)){
                urgencyQueues.get(messageSuper.getUrgency()).add(messageSuper);
            }
        } else {
            urgencyQueues.get(messageSuper.getUrgency()).add(messageSuper);
        }
    }

    public void serialPop(MessageUrgency urgency){

        if(this.serverConnectThread.isAlive())
            return;


        if(urgencyQueues.get(urgency).isEmpty()) {
            return;
        }


        Message toSend =  urgencyQueues.get(urgency).poll();

        if(toSend == null){
            return;
        }

        try{
            byte[] messagePacket = toSend.serialize();

            serverSocket.getOutputStream().write(messagePacket);

        } catch (IOException e){
            System.err.println("Could not serialize message!");
        }

    }


    @Override
    public void run() {
        try{
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
