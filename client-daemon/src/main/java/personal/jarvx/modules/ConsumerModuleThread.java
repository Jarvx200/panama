package personal.jarvx.modules;

public class ConsumerModuleThread extends Thread {

    private final ConsumerModuleAbstract module;
    private boolean running;

    public void startModule(){
        this.running = true;
        this.start();
    }

    public void stopModule(){
        this.running = false;
    }

    public boolean isRunning(){
        return this.running;
    }

    public ConsumerModuleThread(final ConsumerModuleAbstract module) {
        this.module = module;
        this.running = true;
    }

    public void run(){
        while (this.running) {
            this.module.start();
        }
        this.module.stop();
    }
}
