package pelans.dragon;



import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import pelans.DragonCustom;


public class WitherBoss {
	
	public static void SkeletonMinionWitherBoss(Mob mob) {
		if(!mob.getScoreboardTags().contains("SkeletonMinionWitherBoss"))
			mob.addScoreboardTag("SkeletonMinionWitherBoss");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(mob.isDead()) {
					this.cancel();
				}
				if(mob.hasGravity()) {
					mob.setGravity(false);
					PotionEffect pocion = new PotionEffect(PotionEffectType.LEVITATION,4*20,0);
					mob.addPotionEffect(pocion);
				}
				else {
					mob.setGravity(true);
				}
			}
		}.runTaskTimer(DragonCustom.plugin, 20*10, 20*10);
	}

}
