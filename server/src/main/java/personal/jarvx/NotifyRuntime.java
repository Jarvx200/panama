package personal.jarvx;

import java.io.IOException;

public class NotifyRuntime {

    private static NotifyRuntime instance = new NotifyRuntime();

    Runtime executionRuntime;

    private NotifyRuntime() {
        executionRuntime = Runtime.getRuntime();
    }

    public void sendNotification(String[] arguments){
        try{
            executionRuntime.exec(arguments);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public synchronized static NotifyRuntime getInstance() {
        if (instance == null) {
            instance = new NotifyRuntime();
        }
        return instance;
    }
}
