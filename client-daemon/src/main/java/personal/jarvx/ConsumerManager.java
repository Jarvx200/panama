package personal.jarvx;

public class ConsumerManager {
    private static ConsumerManager instance = null;

    private ConsumerManager(){
        loadModules();
    };

    private void loadModules(){


    }

    public synchronized static ConsumerManager getInstance(){
        if(instance == null){ instance = new ConsumerManager(); }
        return instance;
    }

    //Reload if needed

}
