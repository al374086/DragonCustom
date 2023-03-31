package pelans.dragon;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;



public class Cabezas {
	public static Iterator<? extends Player> iter;
	
	
	public static Player getCabeza() {
		if(!(iter != null && iter.hasNext())) {
			Collection<? extends Player> col = Bukkit.getServer().getOnlinePlayers();
			if( col.size() == 0) {
				return null;
			}
			else {
				iter = col.iterator();
			}
		}
		Player jugador = iter.next();
		return jugador;
	}
	
}
