package personal.jarvx.shared.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Supplier;

public class Message {

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


    private HashMap<String, Supplier<byte[]>> protocolMap = new HashMap<>(){{
        put(":URGENCY:", ()->new byte[] { urgency.getPriorityByte() } );
        put(":PRODUCER:",() -> producer.getBytes(StandardCharsets.UTF_8));
        put(":CONTENT:", () -> content.getBytes(StandardCharsets.UTF_8));
        put(":IP:", "127.0.0.1"::getBytes);
    }}; // will be shared by server, possible tuplet in val

    //TODO: Extend with inherited message types, users should be able to extend message and fields, both on the daemon and server

    public Message(String producer,String message, MessageUrgency messageUrgency) {
        this.content = message;
        this.urgency = messageUrgency;
        this.producer = producer;
    }


    public MessageUrgency getUrgency() {
        return this.urgency;
    }

    public String getContent() {
        return this.content;
    }


    // Might move logic to packetbuilder if the protocol will be extended
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream vlenPacket = new ByteArrayOutputStream();

        for(String key : protocolMap.keySet()) {
            vlenPacket.write(key.getBytes(StandardCharsets.UTF_8));
            vlenPacket.write(protocolMap.get(key).get());
        }

        return vlenPacket.toByteArray();
    }

}
