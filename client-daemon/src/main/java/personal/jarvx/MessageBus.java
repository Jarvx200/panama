package personal.jarvx;

import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class MessageBus extends Thread{


    private HashMap<MessageUrgency, Queue<Message>> urgencyQueues;
    private Timer popTimer;


    private Socket serverSocket = null;

    public MessageBus(@Nonnull String host, int port) {

        popTimer = new Timer();


        try{
            serverSocket = new Socket(host, port);
        } catch (Exception e){
            System.err.println("Could not connect to server, all messages will be cached locally!");
            //TODO: Thread to retry connection
        }
        urgencyQueues = new HashMap<>();

        for (MessageUrgency urgency : MessageUrgency.values()) {
            urgencyQueues.put(urgency, new LinkedList<>());
            TimerTask popTask = new TimerTask() {
                @Override
                public void run() {
                    serialPop(urgency);
                }
            };
            popTimer.schedule(popTask, 0, urgency.getDelay());
        }
    }

    public void push(Message messageSuper){
        urgencyQueues.get(messageSuper.getUrgency()).add(messageSuper);
    }

    public void serialPop(MessageUrgency urgency){

        if(urgencyQueues.get(urgency).isEmpty()){
            System.out.println("Sending " );
            return;
        }
        Message toSend =  urgencyQueues.get(urgency).poll();
        assert toSend != null;


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
