import org.junit.Test;
import personal.jarvx.ConsumerManager;
import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

import java.io.IOException;
import java.util.Arrays;

public class MessageTesting {
    @Test
    public void testPriorityByte(){
        MessageUrgency mu = MessageUrgency.LOW;
        System.out.println(mu.getPriorityByte());
    }

    @Test
    public void serializeMessage() throws IOException {
        Message message = new Message("Discord", "Hello world", MessageUrgency.MEDIUM);
        System.out.println(Arrays.toString(message.serialize()));
    }

    @Test
    public void sendToSocket(){
        ConsumerManager consumerManager = ConsumerManager.getInstance();
    }

    @Test
    public void desreialize() throws IOException {
        Message message = Message.deserialize(":IP:127.0.0.1:URGENCY: :PRODUCER:CAT:CONTENT:Hello world".getBytes());

        System.out.println(message.getProducer());
    }
}
