package personal.jarvx.modules;

import personal.jarvx.MessageBus;

public abstract class ConsumerModuleAbstract {

    /* Injection */
    protected final MessageBus bus;
    private Boolean on;


    /* Callable by the consumer manager */

    public final void moduleStart(){
        this.on = true;
        this.start();
    }

    public final void moduleStop(){
        this.on = false;
        this.stop();
    }


    /* Overridable by users */

    protected void start(){

    }

    // ? Filter (dunno)

    protected void stop(){

    }

    protected ConsumerModuleAbstract(final MessageBus bus) {
        this.bus = bus;
        this.on = false;
    }

}
