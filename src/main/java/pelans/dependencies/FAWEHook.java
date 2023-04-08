package pelans.dependencies;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import pelans.DragonCustom;

import java.util.ArrayList;
import java.util.List;

public class FAWEHook {

    public static void replaceBlocks(Location pos1, Location pos2, World w, List<Material> maskBlocks, List<Material> patterns) {
        new BukkitRunnable() {
            @Override
            public void run() {
                com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(w);
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                    List<BlockType> maskTypes = new ArrayList<>();
                    for (Material block: maskBlocks) maskTypes.add(BukkitAdapter.asBlockType(block));
                    List<BlockType> patternsTypes = new ArrayList<>();
                    for (Material block: patterns) patternsTypes.add(BukkitAdapter.asBlockType(block));
                    int chance = 100/patternsTypes.size();
                    BlockVector3 poss1 = BukkitAdapter.asBlockVector(pos1);
                    BlockVector3 poss2 = BukkitAdapter.asBlockVector(pos2);
                    CuboidRegion cuboidRegion = new CuboidRegion(world, poss1, poss2);
                    Mask mask = new BlockTypeMask(editSession.extent, maskTypes);
                    RandomPattern pattern = new RandomPattern();
                    for (BlockType block: patternsTypes) pattern.add(block, chance);
                    editSession.replaceBlocks(cuboidRegion, mask, pattern);
                }
            }
        }.runTaskAsynchronously(DragonCustom.plugin);
    }
}
