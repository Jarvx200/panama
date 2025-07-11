package personal.jarvx;

import org.reflections.Reflections;
import personal.jarvx.modules.ConsumerModuleAbstract;
import personal.jarvx.modules.ConsumerModuleThread;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ConsumerManager {
    private static ConsumerManager instance = null;


    private final MessageBus messageBus;
    private HashMap<String, ConsumerModuleThread> consumers;

    private ConsumerManager(){
        consumers = new HashMap<>();
        this.messageBus = new MessageBus("127.0.0.1", 2910);
        loadModules(Optional.ofNullable(""));
    };


    public void loadModules(Optional<String> archivePath){

        messageBus.start();

        String scannablePackage = archivePath
                .filter(s-> !s.trim().isEmpty())
                .orElse("personal.jarvx.modules.plugins");

        Reflections pluginReflections = new Reflections(scannablePackage);

        Set<Class<? extends ConsumerModuleAbstract>> plugins = pluginReflections.getSubTypesOf(ConsumerModuleAbstract.class);

        plugins.forEach(
                (pluginClass) -> {
                    try {
                        ConsumerModuleAbstract moduleBase = pluginClass.getDeclaredConstructor(MessageBus.class).newInstance(messageBus);
                        ConsumerModuleThread consumerModuleThread = new ConsumerModuleThread(moduleBase);
                        consumerModuleThread.startModule();


                        consumers.put(UUID.randomUUID().toString(),consumerModuleThread);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e){
                        System.err.println("Could not load module: " + pluginClass.getName() + "\n" + e.getMessage());
                    }
                }
        );

    }

    public synchronized static ConsumerManager getInstance(){
        if(instance == null){ instance = new ConsumerManager(); }
        return instance;
    }

    //Reload if needed

}
