package pelans.dependencies;

import pelans.ProtocolsEvents.ProtocolEntityEvents;

public class ProtocolHook {

    public static void registerPorotocolEvents(){
        ProtocolEntityEvents.register();
    }
}
