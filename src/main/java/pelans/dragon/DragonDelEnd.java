package pelans.dragon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import pelans.DragonCustom;
import pelans.Util.DependencyManager;
import pelans.Util.Particulas;
import pelans.dependencies.FAWEHook;

public class DragonDelEnd implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void RecibirDano(EntityDamageByEntityEvent event) {
		if(event.getEntityType().equals(EntityType.ENDER_DRAGON) & event.getEntity().getWorld().getName().equals(DragonCustom.worldName)) {
			EnderDragon mob = (EnderDragon) event.getEntity();
			//Bukkit.getConsoleSender().sendMessage("Vida: " + mob.getHealth() + "-" + event.getFinalDamage());;
			if(mob.getScoreboardTags().contains("Fase 1") & mob.getHealth()/mob.getMaxHealth() < 9.0/10) {
				mob.removeScoreboardTag("Fase 1");
				mob.addScoreboardTag("Fase 2");
				summonCirculo(mob.getWorld(),"Bob",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 2") & mob.getHealth()/mob.getMaxHealth() < 8.0/10) {
				mob.removeScoreboardTag("Fase 2");
				mob.addScoreboardTag("Fase 3");
				summonCirculo(mob.getWorld(),"Rich",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 3") & mob.getHealth()/mob.getMaxHealth() < 7.0/10) {
				mob.removeScoreboardTag("Fase 3");
				mob.addScoreboardTag("Fase 4");
				summonCirculo(mob.getWorld(),"Bob",true,0);
				summonCirculo(mob.getWorld(),"Rich",false,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 4") & mob.getHealth()/mob.getMaxHealth() < 6.0/10) {
				mob.removeScoreboardTag("Fase 4");
				mob.addScoreboardTag("Fase 5");
				summonCirculo(mob.getWorld(),"Bob",true,0);
				summonCirculo(mob.getWorld(),"Rich",false,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 5") & mob.getHealth()/mob.getMaxHealth() < 5.0/10) {
				mob.removeScoreboardTag("Fase 5");
				mob.addScoreboardTag("Fase 6");
				ataques(4, mob.getWorld());
			}
			else if(mob.getScoreboardTags().contains("Fase 6") & mob.getHealth()/mob.getMaxHealth() < 4.0/10) {
				mob.removeScoreboardTag("Fase 6");
				mob.addScoreboardTag("Fase 7");
				summonCirculo(mob.getWorld(),"InvocarPhantomBob",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 7") & mob.getHealth()/mob.getMaxHealth() < 3.0/10) {
				mob.removeScoreboardTag("Fase 7");
				mob.addScoreboardTag("Fase 8");
				summonCirculo(mob.getWorld(),"InvocarPhantomRich",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 8") & mob.getHealth()/mob.getMaxHealth() < 2.0/10) {
				mob.removeScoreboardTag("Fase 8");
				mob.addScoreboardTag("Fase 9");
				summonCirculo(mob.getWorld(),"InvocarPhantomRich",false,0);
				summonCirculo(mob.getWorld(),"InvocarPhantomBob",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 9") & mob.getHealth()/mob.getMaxHealth() < 1.0/10) {
				mob.removeScoreboardTag("Fase 9");
				mob.addScoreboardTag("Fase 10");
				summonCirculo(mob.getWorld(),"Bob",true,-2);
				summonCirculo(mob.getWorld(),"Rich",false,0);
				ataques(4, mob.getWorld());
				arreglarBossBar(mob);
				mob.setMaxHealth(600);
				mob.setHealth(mob.getHealth()+300);
			}
			if(mob.getScoreboardTags().contains("Fase 6")) {
				Random rand = new Random();
				int azar = rand.nextInt(6);
				if(azar == 0) {
					InvocarPhantom(mob.getLocation().clone().add(0,5,0),18);
				}
				else if(azar == 1) {
					InvocarGhast(mob.getLocation().clone().add(0,5,0));
				}
				else if(azar == 2) {
					InvocarRich(mob.getLocation().clone().add(0,5,0));
				}
				else if(azar == 3) {
					InvocarBob(mob.getLocation().clone().add(0,5,0));
				}
				else if(azar == 4) {
					InvocarPhantomBob(mob.getLocation().clone().add(0,5,0));
				}
				else if(azar == 5) {
					InvocarPhantomRich(mob.getLocation().clone().add(0,5,0));
				}
			}
		}
		if(event.getDamager().getType().equals(EntityType.FIREBALL)) {
			Fireball fireball = (Fireball) event.getDamager();
			if(fireball.getShooter() instanceof Ghast & !hasBeenPreviouslyDragonKilled() & fireball.getWorld().getName().equals(DragonCustom.worldName))
				if(event.getEntity() instanceof LivingEntity mob) {
					event.setDamage(event.getDamage()*6);
					mob.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,100,10));
				}
		}
		else if(event.getDamager().getType().equals(EntityType.ARROW) & event.getEntity().getScoreboardTags().contains("BatallaDragon")) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Skeleton esqueleto) {
				if(esqueleto.getScoreboardTags().contains("BatallaDragon"))
					event.setCancelled(true);
			}
		}
		if (event.getDamager() instanceof Fireball fb){
			if (fb.getScoreboardTags().contains("inferno") && event.getEntity() instanceof Player){
				event.setDamage(5000.0);
			}
		}
		if(event.getDamager().getScoreboardTags().contains("BatallaDragon"))
			if(event.getEntity().getScoreboardTags().contains("BatallaDragon"))
				event.setCancelled(true);
	}
	
	private static void InvocarPhantomRich(Location cords) {
		Phantom phantom = InvocarPhantom(cords, 1);
		Skeleton skeleton = InvocarRich(cords);
		phantom.addPassenger(skeleton);
	}
	
	private static void InvocarPhantomBob(Location cords) {
		Phantom phantom = InvocarPhantom(cords, 1);
		WitherSkeleton witherSkeleton = InvocarBob(cords);
		phantom.addPassenger(witherSkeleton);
	}
	
	
	@SuppressWarnings("deprecation")
	private static Phantom InvocarPhantom(Location cords, int size) {
		Phantom phantom = (Phantom) cords.getWorld().spawnEntity(cords, EntityType.PHANTOM);
		phantom.addScoreboardTag("BatallaDragon");
		CargarEntidades.AutoTrack(phantom);
		phantom.setPersistent(true);
		phantom.setRemoveWhenFarAway(false);
		int res = 80;
		phantom.setMaxHealth(res);
		phantom.setHealth(res);
		phantom.setSize(size);
		return phantom;
	}
	
	@SuppressWarnings("deprecation")
	private static void InvocarGhast(Location cords) {
		Ghast ghast = (Ghast) cords.getWorld().spawnEntity(cords, EntityType.GHAST);
		ghast.addScoreboardTag("BatallaDragon");
		ghast.setPersistent(true);
		ghast.setRemoveWhenFarAway(false);
		ghast.setMaxHealth(80);
		ghast.setHealth(80);
	}
	
	
	private static Skeleton InvocarRich(Location cords) {
		Skeleton mob = (Skeleton) cords.getWorld().spawnEntity(cords, EntityType.SKELETON);
		MobSpawning.Rich2012(mob);
		mob.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,5));
		mob.setGravity(false);
		mob.setPersistent(true);
		mob.setRemoveWhenFarAway(false);
		mob.addScoreboardTag("WitherSkeletonMinionWitherBoss");
		mob.addScoreboardTag("BatallaDragon");
		new BukkitRunnable() {
			@Override
			public void run() {
				mob.setAI(true);
				CargarEntidades.AutoTrack(mob);
			}
		}.runTaskLater(DragonCustom.plugin, 20*8);
		WitherBoss.SkeletonMinionWitherBoss(mob);
		return mob;
	}
	
