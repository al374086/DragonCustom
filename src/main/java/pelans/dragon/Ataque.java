package pelans.dragon;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.DynamicLocation;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pelans.DragonCustom;
import pelans.Particulas.FireConeEffect;
import pelans.Util.Particulas;

import java.util.ArrayList;
import java.util.List;

public class Ataque {

    private record RestaurarData(BlockData data, Location loc) {}

    public static void congelar(LivingEntity e){
        e.getWorld().playSound(e.getLocation(), Sound.ENTITY_SNOWBALL_THROW, SoundCategory.HOSTILE, 10, 0);
        Location dragonLoc = e.getLocation();
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
                    G=0;
                    B=255;
                    if (loc == null) return;
                    boolean c = false;
                    double d = loc.distance(dragonLoc);
                    for (int i = 0; i < (int) d * 5; i++) {
                        Color laserColor = Color.fromRGB(R, G, B);
                        Location tempLoc = loc.clone();
                        tempLoc.setX(dragonLoc.getX() + (i * ((loc.getX() - dragonLoc.getX()) / (d * 5))));
                        tempLoc.setY(dragonLoc.getY() + (i * ((loc.getY() - dragonLoc.getY()) / (d * 5))));
                        tempLoc.setZ(dragonLoc.getZ() + (i * ((loc.getZ() - dragonLoc.getZ()) / (d * 5))));

                        Particulas.spawnParticles(w, Particle.REDSTONE, loc, 50, 1, new Particle.DustOptions(laserColor, 0.5f), false);

                        if(laserColor.getBlue() >= 1) {
                            int a;
                            a = laserColor.getBlue();
                            B = a-1;
                        }
                        for (Entity entity : w.getNearbyEntities(tempLoc, 0.5, 0.5, 0.5)) {
                            if (entity instanceof LivingEntity ent && entity != e) {
                                c = true;
                                ent.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*15, 150));
                                ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*15, 7));
                                ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*15, 2));
                                List<RestaurarData> blocks = new ArrayList<>();
                                Location loc = ent.getLocation().toBlockLocation().add(0, -2, 0);
                                for (int i2 = 0; i2 <= 6; i2++) {
                                    blocks.add(new RestaurarData(loc.getBlock().getBlockData(), loc.getBlock().getLocation()));
                                    if (i2 != 0 && i2 != 6){
                                        blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.EAST).getBlockData(), loc.getBlock().getRelative(BlockFace.EAST).getLocation()));
                                        blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.NORTH).getBlockData(), loc.getBlock().getRelative(BlockFace.NORTH).getLocation()));
                                        blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.SOUTH).getBlockData(), loc.getBlock().getRelative(BlockFace.SOUTH).getLocation()));
                                        blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.WEST).getBlockData(), loc.getBlock().getRelative(BlockFace.WEST).getLocation()));
                                        if (i2 != 1 && i2 != 5){
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getBlockData(), loc.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getBlockData(), loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getBlockData(), loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getBlockData(), loc.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getBlockData(), loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getBlockData(), loc.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getBlockData(), loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation()));
                                            blocks.add(new RestaurarData(loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getBlockData(), loc.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation()));
                                        }
                                    }
                                    loc = loc.getBlock().getRelative(BlockFace.UP).getLocation();
                                }
                                for (RestaurarData b : blocks) {
                                    b.loc().getBlock().setType(Material.PACKED_ICE);
                                }
                                ent.setVelocity(ent.getVelocity().multiply(-1));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (RestaurarData b : blocks) {
                                            b.loc().getBlock().setBlockData(b.data());
                                        }
                                    }
                                }.runTaskLater(DragonCustom.plugin, 20*15);
                            }
                        }
                        if (c) break;
                    }
                }
            }.runTask(DragonCustom.plugin);
        }
    }

    public static void lanzaLlamas(EnderDragon dragon){
        ComplexEntityPart head = dragon.getParts().stream().toList().get(0);
        ComplexEntityPart neck = dragon.getParts().stream().toList().get(1);

        Vector vector = head.getLocation().clone().toVector().subtract(neck.getLocation().toVector()).multiply(10);
        Location headLoc = head.getLocation();
        headLoc.setDirection(vector);
        Location loc = headLoc.clone();

        float yaw = (headLoc.getYaw());
        float pich = 57f;
        // Convertir yaw a radianes
        double yaw_rad = yaw * Math.PI / 180;

        // Calcular la diferencia en las coordenadas X y Z
        double x_diff = Math.sin(yaw_rad)*-1;
        double z_diff = Math.cos(yaw_rad);
        for (int i = 1; i <= 30; i++){
            AreaEffectCloud cloud = (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
            cloud.addScoreboardTag("DragonBattleQuemar"); //cambiar y programar
            cloud.setDuration(3 * 20);
            cloud.setDurationOnUse(0);
            cloud.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
            //cloud.setDurationOnUse(0);
            int a = i/4;
            if (a >= 6){
                a = 6;
            }
            cloud.setRadius(a);
            cloud.setWaitTime(0);
            cloud.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1, 1, false, false), true);
            loc.add(1*x_diff, -1.5, 1*z_diff);
        }
        Location Origin = headLoc.clone();
        Origin.setPitch(pich);
        List<FireConeEffect> cones = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            FireConeEffect cone = new FireConeEffect(new EffectManager(DragonCustom.plugin));
            cone.particle = Particle.FLAME;
            cone.angularVelocity = 10;
            cone.arrivalTime = 0;
            cone.radiusGrow = i*0.001f;
            cone.particles = 100;
            cone.particlesCone = 1000;
            cone.visibleRange = 100;
            cone.duration = 3000;
            cone.setDynamicOrigin(new DynamicLocation(Origin));
            cone.setDynamicTarget(new DynamicLocation(loc.clone().add(0,-5,0)));
            cones.add(cone);
        }
        cones.forEach(Effect::start);
    }
}
