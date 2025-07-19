package personal.jarvx.shared.model;

import java.util.HashMap;

public enum MessageUrgency {

    HIGH(new NotifyData(1, "critical", 3000)),
    MEDIUM(new NotifyData(2, "normal", 2000)),
    LOW(new NotifyData(100, "low", 1000)),
    NEUTRAL(new NotifyData(1000, "low", 1000));

    public record NotifyData (int delay, String urgency, int expireTime){}


    public final NotifyData notifyData;

    MessageUrgency(NotifyData notifyData){
        this.notifyData = notifyData;
    }

    public byte getPriorityByte(){
       return (byte) this.ordinal();
    }

    public static MessageUrgency fromByte(byte priority) {
        for (MessageUrgency urgency : MessageUrgency.values()) {
            if (urgency.getPriorityByte() == priority) {
                return urgency;
            }
        }
        return null;
    }

}
