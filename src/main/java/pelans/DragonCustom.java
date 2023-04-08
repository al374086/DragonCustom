package pelans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pelans.Util.DependencyManager;
import pelans.commands.DragonCommand;
import pelans.dragon.CargarEntidades;
import pelans.dragon.DragonDelEnd;

import java.io.File;


public class DragonCustom extends JavaPlugin {
	public static DragonCustom plugin;
	private final PluginDescriptionFile pdffile = getDescription();
	public final String version = pdffile.getVersion();
	public final String nombre = ChatColor.YELLOW+"["+ChatColor.BLUE+pdffile.getName()+ChatColor.YELLOW+"] "; //[MiPlugin] Amarillo, Azul, Amarillo
	public static String worldName;

	public void onEnable() {
		
		plugin = this;
		
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+" Activando plugin (version: "+ChatColor.RED+version+ChatColor.WHITE+")");
		
		registerConfig();
		DependencyManager.checkPluginsDependencies();
		registerEvents();
		registerCommands();
		
		worldName = this.getConfig().getString("Config.mundo");
		
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+" Mundo donde esta el jefe custom: " + ChatColor.DARK_PURPLE + worldName);
		
		for(World world : Bukkit.getServer().getWorlds()) {
			for(Entity entidad : world.getEntities()) {
				CargarEntidades.analizarEntidad(entidad);
			}
		}

		
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+" Ha sido activado (version: "+ChatColor.RED+version+ChatColor.WHITE+")");
	}
			
	
	public void onDisable() {
		Scoreboard test = DragonCustom.plugin.getServer().getScoreboardManager().getMainScoreboard();
		Team teamColor;
		teamColor = test.getTeam("Inferno_Ball");
		if (teamColor != null){
			teamColor.unregister();
		}
	}
	
	
	public void registerConfig() {
		File config = new File(this.getDataFolder(),"config.yml");
		//String rutaConfig = config.getPath();
		if(!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new DragonDelEnd(), this);
		pm.registerEvents(new CargarEntidades(), this);
	}

	public void registerCommands(){
		this.getCommand("dragon").setExecutor(new DragonCommand());
	}
}
