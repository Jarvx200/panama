package personal.jarvx.model;

import personal.jarvx.shared.model.Message;

import java.net.Inet4Address;

public class Notification {
    private final Inet4Address ip;
    private final Message message;

    public Notification(Inet4Address ip, Message message) {
        this.ip = ip;
        this.message = message;
    }

}
