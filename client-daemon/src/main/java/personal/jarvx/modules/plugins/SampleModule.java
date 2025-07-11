package personal.jarvx.modules.plugins;

import personal.jarvx.MessageBus;
import personal.jarvx.modules.ConsumerModuleAbstract;
import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

public class SampleModule extends ConsumerModuleAbstract{
    public SampleModule(MessageBus bus) {
        super(bus);
    }

    @Override
    public void start() {
        bus.push(new Message("Discord", "Hello world", MessageUrgency.MEDIUM));
    }

    @Override
    public void stop() {
        System.out.println("SampleModule stopped");
    }
}
