package pelans.dragon;


import org.bukkit.GameMode;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import pelans.DragonCustom;

public class CargarEntidades implements Listener  {
	
	@EventHandler
	public void cargar(EntitiesLoadEvent event) {
		for (Entity entidad : event.getEntities()) {
			analizarEntidad(entidad);
		}
		
	}
	
	public static void analizarEntidad(Entity entidad) {

		
		if(entidad.getScoreboardTags().contains("AutoTrack")) {
			AutoTrack((Mob) entidad);
		}
		if(entidad.getScoreboardTags().contains("SkeletonMinionWitherBoss")) {
			WitherBoss.SkeletonMinionWitherBoss((Mob) entidad);
		}
		if(entidad.getScoreboardTags().contains("WitherSkeletonMinionWitherBoss")) {
			((LivingEntity) entidad).setAI(true);
			AutoTrack((Mob) entidad);
		}
		//Dragon
		if(entidad instanceof EnderDragon) {
			EnderDragon dragon = (EnderDragon) entidad;
			DragonDelEnd.FixearDragon(dragon);
			DragonDelEnd.autoAtaque(dragon);
		} 
	}
	
	
	public static void AutoTrack(Mob mob) {
		if(!mob.getScoreboardTags().contains("AutoTrack"))
			mob.addScoreboardTag("AutoTrack");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(mob.isDead())
					this.cancel();
				if (mob.getTarget() == null)
					for(Entity entidad :mob.getNearbyEntities(50, 50, 50)) 
						if(entidad instanceof Player) {
							Player jugador = (Player) entidad;
							if(jugador.getGameMode() == GameMode.SURVIVAL) {
								if(mob.getTarget() == null)
									mob.setTarget(jugador);
								break;
							}
						}
			}
		}.runTaskTimer(DragonCustom.plugin, 0, 20*10);
	}

}
