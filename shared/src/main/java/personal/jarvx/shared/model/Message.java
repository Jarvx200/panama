package personal.jarvx.shared.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

public class Message{

    /* PROTOCOL:

    Bytes:
    1: Message urgency,

    1+:
    L:val

    :L: - Protocol key
    val - Protocol value


    TODO: Could extend with encryption or user plugins (builder + command)

     */



    private String content = "";
    private String producer = "";
    private MessageUrgency urgency;
    private Inet4Address ip; // Name map on server

    public String getProducer() {
        return producer;
    }

    protected interface SerialPair <T>{
       void deserialize(Builder builder, byte[] message ) throws IOException;
       byte[] serialize(T args) throws IOException;
    }



    protected static HashMap<String, SerialPair<Message>> protocolMap = new HashMap<>() {{

        put(":URGENCY:", new SerialPair<Message>() {
            @Override
            public byte[] serialize(Message msg) {
                return new byte[]{ msg.getUrgency().getPriorityByte() };
            }

            @Override
            public void deserialize(Builder builder, byte[] message) {
                builder.setUrgency(MessageUrgency.fromByte(message[0]));
            }
        });

        put(":PRODUCER:", new SerialPair<Message>() {
            @Override
            public byte[] serialize(Message msg) {
                return msg.producer.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public void deserialize(Builder builder, byte[] message) {
                builder.setProducer(new String(message, StandardCharsets.UTF_8));
            }
        });

        put(":CONTENT:", new SerialPair<Message>() {
            @Override
            public byte[] serialize(Message msg) {
                return msg.content.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public void deserialize(Builder builder, byte[] message) {
                builder.setContent(new String(message, StandardCharsets.UTF_8));
            }
        });

        put(":IP:", new SerialPair<Message>() {
            @Override
            public byte[] serialize(Message msg) {
                return "127.0.0.1".getBytes(StandardCharsets.UTF_8); // placeholder
            }

            @Override
            public void deserialize(Builder builder, byte[] message) {

            }
        });
    }};


    //TODO: Extend with inherited message types, users should be able to extend message and fields, both on the daemon and server

    public Message(String producer,String content, MessageUrgency messageUrgency) {
        this.content = content;
        this.urgency = messageUrgency;
        this.producer = producer;
    }

    public Message(Builder builder) {
        this.content = builder.content;
        this.urgency = builder.urgency;
        this.producer = builder.producer;
    }


    public MessageUrgency getUrgency() {
        return this.urgency;
    }

    public String getContent() {
        return this.content;
    }


    // Might move logic to packetbuilder if the protocol will be extended
    public final byte[] serialize() throws IOException {
        ByteArrayOutputStream vlenPacket = new ByteArrayOutputStream();

        for(String key : protocolMap.keySet()) {
            vlenPacket.write(key.getBytes(StandardCharsets.UTF_8));
            vlenPacket.write(protocolMap.get(key).serialize(this));
        }

        vlenPacket.write('\n');
        return vlenPacket.toByteArray();
    }

    public static Message deserialize(byte[] buf) {
        Builder builder = new Builder();
        String content = new String(buf, StandardCharsets.UTF_8);




        int kvp = 0;
        String[] kv = new String[2];

        content = content.substring(1);
        for(String s  : content.split(":")) {
            if(kvp > 1){
                try {
                    protocolMap.get(String.format(":%s:", kv[0])).deserialize(builder, kv[1].getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.err.println("Could not deserialize field: " + kv[0] + ":" + kv[1]);
                }
                kvp = 0;
            }
            kv[kvp++] = s;
        }
        if(kvp > 1){
            try{
                protocolMap.get(String.format(":%s:", kv[0])).deserialize(builder, kv[1].getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.err.println("Could not deserialize field: " + kv[0] + ":" + kv[1]);
            }
        }


        return new Message(builder);
    }

    public static class Builder{
        private String content = "";
        private String producer = "";
        private MessageUrgency urgency;
        private Inet4Address ip; // Name map on server

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setProducer(String producer) {
            this.producer = producer;
            return this;
        }

        public Builder setUrgency(MessageUrgency urgency) {
            this.urgency = urgency;
            return this;
        }

        public Message build(){
            return new Message(this);
        }

    }



    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Message m = (Message) o;
        return this.content.equals(m.content);
    }



}
