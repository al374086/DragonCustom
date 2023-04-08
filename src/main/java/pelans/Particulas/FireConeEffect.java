package pelans.Particulas;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireConeEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.FLAME;

    /**
     * Growing per iteration in the length (0.05)
     */
    public float lengthGrow = .05f;

    /**
     * Radials per iteration to spawn the next particle (PI / 16)
     */
    public double angularVelocity = Math.PI / 16;

    /**
     * Cone-particles per interation (10)
     */
    public int particles = 10;

    /**
     * Growth in blocks per iteration on the radius (0.006)
     */
    public float radiusGrow = 0.006f;

    /**
     * Conesize in particles per cone
     */
    public int particlesCone = 180;

    /**
     * Start-angle or rotation of the cone
     */
    public double rotation = 0;

    /**
     * Randomize every cone on creation (false)
     */
    public boolean randomize = false;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    protected boolean frist = false;

    public FireConeEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 200;
        frist = true;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int x = 0; x < particles; x++) {

            if (step > particlesCone) {
                step = 0;
                frist = false;
            }
            if (randomize && step == 0) rotation = RandomUtils.getRandomAngle();

            double angle = step * angularVelocity + rotation;
            float radius = step * radiusGrow;
            float length = step * lengthGrow;
            Vector v = new Vector(Math.cos(angle) * radius, length, Math.sin(angle) * radius);
            VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
            VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

            location.add(v);
            display(particle, location);
            if (frist){
                if (!location.getBlock().getType().equals(Material.AIR) && !location.getBlock().getType().equals(Material.FIRE)) {
                    if (!location.getWorld().getHighestBlockAt(location).getRelative(0, 1, 0).getType().equals(Material.FIRE)){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                location.getWorld().getHighestBlockAt(location).getRelative(0, 1, 0).setType(Material.FIRE);
                            }
                        }.runTask(effectManager.getOwningPlugin());
                    }
                }
            }
            location.subtract(v);
            step++;
        }
    }
}