	private static WitherSkeleton InvocarBob(Location cords) {
		WitherSkeleton mob = (WitherSkeleton) cords.getWorld().spawnEntity(cords, EntityType.WITHER_SKELETON);
		MobSpawning.Bob(mob);
		mob.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,5));
		mob.setPersistent(true);
		mob.setRemoveWhenFarAway(false);
		mob.setAI(false);
		mob.addScoreboardTag("WitherSkeletonMinionWitherBoss");
		mob.addScoreboardTag("BatallaDragon");
		new BukkitRunnable() {
			@Override
			public void run() {
				mob.setAI(true);
				CargarEntidades.AutoTrack(mob);
			}
		}.runTaskLater(DragonCustom.plugin, 20*8);
		return mob;
	}
	
	@EventHandler
    public void SpawneoDeMobs(CreatureSpawnEvent event) {
		LivingEntity entidad = event.getEntity();
		if(entidad instanceof Enderman & entidad.getWorld().getName().equals(DragonCustom.worldName)) {
			if (!hasBeenPreviouslyDragonKilled()) {
				entidad.addScoreboardTag("BatallaDragon");
				entidad.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,1));
			}
		}
		else if( entidad instanceof EnderDragon & entidad.getWorld().getName().equals(DragonCustom.worldName)) {
			FixearDragon((EnderDragon) entidad);
		}
	} 
	
	@EventHandler
    public void EntidadMuerta(EntityDeathEvent event) {
		if(event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
			for(Entity entidad : event.getEntity().getWorld().getEntities()) {
				if(entidad.getScoreboardTags().contains("BatallaDragon")) {
					if(entidad instanceof LivingEntity entidadVida) {
						entidadVida.setHealth(0);
					}
				}
			}
		}
	}
	
	 @EventHandler
		public void NoUsarDurabilidad(PlayerItemDamageEvent event) {
	    	if(event.getPlayer().getWorld().getName().equals(DragonCustom.worldName) & !hasBeenPreviouslyDragonKilled()) {
	    		event.setCancelled(true);
	    	}
	    }
	

	
	
	private boolean hasBeenPreviouslyDragonKilled() {
		World end = Bukkit.getServer().getWorld(DragonCustom.worldName);
    	if(end !=null) {
	    	DragonBattle batalla = end.getEnderDragonBattle();
			return true;
	    	//return batalla.hasBeenPreviouslyKilled();
    	}
    	Bukkit.getConsoleSender().sendMessage("No se ha encontrado el mundo");
	    return true;
		
	}
	
	@SuppressWarnings("deprecation")
	public static void FixearDragon(EnderDragon dragon) {
		if(/*!dragon.getDragonBattle().hasBeenPreviouslyKilled() && !dragon.getScoreboardTags().contains("CambiarEnd")*/ true) {
			dragon.setMaxHealth(1200);
			dragon.setHealth(1200);
			dragon.addScoreboardTag("CambiarEnd");
			dragon.addScoreboardTag("Fase 1");
			dragon.addScoreboardTag("BatallaDragon");
			arreglarBossBar(dragon);
			autoAtaque(dragon);
			autoAtaque2(dragon);
			cambiarSuelo(dragon);
			cambiarPilar(dragon.getWorld());
		}
		else if(!dragon.getDragonBattle().hasBeenPreviouslyKilled()){
			arreglarBossBar(dragon);
			autoAtaque(dragon);
			autoAtaque2(dragon);
		}
	}

	private static void cambiarSuelo(EnderDragon dragon) {
		if(!dragon.getDragonBattle().hasBeenPreviouslyKilled()) {
			World end = dragon.getWorld();
			if (!DependencyManager.WorldEdit){
				int x, y, z;
				for(x=-200;x<=200;x++) {
					for(y=0;y<=255;y++) {
						for( z=-200;z<=200;z++) {
							Location cord = new Location(end,x,y,z);
							if(cord.getBlock().getType().equals(Material.END_STONE)) {
								Random rand = new Random(); //instance of random class
								int int_random = rand.nextInt(2);
								if(int_random == 0) {
									cord.getBlock().setType(Material.END_STONE_BRICKS);
								}
							}
						}
					}
				}
			}else {
				Location loc1 = new Location(end, -300, 0, -300);
				Location loc2 = new Location(end, 300, 255, 300);
				FAWEHook.replaceBlocks(loc1, loc2, end, List.of(Material.END_STONE), List.of(Material.END_STONE, Material.END_STONE_BRICKS));
			}
			saveEnderCristalsToConfig(dragon);
		}
	}

	private static void cambiarPilar(World end) {
		if (!DependencyManager.WorldEdit){
			int x, y, z;
			for(x=-200;x<=200;x++) {
				for(y=0;y<=255;y++) {
					for( z=-200;z<=200;z++) {
						Location cord = new Location(end,x,y,z);
						if(cord.getBlock().getType().equals(Material.OBSIDIAN)) {
							cord.getBlock().setType(Material.BEDROCK);
						}
					}
				}
			}
		}else {
			Location loc1 = new Location(end, -300, 0, -300);
			Location loc2 = new Location(end, 300, 255, 300);
			FAWEHook.replaceBlocks(loc1, loc2, end, List.of(Material.OBSIDIAN), List.of(Material.BEDROCK));
		}
	}

	public static void saveEnderCristalsToConfig(Entity dragon){
		FileConfiguration config = DragonCustom.plugin.getConfig();
		List<Location> crystals = new ArrayList<>();
		for (Entity e : dragon.getNearbyEntities(50, 100, 50)) {
			if(e instanceof EnderCrystal) {
				crystals.add(e.getLocation());
			}
		}
		if(crystals == null || crystals.size() != 10) return;
		config.set("Crystals.World", crystals.get(0).getWorld().getName());
		for(int i=0;i<crystals.size();i++) {
			int i2 = i+1;
			config.set("Crystals.Cristal "+i2+".X", crystals.get(i).getX());
			config.set("Crystals.Cristal "+i2+".Y", crystals.get(i).getY());
			config.set("Crystals.Cristal "+i2+".Z", crystals.get(i).getZ());
		}
		DragonCustom.plugin.saveConfig();
	}

	public static List<Location> enderCristalsFromConfig(){
		FileConfiguration config = DragonCustom.plugin.getConfig();
		if (config.get("Crystals.World") == null) return null;
		List<Location> crystals = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			crystals.add(new Location(Bukkit.getWorld(config.getString("Crystals.World")), config.getDouble("Crystals.Cristal "+i+".X"), config.getDouble("Crystals.Cristal "+i+".Y"), config.getDouble("Crystals.Cristal "+i+".Z")));
		}
		return crystals;
	}
	
	private static void arreglarBossBar(EnderDragon dragon) {
		BossBar bar = dragon.getDragonBattle().getBossBar();
		dragon.setCustomName(ChatColor.translateAlternateColorCodes('&', "☠&5&lENDER&f☠ &c&k&la&6&lKING&c&k&la"));
		bar.setTitle(ChatColor.translateAlternateColorCodes('&', "☠&5&lENDER&f☠ &c&k&la&6&lKING&c&k&la"));
		//bar.setTitle(ChatColor.translateAlternateColorCodes('&', "&4&lPermaSpect &6&lDemon &k&mLite"));
		//bar.setTitle(ChatColor.translateAlternateColorCodes('&', "&a&ka&c&lPelans &4&l3D&a&ka")); 
		bar.removeFlag(BarFlag.CREATE_FOG);
		if(dragon.getScoreboardTags().contains("Fase 10")) {
			bar.setColor(BarColor.RED);
			bar.setStyle(BarStyle.SOLID);
		}
		else {
			bar.setStyle(BarStyle.SEGMENTED_10);
		}
		
	}
	
	@EventHandler
	public void ataquesArea(AreaEffectCloudApplyEvent event) {
		//Bukkit.getConsoleSender().sendMessage("Area effect");
			//event.setCancelled(true);
		for( Entity entidad : event.getAffectedEntities()) {
			if(entidad instanceof Player player) {
				if(event.getEntity().getScoreboardTags().contains("DragonBattleLevitar"))
					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*5,6));
				if(event.getEntity().getScoreboardTags().contains("DragonBattleInstantDamage"))
					player.damage(8);
				if (event.getEntity().getScoreboardTags().contains("DragonBattleQuemar"))
					player.setFireTicks(20*30);
			}
				}
		 //DragonBattleInstantDamage
	}
	
	public static void ataques(int ataque, World world) {
		//Location loc = new Location(world,);
		if(ataque == 1) { //Nube centro
			Location loc = world.getEnderDragonBattle().getEndPortalLocation();
			Integer[] strArray = {4, 5, 4, 3, 2};
		    List<Integer> radio = Arrays.asList(strArray);
		    loc.add(0.5,0,0.5);
			for(int i=0; i<5; i++) {
				AreaEffectCloud cloud =  (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
				cloud.addScoreboardTag("DragonBattleLevitar");
				cloud.setDuration(30*20);
				cloud.setDurationOnUse(0);
				cloud.setParticle(Particle.PORTAL);
				//cloud.setDurationOnUse(0);
				cloud.setRadius(radio.get(i));
				cloud.setWaitTime(20*3);
				cloud.setReapplicationDelay(20*5);
				cloud.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1, 1, true, true), true);
				loc.add(0,1,0);
			}
		}
		else if(ataque == 2) {
			Location loc = world.getEnderDragonBattle().getEndPortalLocation();
			for(Entity entidad : world.getNearbyEntities(loc, 300, 300, 300)) {
				if(entidad instanceof Player jugador) {
					if(jugador.getGameMode() == GameMode.SURVIVAL) {
						jugador.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20*5,0));
						
						AreaEffectCloud cloud =  (AreaEffectCloud) jugador.getWorld().spawnEntity(jugador.getLocation(), EntityType.AREA_EFFECT_CLOUD);
						cloud.addScoreboardTag("DragonBattleInstantDamage");
						cloud.setDuration(20*30);
						cloud.setDurationOnUse(0);
						cloud.setParticle(Particle.SMOKE_NORMAL);
						//cloud.setDurationOnUse(0);
						cloud.setRadius(3);
						cloud.setWaitTime(20*5);
						cloud.setReapplicationDelay(10); //0.5 segundos
						cloud.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1, 1, true, true), true);
					}
				}
			}
		}
		else if(ataque ==3) {
			Location loc = world.getEnderDragonBattle().getEndPortalLocation();
			for(Entity entidad : world.getNearbyEntities(loc, 300, 300, 300)) {
				if(entidad instanceof Player jugador) {
					if(jugador.getGameMode() == GameMode.SURVIVAL) {
						Endermite endermite =(Endermite) world.spawnEntity(jugador.getLocation(), EntityType.ENDERMITE);
						endermite.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*10,4));
					}
				}
			}
			
			loc.add(0,4,0);
			world.spawnEntity(loc, EntityType.ENDERMITE);
		}
		else if(ataque ==4) { //No es como tal un ataque, sino parte de una fase.
			for (Location loc : enderCristalsFromConfig()) {
				loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
			}
		}
		else if(ataque ==5) {
			summonCirculo(world,"TNT",false,0);
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
    public void onTnTExplode (EntityExplodeEvent e) {
		if(e.getEntity().getScoreboardTags().contains("BloquesConGravedad")) {
			double x;
	        double y;
	        double z;
	        Location eLoc;
	        if(e.getEntity() == null){
	            eLoc = e.getLocation();
	        } else {
	            eLoc = e.getEntity().getLocation();
	        }
	        World w = eLoc.getWorld();
	        for (int i = 0; i < e.blockList().size();i++){
	            Block b = e.blockList().get(i);
	            Location bLoc = b.getLocation();
	            x = (bLoc.getX() - eLoc.getX())*1.5;
	            y = (bLoc.getY() - eLoc.getY())*1.5 + 2.5;
	            z = (bLoc.getZ() - eLoc.getZ())*1.5;
	            
	            FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(), b.getData());
	            fb.setDropItem(true);
	            fb.setVelocity(new Vector(x,y,z));
	            
	            b.setType(Material.AIR);
	        }
		}
    } 
	
	
	private static void summonCirculo(World world, String caso, boolean grande, int altura) {
		Location cord;
		if(caso.equals("TNT")) {
			cord = world.getEnderDragonBattle().getEnderDragon().getLocation();
		}
		else {
			cord = world.getEnderDragonBattle().getEndPortalLocation();
		}
		cord.setY(world.getEnderDragonBattle().getEndPortalLocation().getY()+14);
		List<Integer> listX, listZ;
		Integer[] cordX;
		Integer[] cordZ;
		if(!grande) {
			cordX = new Integer[]{0, 7, 10, 7, 0, -7, -10, -7};
			cordZ = new Integer[]{-9, -6, 0, 7, 10, 7, 0, -6};
		}
		else {
			cordX = new Integer[]{24, 12, -2, -24, -28, -22, -1, 12, 24, 29};
			cordZ = new Integer[]{15, 26, 28, 15, 0, -14, -28, -25, -15, 0};
		}
		listX = Arrays.asList(cordX);
		listZ = Arrays.asList(cordZ);
		Location copy = cord.clone().add(0, altura, 0);
    	for(int i=0;i<listX.size();i++) {
    		cord = copy.clone();
    		cord.setX(listX.get(i)+copy.getX());
    		cord.setZ(listZ.get(i)+copy.getZ());
			switch (caso) {
				case "Rich" -> InvocarRich(cord);
				case "Bob" -> InvocarBob(cord);
				case "TNT" -> {
					TNTPrimed tnt = (TNTPrimed) world.spawnEntity(cord, EntityType.PRIMED_TNT);
					tnt.setFuseTicks(20 * 5);
					tnt.addScoreboardTag("BloquesConGravedad");
				}
				case "InvocarPhantomRich" -> InvocarPhantomRich(cord);
				case "InvocarPhantomBob" -> InvocarPhantomBob(cord);
			}
    	}
	}
	
    @EventHandler
    public void ExplotarEnderCrystal(EntityDamageEvent event) {
    	if(event.getEntityType().equals(EntityType.ENDER_CRYSTAL) && event.getEntity().getWorld().getName().equals(DragonCustom.worldName)) {
    		DragonBattle batalla = event.getEntity().getWorld().getEnderDragonBattle();
    		if(/*!batalla.hasBeenPreviouslyKilled()*/ true) {
	    		EnderCrystal entidad = (org.bukkit.entity.EnderCrystal) event.getEntity();
	    		if(entidad.isShowingBottom()) {
		    		Random rand = new Random(); //instance of random class
		        	int int_random = rand.nextInt(4)+3;
		        	for(int i=0; i<int_random;i++) {
		        		Location cord = event.getEntity().getWorld().getEnderDragonBattle().getEndPortalLocation();
		        		cord.add(0,20,0);
		        		InvocarGhast(cord);
		        		InvocarPhantom(cord,18);
		        	}
	    		}
    		}
    	}
    }

    public static void autoAtaque(EnderDragon dragon) {
    	new BukkitRunnable() {
    		int ataqueJugadores = 45;
    		int ataqueCentro = 120;
    		int ataqueTNT = 30;
    		int ataqueEndermite = 90;
			@Override
			public void run() {
				if(dragon.isDead())
					this.cancel();
				boolean hayJugador = false;
				//Bukkit.getConsoleSender().sendMessage("Ataque de tnt en: " + ataqueTNT);
				for(Entity entidad :dragon.getNearbyEntities(300, 300, 300)) 
					if(entidad instanceof Player jugador) {
						hayJugador = true;
						if(jugador.getGameMode() == GameMode.SURVIVAL)
							hayJugador = true;
					}
				if(hayJugador) { //Si hay un jugador, realizar ataques
					Random rand = new Random();
					if(ataqueJugadores--==0) { //Ataque a jugadores
						ataques(2,dragon.getWorld());
						ataqueJugadores = 60 + rand.nextInt(30) - 14; 
					}
					if(ataqueCentro--==0) { //Ataque al centro
						ataques(1,dragon.getWorld());
						ataqueCentro = 120 + rand.nextInt(80) - 39; 
					}
					if(ataqueTNT--==0) { //Ataque de TNT
						ataques(5, dragon.getWorld());
						ataqueTNT = 30 + rand.nextInt(30) - 14; 
					}
					if(ataqueEndermite--==0) { //Spawnea Endermite
						ataques(3,dragon.getWorld());
						ataqueEndermite = 90 + rand.nextInt(60) - 29; 
					}
				}
			}
		}.runTaskTimer(DragonCustom.plugin, 20*15, 20);
    }

	public static void autoAtaque2(EnderDragon dragon) {
		new BukkitRunnable() {
			int ataqueLaser = 55;
			int ataqueBola = 120;
			int ataqueTNT = 30;
			int ataqueBuff = 90;
			int ataqueVolar = 20;
			int ataqueBolas = 40;
			boolean renderDra = false;
			int r = 1;
			@Override
			public void run() {
				if(dragon.isDead())
					this.cancel();
				boolean hayJugador = false;
				for(Entity entidad :dragon.getNearbyEntities(300, 300, 300))
					if(entidad instanceof Player) {
						hayJugador = true;
						break;
					}
				for(Entity ent : dragon.getNearbyEntities(300, 300, 300)) {
					if(renderDra) {
						break;
					}
					if(ent instanceof EnderDragon) {
						if(ent.getScoreboardTags().contains("Fase 10")) {
							renderDra = true;
							break;
						}
					}
				}
				if(renderDra) {
					r = 2;
				}
				if(hayJugador) { //Si hay un jugador, realizar ataques
					Random rand = new Random();
					if(ataqueLaser--==0) { //Lanza un laser que quita mucho
						MasAtaques.ataqueLaser(dragon);
						ataqueLaser = (60 + rand.nextInt(30) - 14)/r;
					}
					if(ataqueBola--==0) { //Lanza una bola de ghast al jugador
						MasAtaques.ataqueBola(dragon);
						ataqueBola = (120 + rand.nextInt(80) - 39)/r;
					}
					if(ataqueTNT--==0) { //Ataque de TNT
						MasAtaques.ataqueTNT(dragon);
						ataqueTNT = (30 + rand.nextInt(30) - 14)/r;
					}
					if(ataqueBuff--==0) { //Añade effectos de pocion a los mobs con la tag de Mystic_Mob
						MasAtaques.ataqueBuff(dragon.getWorld(), rand.nextInt(20) + 30, rand.nextInt(4) + 2 , rand.nextInt(3)+1);
						ataqueBuff = (90 + rand.nextInt(60) - 29)/r;
					}
					if(ataqueVolar--==0) { //Manda un laser a el jugador mas cercano y lo manda a volar
						MasAtaques.ataqueVolar(dragon);
						ataqueVolar = (20 + rand.nextInt(50) - 14)/r;
					}
					if (ataqueBolas--==0) {//lanza una bola de dragon por cada jugador 5 veces
						MasAtaques.ataqueBolas(dragon);
						ataqueBolas = (40 + rand.nextInt(60) - 14)/r;
					}
				}
			}
		}.runTaskTimer(DragonCustom.plugin, 2022, 20);
	}


	@SuppressWarnings("deprecation")
	public static class MasAtaques {

		public static void ataqueBolas(EnderDragon dragon){
			dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10, 1);
			Location LocN = dragon.getLocation();
			World w = dragon.getWorld();
			for(Entity ent : w.getNearbyEntities(LocN, 300, 300, 300)) {
				if(ent instanceof Player) {
					for(int i=0;i<5;i++) {
						new BukkitRunnable() {
							@Override
							public void run() {
								Location target = ent.getLocation();
								Location Loc = dragon.getLocation();
								Vector dragonV = new Vector(Loc.getX(), Loc.getY(), Loc.getZ());
								Vector targetV = new Vector(target.getX(), target.getY(), target.getZ());
								Vector vec = targetV.subtract(dragonV);
								DragonFireball fireball = (DragonFireball)w.spawnEntity(Loc, EntityType.DRAGON_FIREBALL);
								fireball.setDirection(vec);
							}
						}.runTaskLater(DragonCustom.plugin, i*10);

					}

				}
			}
		}

		public static void ataqueVolar(EnderDragon dragon){
			dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_SNOWBALL_THROW, SoundCategory.HOSTILE, 10, 0);
			Location dragonLoc = dragon.getLocation();
			World w = dragonLoc.getWorld();
			ArrayList<Location> targets = new ArrayList<>();
			assert w != null;

			for (Entity ent : w.getNearbyEntities(dragonLoc, 300, 300, 300)) {
				if (ent instanceof LivingEntity && ent.getType() == EntityType.PLAYER) {
					targets.add(ent.getLocation());
				}
			}

			for(Location loc : targets){
				new BukkitRunnable() {
					@Override
					public void run() {
						int R,G,B;
						R=0;
						G=255;
						B=0;
						if (loc == null) return;
						double d = loc.distance(dragonLoc);
						for (int i = 0; i < (int) d * 5; i++) {
							Color laserColor = Color.fromRGB(R, G, B);
							Location tempLoc = loc.clone();
							tempLoc.setX(dragonLoc.getX() + (i * ((loc.getX() - dragonLoc.getX()) / (d * 5))));
							tempLoc.setY(dragonLoc.getY() + (i * ((loc.getY() - dragonLoc.getY()) / (d * 5))));
							tempLoc.setZ(dragonLoc.getZ() + (i * ((loc.getZ() - dragonLoc.getZ()) / (d * 5))));

							Particulas.spawnParticles(w, Particle.REDSTONE, loc, 50, 1, new Particle.DustOptions(laserColor, 0.5f), false);

							if(laserColor.getGreen() >= 1) {
								int a;
								a = laserColor.getGreen();
								G = a-1;
							}
							for (Entity ent : w.getNearbyEntities(tempLoc, 0.5, 0.5, 0.5)) {
								if (ent instanceof LivingEntity && ent != dragon) {
									Vector vec = new Vector(ent.getVelocity().getX(),5,ent.getVelocity().getZ());
									ent.setVelocity(vec);
								}
							}
						}
					}
				}.runTask(DragonCustom.plugin);

			}

		}

		public static void ataqueBuff(World world, int tiempo, int fuerza, int resistencia){
			Location center = new Location(world,0,world.getHighestBlockYAt(0, 0),0);
			world.playSound(center, Sound.BLOCK_BELL_RESONATE, SoundCategory.HOSTILE, 10, 0);
			new BukkitRunnable() {@Override public void run() {world.playSound(center, Sound.BLOCK_BELL_USE, SoundCategory.HOSTILE, 10, 0);}}.runTaskLater(DragonCustom.plugin, 80);
			for(Entity ent : world.getNearbyEntities(center, 300, 300, 300)) {
				if(ent.getScoreboardTags().contains("Mystic_Mob") && !ent.getScoreboardTags().contains("Mystic_Wither")) {
					try {
						LivingEntity mob = (LivingEntity)ent;
						mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, tiempo*20, fuerza, true, true, true));
						mob.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, tiempo*20, fuerza, true, true, true));
						mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, tiempo*20, 1, true, true, true));
						mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, tiempo*20, resistencia, true, true, true));
					}catch(Exception ignored) {

					}
				}
			}
		}

		public static void ataqueLaser(EnderDragon dragon){
			dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 10, 2);
			Location dragonLoc = dragon.getLocation();
			World w = dragonLoc.getWorld();
			ArrayList<Location> targets = new ArrayList<>();
			assert w != null;
			for (Entity ent : w.getNearbyEntities(dragonLoc, 300, 300, 300)) {
				if (ent instanceof LivingEntity && ent.getType() == EntityType.PLAYER) {
					targets.add(ent.getLocation());
				}
			}
			for(Location loc : targets){
				if (randomBoleamWithPlayers(w)){
					new BukkitRunnable() {
						@Override
						public void run() {
							int R,G,B;
							R = 112;
							G = 65;
							B = 144;
							if (loc == null) return;
							double d = loc.distance(dragonLoc);
							boolean b = false;
							for (int i = 0; i < (int) d * 5; i++) {
								Color laserColor = Color.fromRGB(R, G, B);
								Location tempLoc = loc.clone();
								tempLoc.setX(dragonLoc.getX() + (i * ((loc.getX() - dragonLoc.getX()) / (d * 5))));
								tempLoc.setY(dragonLoc.getY() + (i * ((loc.getY() - dragonLoc.getY()) / (d * 5))));
								tempLoc.setZ(dragonLoc.getZ() + (i * ((loc.getZ() - dragonLoc.getZ()) / (d * 5))));


								Particulas.spawnParticles(w, Particle.REDSTONE, loc, 50, 1, new Particle.DustOptions(laserColor, 0.5f), false);

								if(laserColor.getRed() <= 254) {
									int a;
									a = laserColor.getRed();
									R = a+1;
								}

								for (Entity ent : w.getNearbyEntities(tempLoc, 0.5, 0.5, 0.5)) {
									if (ent instanceof LivingEntity && ent != dragon) {
										if(!b) {
											((LivingEntity)ent).damage(25, dragon);
											b = true;
										}

									}
								}
								if(b) break;
							}
							TNTPrimed tnt = (TNTPrimed)loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
							tnt.getScoreboardTags().add("BloquesConGravedad");
							tnt.setFuseTicks(0);
						}
					}.runTask(DragonCustom.plugin);
				}
			}

		}

		public static void ataqueTNT(EnderDragon dragon){ //mejorar
			dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_TNT_PRIMED, SoundCategory.HOSTILE, 10, 2);
			World world = dragon.getWorld();
			Location center = new Location(world,0,world.getHighestBlockYAt(0, 0),0);
			for(Entity ent : world.getNearbyEntities(center, 300, 300, 300)) {
				if(ent instanceof Player) {
					Location tempLoc = ent.getLocation();
					tempLoc.setY(tempLoc.getY()+15);
					TNTPrimed tnt = (TNTPrimed)world.spawnEntity(tempLoc, EntityType.PRIMED_TNT);
					tnt.getScoreboardTags().add("BloquesConGravedad");
					tnt.setFuseTicks(30);
					tnt.setVelocity(new Vector(0,-0.5,0));
				}
			}
		}

		public static void ataqueBola(EnderDragon dragon){ //mejorar
			dragon.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10, 1);
			Location dragonLoc = dragon.getLocation();
			World w = dragonLoc.getWorld();
			ArrayList<Location> targets = new ArrayList<>();
			assert w != null;
			for (Entity ent : w.getNearbyEntities(dragonLoc, 300, 300, 300)) {
				if (ent instanceof LivingEntity && ent.getType() == EntityType.PLAYER) {
					targets.add(ent.getLocation());
				}
			}

			Scoreboard test = DragonCustom.plugin.getServer().getScoreboardManager().getMainScoreboard();
			Team teamColor = null;
			teamColor = test.getTeam("Inferno_Ball");
			if (teamColor == null){
				teamColor = test.registerNewTeam("Inferno_Ball");
				teamColor.color(NamedTextColor.DARK_RED);
			}
			for(Location target : targets){
				Team finalTeamColor = teamColor;
				new BukkitRunnable() {
					@Override
					public void run() {
						Location dragonLoc2 = new Location(dragonLoc.getWorld(),dragonLoc.getX(),dragonLoc.getY(),dragonLoc.getZ());
						Fireball fireball = (Fireball) dragon.getWorld().spawnEntity(dragonLoc2, EntityType.FIREBALL);
						Vector dragonV = new Vector(dragonLoc.getX(), dragonLoc.getY(), dragonLoc.getZ());
						Vector targetV = new Vector(target.getX(), target.getY(), target.getZ());
						Vector vec = targetV.subtract(dragonV);
						fireball.setGlowing(true);
						fireball.setDirection(vec);
						fireball.getScoreboardTags().add("inferno");
						finalTeamColor.addEntity(fireball);
					}
				}.runTask(DragonCustom.plugin);
			}

		}

		public static void ataqueLluvia(EnderDragon dragon){
			World w = dragon.getWorld();
			Location l = new Location(w, 0, 90, 0);
			List<Location> Ll = new ArrayList<>();
			Ll.add(new Location(w, 0.5, 64, 15.5));
			Ll.add(new Location(w, 15.5, 64, 0.5));
			Ll.add(new Location(w, -14.5, 64, 0.5));
			Ll.add(new Location(w, 0.5, 64, -14.5));
			new BukkitRunnable() {
				double porcentaje = 0;
				@Override
				public void run() {
					for(Location tempL : Ll) {
						Particulas.pintarLinea(tempL, l, porcentaje);
					}
					if(porcentaje >= 1) {
						ataqueContinuacion_AtaqueLluvia(l);
						this.cancel();
					}
					Bukkit.broadcastMessage(String.valueOf(porcentaje));
					porcentaje += 0.03;
				}
			}.runTaskTimer(DragonCustom.plugin, 0, 5);
		}

		public static void ataqueContinuacion_AtaqueLluvia(Location l) { //mejorar
			l.getWorld().playSound(l, Sound.WEATHER_RAIN, SoundCategory.HOSTILE, 10, 0);
			int x,z;
			for(x=-200;x<=200;x++) {
				for( z=-200;z<=200;z++) {
					Location cord = new Location(l.getWorld(),x,l.getY(),z);
					if(cord.getBlock().getType().equals(Material.AIR)) {
						Random rand = new Random(); //instance of random class
						int int_random = rand.nextInt(6);
						if(int_random == 0) {
							FallingBlock fb = cord.getWorld().spawnFallingBlock(cord, Material.COBBLESTONE.createBlockData());
							fb.setHurtEntities(true);
							fb.setDropItem(false);
							fb.getScoreboardTags().add("lluvia_Block");
						}
					}
				}
			}
		}

		private static Boolean randomBoleamWithPlayers(World w){
			int cantidadJugadores = w.getPlayers().size();
			if(cantidadJugadores >= 5){
				Random rand = new Random();
				return rand.nextBoolean();
			}else return true;
		}
	}
}
