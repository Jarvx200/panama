package personal.jarvx.shared.model;

public enum MessageUrgency {

    HIGH(1),
    MEDIUM(10),
    LOW(100),
    NEUTRAL(1000);

    private final Integer delay;

    MessageUrgency(Integer delay){
        this.delay = delay * 1000;
    }

    public Integer getDelay() {
        return delay;
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
