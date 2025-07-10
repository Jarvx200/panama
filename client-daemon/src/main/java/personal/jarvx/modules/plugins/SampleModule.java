package personal.jarvx.modules.plugins;

import personal.jarvx.MessageBus;
import personal.jarvx.modules.ConsumerModuleAbstract;

import java.io.Console;

public class SampleModule extends ConsumerModuleAbstract{
    public SampleModule(MessageBus bus) {
        super(bus);
    }

    @Override
    public void start() {
        System.out.println("SampleModule started");
    }

    @Override
    public void stop() {
        System.out.println("SampleModule stopped");
    }
}
