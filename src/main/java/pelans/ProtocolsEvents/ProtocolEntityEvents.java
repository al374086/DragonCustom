package pelans.ProtocolsEvents;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pelans.DragonCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.comphenix.protocol.PacketType.Play.Server.SPAWN_ENTITY;

public class ProtocolEntityEvents {
    private static final PacketType[] ENTITY_PACKETS = new PacketType[]{
            SPAWN_ENTITY
    };

    private static final List<UUID> list = new ArrayList<>();

    public static void register(){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(DragonCustom.plugin, ENTITY_PACKETS) {
            @Override
            public void onPacketSending(PacketEvent e) {
                if (list.contains(e.getPacket().getUUIDs().read(0))) {
                    list.remove(e.getPacket().getUUIDs().read(0));
                    return;
                }
                Player player = e.getPlayer();
                PacketContainer packet = e.getPacket();
                Entity entity = Bukkit.getEntity(packet.getUUIDs().read(0));
                if (entity == null) return;
                if (entity.getType().equals(EntityType.FALLING_BLOCK)){
                    list.add(packet.getUUIDs().read(0));
                    e.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(entity.getScoreboardTags().contains("lluvia_Block")){
                                Location loc = entity.getLocation();
                                Location loc2 = player.getLocation();
                                loc.setY(0);
                                loc2.setY(0);
                                if(loc.distance(loc2) <= 20){
                                    manager.sendServerPacket(player, packet);
                                }
                            }else {
                                manager.sendServerPacket(player, packet);
                            }
                        }
                    }.runTaskAsynchronously(DragonCustom.plugin);
                }
            }
        });
    }
}
