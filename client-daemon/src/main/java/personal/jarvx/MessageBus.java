package personal.jarvx;

import personal.jarvx.shared.model.MessageSuper;
import personal.jarvx.shared.model.MessageUrgency;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MessageBus {

    /* PROTOCOL:

    Bytes:
    1: Message urgency,
    2-33: Producer
    34+: Content

    TODO: Could extend with encryption

     */

    private HashMap<MessageUrgency, Queue<MessageSuper>> urgencyQueues;


    private Socket serverSocket = null;

    public MessageBus(@Nonnull String host, int port) {
        try{
            serverSocket = new Socket(host, port);
        } catch (Exception e){
            System.err.println("Could not connect to server, all messages will be cached locally!");
            //TODO: Thread to retry connection
            return;
        }
        urgencyQueues = new HashMap<>();

        for (MessageUrgency urgency : MessageUrgency.values()) {
            urgencyQueues.put(urgency, new LinkedList<>());
        }
    }

    public void push(MessageSuper messageSuper){
        urgencyQueues.get(messageSuper.getUrgency()).add(messageSuper);
    }


}
