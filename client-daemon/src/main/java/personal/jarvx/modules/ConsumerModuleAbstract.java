package personal.jarvx.modules;

import personal.jarvx.MessageBus;

import java.io.Console;

public abstract class ConsumerModuleAbstract {

    /* Injection */
    protected final MessageBus bus;

    public abstract void start();
    public abstract void stop();

    //TODO: Filter callable by super on children to forward to injected bus

    public ConsumerModuleAbstract(final MessageBus bus) {
        this.bus = bus;
    }

}
