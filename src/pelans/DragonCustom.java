package pelans;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pelans.dragon.CargarEntidades;
import pelans.dragon.DragonDelEnd;


public class DragonCustom extends JavaPlugin {
	public static DragonCustom plugin;
	private static PluginDescriptionFile pdffile;
	public static String version;
	public static String nombre;
	public static String worldName;
	
	@Override
	public void onLoad() {

		
	}
	public void onEnable() {
		
		plugin = this;
		pdffile = plugin.getDescription();
		version = pdffile.getVersion();
		nombre = ChatColor.YELLOW+"["+ChatColor.BLUE+pdffile.getName()+ChatColor.YELLOW+"] "; //[MiPlugin] Amarillo, Azul, Amarillo
		
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+" Activando plugin (version: "+ChatColor.RED+version+ChatColor.WHITE+")");
		
		
		
		registerConfig();
		registerEvents();
		
		
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
}
