package pelans.dragon;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkeleton;
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
import org.bukkit.util.Vector;

import pelans.DragonCustom;

import org.bukkit.entity.EnderCrystal;

public class DragonDelEnd implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void RecibirDaño(EntityDamageByEntityEvent event) {
		if(event.getEntityType().equals(EntityType.ENDER_DRAGON) & event.getEntity().getWorld().getName().equals(DragonCustom.worldName)) {
			EnderDragon mob = (EnderDragon) event.getEntity();
			//Bukkit.getConsoleSender().sendMessage("Vida: " + mob.getHealth() + "-" + event.getFinalDamage());;
			if(mob.getScoreboardTags().contains("Fase 1") & mob.getHealth()/mob.getMaxHealth() < 5.0/6) {
				mob.removeScoreboardTag("Fase 1");
				mob.addScoreboardTag("Fase 2");
				summonCirculo(mob.getWorld(),"Bob",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 2") & mob.getHealth()/mob.getMaxHealth() < 4.0/6) {
				mob.removeScoreboardTag("Fase 2");
				mob.addScoreboardTag("Fase 3");
				summonCirculo(mob.getWorld(),"Rich",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 3") & mob.getHealth()/mob.getMaxHealth() < 3.0/6) {
				mob.removeScoreboardTag("Fase 3");
				mob.addScoreboardTag("Fase 4");
				summonCirculo(mob.getWorld(),"EnderCrystal",true,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 4") & mob.getHealth()/mob.getMaxHealth() < 2.0/6) {
				mob.removeScoreboardTag("Fase 4");
				mob.addScoreboardTag("Fase 5");
				summonCirculo(mob.getWorld(),"Bob",true,0);
				summonCirculo(mob.getWorld(),"Rich",false,0);
			}
			else if(mob.getScoreboardTags().contains("Fase 5") & mob.getHealth()/mob.getMaxHealth() < 1.0/6) {
				mob.removeScoreboardTag("Fase 5");
				mob.addScoreboardTag("Fase 6");
				summonCirculo(mob.getWorld(),"Bob",true,-2);
				summonCirculo(mob.getWorld(),"Rich",false,0);
				summonCirculo(mob.getWorld(),"EnderCrystal",true,2);
				arreglarBossBar(mob);
				mob.setMaxHealth(300);
				mob.setHealth(mob.getHealth()+50);
			}
			if(mob.getScoreboardTags().contains("Fase 6")) {
				Random rand = new Random();
				int azar = rand.nextInt(4);
				if(azar == 0) {
					InvocarPhantom(mob.getLocation().clone().add(0,5,0));
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
			}
		}
		if(event.getDamager().getType().equals(EntityType.FIREBALL)) {
			Fireball fireball = (Fireball) event.getDamager();
			if(fireball.getShooter() instanceof Ghast & !hasBeenPreviouslyDragonKilled() & fireball.getWorld().getName().equals(DragonCustom.worldName))
				if(event.getEntity() instanceof LivingEntity) {
					event.setDamage(event.getDamage()*6);
					LivingEntity mob = (LivingEntity) event.getEntity();
					mob.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,100,10));
				}
		}
		else if(event.getDamager().getType().equals(EntityType.ARROW) & event.getEntity().getScoreboardTags().contains("BatallaDragon")) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Skeleton) {
				Skeleton esqueleto= (Skeleton) arrow.getShooter();
				if(esqueleto.getScoreboardTags().contains("BatallaDragon"))
					event.setCancelled(true);
			}
		}
		if(event.getDamager().getScoreboardTags().contains("BatallaDragon"))
			if(event.getEntity().getScoreboardTags().contains("BatallaDragon"))
				event.setCancelled(true);
	}
	
	
	@SuppressWarnings("deprecation")
	private static void InvocarPhantom(Location cords) {
		Phantom phantom = (Phantom) cords.getWorld().spawnEntity(cords, EntityType.PHANTOM);
		phantom.addScoreboardTag("BatallaDragon");
		CargarEntidades.AutoTrack(phantom);
		phantom.setPersistent(true);
		phantom.setRemoveWhenFarAway(false);
		int res = 80;
		phantom.setMaxHealth(res);
		phantom.setHealth(res);
		phantom.setSize(18);
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
	
	
	private static void InvocarRich(Location cords) {
		LivingEntity mob = (LivingEntity) cords.getWorld().spawnEntity(cords, EntityType.SKELETON);
		MobSpawning.Rich2012((Skeleton) mob);
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
				CargarEntidades.AutoTrack((Mob) mob);
			}
		}.runTaskLater(DragonCustom.plugin, 20*8);
		WitherBoss.SkeletonMinionWitherBoss((Mob) mob);
	}
	
	private static void InvocarBob(Location cords) {
		LivingEntity mob = (LivingEntity) cords.getWorld().spawnEntity(cords, EntityType.WITHER_SKELETON);
		MobSpawning.Bob((WitherSkeleton) mob);
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
				CargarEntidades.AutoTrack((Mob) mob);
			}
		}.runTaskLater(DragonCustom.plugin, 20*8);
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
					if(entidad instanceof LivingEntity) {
						LivingEntity entidadVida = (LivingEntity) entidad;
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
	    	return batalla.hasBeenPreviouslyKilled();
    	}
    	Bukkit.getConsoleSender().sendMessage("No se ha encontrado el mundo");
	    return true;
		
	}
	
	@SuppressWarnings("deprecation")
	public static void FixearDragon(EnderDragon dragon) {
		if(!dragon.getDragonBattle().hasBeenPreviouslyKilled() && !dragon.getScoreboardTags().contains("CambiarEnd")) {
			dragon.setMaxHealth(600);
			dragon.setHealth(600);
			dragon.addScoreboardTag("CambiarEnd");
			dragon.addScoreboardTag("Fase 1");
			dragon.addScoreboardTag("BatallaDragon");
			arreglarBossBar(dragon);
			autoAtaque(dragon);
			World end = dragon.getWorld();
			int x, y, z;
			for ( x=-200;x<=200;x++) {
				  for(y=0;y<=255;y++) {
					  for( z=-200;z<=200;z++) {
						  Location cord = new Location(end,x,y,z);
						  if(cord.getBlock().getType().equals(Material.OBSIDIAN)) {
							  cord.getBlock().setType(Material.BEDROCK);
						  }
						  else if(cord.getBlock().getType().equals(Material.END_STONE)) {
							  Random rand = new Random(); //instance of random class
							  int int_random = rand.nextInt(2);
							  if(int_random == 0) {
								  cord.getBlock().setType(Material.END_STONE_BRICKS);
							  }
						  }
					  }
				  }
			  }
		}
		else if(!dragon.getDragonBattle().hasBeenPreviouslyKilled()){
			arreglarBossBar(dragon);
			autoAtaque(dragon);
		}
	}
	
	private static void arreglarBossBar(EnderDragon dragon) {
		BossBar bar = dragon.getDragonBattle().getBossBar();
		bar.setTitle(ChatColor.translateAlternateColorCodes('&', "☠&5&lENDER&f☠ &c&k&la&6&lKING&c&k&la"));
		//bar.setTitle(ChatColor.translateAlternateColorCodes('&', "&4&lPermaSpect &6&lDemon &k&mLite"));
		//bar.setTitle(ChatColor.translateAlternateColorCodes('&', "&a&ka&c&lPelans &4&l3D&a&ka")); 
		bar.removeFlag(BarFlag.CREATE_FOG);
		if(dragon.getScoreboardTags().contains("Fase 6")) {
			bar.setColor(BarColor.RED);
			bar.setStyle(BarStyle.SOLID);
		}
		else {
			bar.setStyle(BarStyle.SEGMENTED_6);
		}
		
	}
	
	@EventHandler
	public void ataquesArea(AreaEffectCloudApplyEvent event) {
		//Bukkit.getConsoleSender().sendMessage("Area effect");
			//event.setCancelled(true);
		for( Entity entidad : event.getAffectedEntities()) {
			if(entidad instanceof Player) {
				if(event.getEntity().getScoreboardTags().contains("DragonBattleLevitar"))
					((Player) entidad).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*5,6));
				if(event.getEntity().getScoreboardTags().contains("DragonBattleInstantDamage"))
					((Player) entidad).damage(8);
				
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
				cloud.setBasePotionData(new PotionData(PotionType.LUCK));
				loc.add(0,1,0);
			}
		}
		else if(ataque == 2) {
			Location loc = world.getEnderDragonBattle().getEndPortalLocation();
			for(Entity entidad : world.getNearbyEntities(loc, 300, 300, 300)) {
				if(entidad instanceof Player) {
					Player jugador = (Player) entidad;
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
						cloud.setBasePotionData(new PotionData(PotionType.LUCK));
						//cloud.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE,false,true)); //Cuidado, afecta tambien a otros mobs!
					}
				}
			}
		}
		else if(ataque ==3) {
			Location loc = world.getEnderDragonBattle().getEndPortalLocation();
			for(Entity entidad : world.getNearbyEntities(loc, 300, 300, 300)) {
				if(entidad instanceof Player) {
					Player jugador = (Player) entidad;
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
			summonCirculo(world,"EnderCrystal",true,0);
		}
		else if(ataque ==5) {
			summonCirculo(world,"TNT",false,0);
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
    public void onTnTExplode (EntityExplodeEvent e) {
		if(e.getEntity().getScoreboardTags().contains("BloquesConGravedad")) {
			double x = 0;
	        double y = 0;
	        double z = 0;
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
	            
	            FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(), (byte)b.getData());
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
		if(!grande) {
			Integer[] cordX = {0,7,10,7,0,-7,-10,-7};
	    	Integer[] cordZ = {-9,-6,0,7,10,7,0,-6};
	    	listX = Arrays.asList(cordX);
	    	listZ = Arrays.asList(cordZ);
		}
		else {
			Integer[] cordX = {24,12,-2,-24,-28,-22,-1,12,24,29};
	    	Integer[] cordZ = {15,26,28,15,0,-14,-28,-25,-15,0};
	    	listX = Arrays.asList(cordX);
	    	listZ = Arrays.asList(cordZ);
		}
    	Location copy = cord.clone().add(0, altura, 0);
    	for(int i=0;i<listX.size();i++) {
    		cord = copy.clone();
    		cord.setX(listX.get(i)+copy.getX());
    		cord.setZ(listZ.get(i)+copy.getZ());
    		if(caso.equals("Rich"))
    			InvocarRich(cord);
    		else if(caso.equals("Bob"))
    			InvocarBob(cord);
    		else if(caso.equals("TNT")) {
    			TNTPrimed tnt = (TNTPrimed) world.spawnEntity(cord, EntityType.PRIMED_TNT);
        		tnt.setFuseTicks(20*5);
        		tnt.addScoreboardTag("BloquesConGravedad");
    		}
    		else if(caso.equals("EnderCrystal"))
    			world.spawnEntity(cord, EntityType.ENDER_CRYSTAL);
    	}
	}
	
    @EventHandler
    public void ExplotarEnderCrystal(EntityDamageEvent event) {
    	if(event.getEntityType().equals(EntityType.ENDER_CRYSTAL) && event.getEntity().getWorld().getName().equals(DragonCustom.worldName)) {
    		DragonBattle batalla = event.getEntity().getWorld().getEnderDragonBattle();
    		if(!batalla.hasBeenPreviouslyKilled()) {
	    		EnderCrystal entidad = (org.bukkit.entity.EnderCrystal) event.getEntity();
	    		if(entidad.isShowingBottom()) {
		    		Random rand = new Random(); //instance of random class
		        	int int_random = rand.nextInt(4)+3;
		        	for(int i=0; i<int_random;i++) {
		        		Location cord = event.getEntity().getWorld().getEnderDragonBattle().getEndPortalLocation();
		        		cord.add(0,20,0);
		        		InvocarGhast(cord);
		        		InvocarPhantom(cord);
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
					if(entidad instanceof Player) {
						Player jugador = (Player) entidad;
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
						summonCirculo(dragon.getWorld(),"TNT",false,0);
						ataqueTNT = 30 + rand.nextInt(30) - 14; 
					}
					if(ataqueEndermite--==0) { //Spawnea Endermite
						ataques(3,dragon.getWorld());
						ataqueEndermite = 90 + rand.nextInt(60) - 29; 
					}
				}
			}
		}.runTaskTimer(DragonCustom.plugin, 20*15, 20*1);
    }
	

}
