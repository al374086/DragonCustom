package pelans.commands;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.DynamicLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pelans.DragonCustom;
import pelans.dragon.Ataque;
import pelans.dragon.DragonDelEnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p){
            if (!p.isOp()) return true;
            if (args.length == 0) {
                p.sendMessage("Espefifica algo cabron.");
                return true;
            }
            if (args[0].equalsIgnoreCase("summon")){
                EnderDragon dragon = (EnderDragon) p.getWorld().spawnEntity(p.getLocation(), EntityType.ENDER_DRAGON);
                return true;
            }
            else if(args[0].equalsIgnoreCase("congelar")){
                boolean a = false;
                for(Entity ent : p.getWorld().getNearbyEntities(p.getLocation(), 300, 300, 300)) {
                    if (ent.getType().equals(EntityType.ENDERMAN) && !a) {
                        Ataque.congelar((LivingEntity) ent);
                        a = true;
                    }
                }

                return true;
            }else if (args[0].equalsIgnoreCase("test2")){
                Location loc = p.getEyeLocation().add(p.getEyeLocation().toVector().subtract(p.rayTraceBlocks(2000).getHitPosition()).multiply(-1));
                for (int i = 1; i <= 3; i++){
                    AreaEffectCloud cloud = (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
                    cloud.addScoreboardTag("DragonBattleLevitar");
                    cloud.setDuration(30 * 20);
                    cloud.setDurationOnUse(0);
                    cloud.setParticle(Particle.FLAME);
                    //cloud.setDurationOnUse(0);
                    cloud.setRadius(i);
                    cloud.setWaitTime(0);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1, 1, false, false), true);
                    loc.add(i, -1, 0);
                }
                return true;
            }else if (args[0].equalsIgnoreCase("test3")){
                Location loc = p.getEyeLocation().add(p.getEyeLocation().toVector().subtract(p.rayTraceBlocks(2000).getHitPosition()).multiply(-1));
                float yaw = (p.getLocation().getYaw());
                float pich = 57f;
                // Convertir yaw a radianes
                double yaw_rad = yaw * Math.PI / 180;

                // Calcular la diferencia en las coordenadas X y Z
                double x_diff = Math.sin(yaw_rad)*-1;
                double z_diff = Math.cos(yaw_rad);
                for (int i = 1; i <= 30; i++){
                    AreaEffectCloud cloud = (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
                    cloud.addScoreboardTag("DragonBattleLevitar");
                    cloud.setDuration(15 * 20);
                    cloud.setDurationOnUse(0);
                    cloud.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
                    //cloud.setParticle(Particle.FLAME);
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
                Location Origin = p.getEyeLocation().add(p.getEyeLocation().toVector().subtract(p.rayTraceBlocks(2000).getHitPosition()).multiply(-1));
                Origin.setPitch(pich);
                List<ConeEffect> cones = new ArrayList<>();
                for (int i = 1; i <= 10; i++){
                    ConeEffect cone = new ConeEffect(new EffectManager(DragonCustom.plugin));
                    cone.particle = Particle.FLAME;
                    cone.angularVelocity = 10;
                    cone.arrivalTime = 0;
                    cone.radiusGrow = i*0.001f;
                    cone.particles = 100;
                    cone.particlesCone = 1000;
                    cone.visibleRange = 100;
                    cone.duration = 15000;
                    cone.setDynamicOrigin(new DynamicLocation(Origin));
                    cone.setDynamicTarget(new DynamicLocation(loc.clone().add(0,-5,0)));
                    cones.add(cone);
                }
                cones.forEach(Effect::start);
                return true;
            }

            EnderDragon dragon = null;
            for(Entity ent : p.getWorld().getNearbyEntities(p.getLocation(), 300, 300, 300)) {
                if (ent.getType().equals(EntityType.ENDER_DRAGON)) {
                        dragon = (EnderDragon) ent;
                        break;
                }
            }
            if(dragon == null) {
                p.sendMessage("No se encontro un dragon en un radio de 300 bloques.");
                return true;
            }
            if(args[0].equalsIgnoreCase("Laser")) {
                DragonDelEnd.MasAtaques.ataqueLaser(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("Bola")) {
                DragonDelEnd.MasAtaques.ataqueBola(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("Buff")) {
                DragonDelEnd.MasAtaques.ataqueBuff(dragon.getWorld(), 60, 5, 3);
                return true;
            }else if(args[0].equalsIgnoreCase("Volar")) {
                DragonDelEnd.MasAtaques.ataqueVolar(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("Tnt")) {
                DragonDelEnd.MasAtaques.ataqueTNT(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("Bolas")) {
                DragonDelEnd.MasAtaques.ataqueBolas(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("lluvia")) {
                DragonDelEnd.MasAtaques.ataqueLluvia(dragon);
                return true;
            }else if(args[0].equalsIgnoreCase("test")){
                dragon.setPhase(EnderDragon.Phase.CHARGE_PLAYER);
                return true;
            }else if (args[0].equalsIgnoreCase("1")){
                DragonDelEnd.ataques(1, dragon.getWorld());
                return true;
            }else if (args[0].equalsIgnoreCase("2")){
                DragonDelEnd.ataques(2, dragon.getWorld());
                return true;
            }else if (args[0].equalsIgnoreCase("3")){
                DragonDelEnd.ataques(3, dragon.getWorld());
                return true;
            }else if (args[0].equalsIgnoreCase("4")){
                DragonDelEnd.ataques(4, dragon.getWorld());
                return true;
            }else if (args[0].equalsIgnoreCase("5")){
                DragonDelEnd.ataques(5, dragon.getWorld());
                return true;
            } else if (args[0].equalsIgnoreCase("lanzallamas")) {
                Ataque.lanzaLlamas(dragon);
                return true;
            }
        }else {
            Bukkit.getConsoleSender().sendMessage("No se puede ejecutar desde consola");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player p)  || !p.isOp())
            return null;
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("summon");
            list.add("laser");
            list.add("bola");
            list.add("buff");
            list.add("volar");
            list.add("tnt");
            list.add("bolas");
            list.add("lluvia");
            list.add("test");
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
            list.add("congelar");
            list.add("lanzallamas");
            list.add("test2");
        }
        list.removeIf(s -> !s.toLowerCase().startsWith(args[0].toLowerCase()));
        Collections.sort(list);
        return list;
    }
}
