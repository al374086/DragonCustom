package pelans.Util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import pelans.DragonCustom;

import java.util.Random;

public class Particulas {
    public static void animacionMuerteReal(Player player) {
        Location loc = player.getLocation();
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_DEATH, 10000, 1);
        new BukkitRunnable() {
            int timer=0;
            Boolean a=true;
            @Override
            public void run() {
                timer++;
                if(timer < 10*4) {
                    Location tempLoc = loc.clone();
                    spawnParticles(tempLoc.getWorld(), Particle.EXPLOSION_HUGE, tempLoc, 5, 1, 1, 1, 1, false);
                    spawnParticles(tempLoc.getWorld(), Particle.CAMPFIRE_SIGNAL_SMOKE, tempLoc, timer*5, timer, 1, 1, 1, false);
                }
                else {
                    if(a) {
                        a=false;
                        for( Player jugador : player.getWorld().getPlayers()) {
                            jugador.sendTitle(ChatColor.GOLD + "Enhorabuena!", ChatColor.RED + "Has matado al " + ChatColor.DARK_PURPLE + "Wither Storm", 10, 70, 20);
                        }
                    }
                }
                if(timer > 60*4) {
                    this.cancel();
                }
            }

        }.runTaskTimer(DragonCustom.plugin, 0, 5);
    }

    public static void animacionMuerte(Player player) {
        Location loc = player.getLocation();
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_DEATH, 10000, 1);
        new BukkitRunnable() {
            int timer=0;
            @Override
            public void run() {
                Location tempLoc = loc.clone();
                tempLoc.setY(tempLoc.getY()+2);
                spawnParticles(tempLoc.getWorld(), Particle.EXPLOSION_HUGE, tempLoc, 5, 1, 1, 1, 1, false);
                timer++;
                if(timer > 10*4) {
                    animacionMuerte2(player);
                    this.cancel();
                }
            }

        }.runTaskTimer(DragonCustom.plugin, 0, 5);
    }

    public static void animacionMuerte2(Player player) {
        new BukkitRunnable() {
            int timer=0;
            double radius=5;
            final Location loc = player.getLocation();
            @Override
            public void run() {
                double  o, phi;
                Color color;
                Sound sonido;
                if(timer < 10*4) {
                    color = Color.GREEN;
                    sonido = Sound.BLOCK_NOTE_BLOCK_BANJO;
                }
                else if(timer < 20*4) {
                    color = Color.YELLOW;
                    sonido = Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
                    //sonido = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
                }
                else {
                    color = Color.RED;
                    sonido = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
                    Location tempLoc = loc.clone();
                    tempLoc.setY(tempLoc.getY()+2);
                    spawnParticles(tempLoc.getWorld(), Particle.CAMPFIRE_SIGNAL_SMOKE, tempLoc, 200, 50, 1, 1, 1, false);
                }
                loc.getWorld().playSound(loc, sonido, 1000, 1);
                loc.getWorld().playSound(loc, sonido, 1000, 2);
                for(o=0; o<360; o+=1/radius*30) { //Esfera
                    for(phi=0; phi<180; phi+=1/radius*30) {
                        Location aux = calculateLoc(loc, o, phi, radius);
                        aux.setY(aux.getY()+2);
                        spawnParticles(aux.getWorld(), Particle.REDSTONE, aux, 1, 1, new Particle.DustOptions(color, 1), false);
                    }
                }
                timer++;
                radius *= 0.99;
                if(timer > 30*4) {
                    this.cancel();
                }
            }

        }.runTaskTimer(DragonCustom.plugin, 0, 5);
    }

    public static void animacionInvocar(Player player) {
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.MUSIC_DISC_11, 1000, 1);
        new BukkitRunnable() {
            final Location loc = player.getLocation();
            int timer = 0;
            double animacion = 0;
            @Override
            public void run() {
                animacion += 0.05;
                double x;
                double radius = 8;
                for(x=0; x<360 & x/360 < animacion/3; x+=1/radius*5) { //Circulo
                    Location aux = calculateLoc(loc, x, radius);
                    spawnParticles(aux.getWorld(), Particle.REDSTONE, aux, 1 ,1, new Particle.DustOptions(Color.RED, 1), false);
                }
                for(x=0; x<5; x++) { //Pentagrama
                    Location p0 = calculateLoc(loc, 72*(2*x), radius);
                    Location p1 = calculateLoc(loc, 72*(2*x+2), radius);
                    pintarLinea(p0,p1, animacion-1*(x+3));
                    pintarLinea(p1,p1.clone().add(0,2,0),Particle.FLAME, animacion-1*(x+4)); //llamas
                }
                Random rand = new Random();
                for(x=0; x<300 & x/300 < (animacion-1*(x+8))/3;x++) { //Almas
                    double radiusAux = rand.nextDouble()*radius;
                    double degree = rand.nextDouble()*360;
                    Location aux = calculateLoc(loc, degree, radiusAux);
                    aux.add(0,rand.nextDouble()*3,0);
                    spawnParticles(aux.getWorld(), Particle.SOUL, aux, 1, 1, 0, 0, 0, false);
                }
                if(animacion>11) {
                    Location tempLoc = loc.clone();
                    tempLoc.setY(tempLoc.getY() + 10);
                    spawnParticles(loc.getWorld(), Particle.EXPLOSION_HUGE, tempLoc, 5, 1, 1, 1, 1, false);
                }
                if(animacion > 8 & animacion < 11) {
                    loc.getWorld().playSound(loc, Sound.AMBIENT_CAVE, 1000, 0);

                }
                if(animacion > 11) {
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1000, 0);
                }


                timer++;
                if(timer > 60*4) {
                    for( Player jugador : loc.getWorld().getPlayers()) {
                        jugador.stopSound(Sound.MUSIC_DISC_11);
                    }
                    this.cancel();
                }

            }
        }.runTaskTimer(DragonCustom.plugin, 20*10, 0);
    }

    public static void particulas(Location loc) {
        new BukkitRunnable() {
            int timer = 0;
            @Override
            public void run() {
                double x;
                double radius = 8;
                for(x=0; x<360; x+=1/radius*5) { //Circulo
                    Location aux = calculateLoc(loc, x, radius);
                    spawnParticles(aux.getWorld(), Particle.REDSTONE, aux, 1 ,1, new Particle.DustOptions(Color.RED, 1), false);
                }
                for(x=0; x<5; x++) { //Pentagrama
                    Location p0 = calculateLoc(loc, 72*x, radius);
                    Location p1 = calculateLoc(loc, 72*(x-2), radius);
                    Location p2 = calculateLoc(loc, 72*(x+2), radius);
                    pintarLinea(p0,p1, 1);
                    pintarLinea(p0,p2, 1);
                    pintarLinea(p0,p0.clone().add(0,2,0),Particle.FLAME, 1); //llamas
                }
                for(x=0; x<30;x++) { //Almas
                    Random rand = new Random();
                    double radiusAux = rand.nextDouble()*radius;
                    double degree = rand.nextDouble()*360;
                    Location aux = calculateLoc(loc, degree, radiusAux);
                    aux.add(0,rand.nextDouble()*3,0);
                    spawnParticles(aux.getWorld(), Particle.SOUL, aux, 1, 1, 0, 0, 0, false);
                }
                Location tempLoc = loc.clone();
                tempLoc.setY(tempLoc.getY()+10);
                spawnParticles(loc.getWorld(), Particle.EXPLOSION_HUGE, tempLoc, 5, 1, 1, 1, 1, false);

                timer++;
                if(timer > 30*4)
                    this.cancel();			}
        }.runTaskTimer(DragonCustom.plugin, 0, 5);
    }

    public static Location calculateLoc(Location loc, double deg, double radius) {
        double rad = Math.toRadians(deg);
        double X = Math.cos(rad)*radius;
        double Z = Math.sin(rad)*radius;
        return loc.clone().add(X,0,Z);
    }

    public static Location calculateLoc(Location loc, double degO, double degPhi, double radius) {
        double radO = Math.toRadians(degO);
        double radPhi = Math.toRadians(degPhi);
        double X = Math.cos(radO)*Math.sin(radPhi)*radius;
        double Y = Math.cos(radPhi)*radius;
        double Z = Math.sin(radO)*Math.sin(radPhi)*radius;

        return loc.clone().add(X,Y,Z);
    }

    public static void pintarLinea(Location p0, Location p1, double porcentaje) {
        double x = p1.getX() - p0.getX();
        double y = p1.getY() - p0.getY();
        double z = p1.getZ() - p0.getZ();
        double distancia = Math.sqrt(x*x + y*y + z*z);
        double i=0;
        while(i < distancia & porcentaje > i/distancia) {
            Location loc = new Location(p0.getWorld(), p0.getX()+i/distancia*x, p0.getY()+i/distancia*y, p0.getZ()+i/distancia*z);
            spawnParticles(p0.getWorld(), Particle.REDSTONE, loc, 1, 1, new Particle.DustOptions(Color.RED, 1), false);
            i+=0.2;
        }
    }

    public static void pintarLinea(Location p0, Location p1, Particle particula, double porcentaje) {
        double x = p1.getX() - p0.getX();
        double y = p1.getY() - p0.getY();
        double z = p1.getZ() - p0.getZ();
        double distancia = Math.sqrt(x*x + y*y + z*z);
        double i=0;
        while(i < distancia & porcentaje > i/distancia) {
            Location loc = new Location(p0.getWorld(), p0.getX()+i/distancia*x, p0.getY()+i/distancia*y, p0.getZ()+i/distancia*z);
            spawnParticles(p0.getWorld(), particula, loc, 1, 1, null, false);
            i+=0.2;
        }
    }

    public static void spawnParticles(World w, Particle particle, Location loc, int high, int low, @Nullable Particle.DustOptions pd, boolean force){
        if (pd != null){
            w.spawnParticle(particle, loc, high, pd);
        }else {
            w.spawnParticle(particle, loc, high);
        }
    }

    public static void spawnParticles(World w, Particle particle, Location loc, int high, int low, int v3, int v4, int v5, boolean force){
        w.spawnParticle(particle, loc, high, v3, v4, v5);
    }

}
