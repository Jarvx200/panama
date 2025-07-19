package personal.jarvx.model;

import personal.jarvx.NotifyRuntime;
import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

import java.net.Inet4Address;

public class Notification {
    private final Inet4Address ip;
    private final Message message;
    private final NotifyRuntime notifyRuntime;
    private String argsString;

    private static final String notificationTemplate = "notify-send --urgency %s --expire-time %s --app-name %s %s";

    public Notification(Inet4Address ip, Message message ) {
        this.ip = ip;
        this.message = message;
        this.notifyRuntime = NotifyRuntime.getInstance();
    }

    public void send(){
        notifyRuntime.sendNotification(
                String.format(notificationTemplate,
                        message.getUrgency().notifyData.urgency(),
                        message.getUrgency().notifyData.expireTime(),
                        message.getProducer(),
                        message.getContent()
                ).split(" ")
        );
    }

}
