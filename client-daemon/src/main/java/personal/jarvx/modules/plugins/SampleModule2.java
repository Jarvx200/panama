package personal.jarvx.modules.plugins;

import personal.jarvx.MessageBus;
import personal.jarvx.modules.ConsumerModuleAbstract;
import personal.jarvx.shared.model.Message;
import personal.jarvx.shared.model.MessageUrgency;

public class SampleModule2 extends ConsumerModuleAbstract{
    public SampleModule2(MessageBus bus) {
        super(bus);
    }

    @Override
    public void start() {
        bus.push(new Message("CAT", "Hello world", MessageUrgency.HIGH));
    }

    @Override
    public void stop() {
        System.out.println("SampleModule stopped");
    }
}
