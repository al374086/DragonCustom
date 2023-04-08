package pelans.Util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pelans.dependencies.ProtocolHook;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DependencyManager {
    public static boolean ProtocolLib = true;
    public static boolean WorldEdit = true;
    private static final Logger log = Bukkit.getLogger();

    public static void checkPluginsDependencies(){
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        List<String> names = new ArrayList<>();
        for (Plugin plugin : plugins) {
            names.add(plugin.getName());
        }

        if (!names.contains("ProtocolLib")){
            ProtocolLib = false;
            log.warning("The ProtocolLib is not found some functions will stop working");
        }else {
            ProtocolHook.registerPorotocolEvents();
        }

        if (!(names.contains("WorldEdit") || names.contains("FastAsyncWorldEdit"))){
            WorldEdit = false;
            log.warning("The worldedit is not found some functions will stop working or the time of some functions that imply big changes in the world will be increased");
        }

    }
}
