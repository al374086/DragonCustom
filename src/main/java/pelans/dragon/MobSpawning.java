package pelans.dragon;



import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class MobSpawning{
	
	
	@SuppressWarnings("deprecation")
	public static void Bob(WitherSkeleton bob) {
    	ItemStack stack = new ItemStack(Material.STONE_SWORD);
		stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 60);
		bob.getEquipment().setItemInMainHand(stack);
		bob.getEquipment().setItemInMainHandDropChance(0);
		bob.setMaxHealth(200);
		bob.setHealth(200);
		
		Player jugador = Cabezas.getCabeza();
		
		bob.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c&l"+ jugador.getName() +" &DDeath Emperor"));
		bob.setCustomNameVisible(true);
		
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta sm = (SkullMeta) item.getItemMeta();
		sm.setOwningPlayer(jugador);
		item.setItemMeta(sm);
		bob.getEquipment().setHelmet(item);
		bob.getEquipment().setHelmetDropChance(0.08f);
		
		stack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.WHITE);
		stack.setItemMeta(meta);
		bob.getEquipment().setChestplate(stack);
		bob.getEquipment().setChestplateDropChance(0);
		
		stack = new ItemStack(Material.LEATHER_LEGGINGS);
		stack.setItemMeta(meta);
		bob.getEquipment().setLeggings(stack);
		bob.getEquipment().setLeggingsDropChance(0);
		
		stack = new ItemStack(Material.LEATHER_BOOTS);
		stack.addEnchantment(Enchantment.DEPTH_STRIDER, 2);
		stack.setItemMeta(meta);
		bob.getEquipment().setBoots(stack);
		bob.getEquipment().setBootsDropChance(0);
		
		bob.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,1));
		bob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,Integer.MAX_VALUE,0));
		
		bob.addScoreboardTag("Bob");
    }
	
	@SuppressWarnings("deprecation")
	public static void Rich2012(Skeleton rich2012) {
		ItemStack stack = new ItemStack(Material.BOW);
		stack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 50);
		rich2012.getEquipment().setItemInMainHand(stack);
		rich2012.getEquipment().setItemInMainHandDropChance(0);
		rich2012.setMaxHealth(120);
		rich2012.setHealth(120);
		Player jugador = Cabezas.getCabeza();
		rich2012.setCustomName(ChatColor.translateAlternateColorCodes('&',"&c&l"+ jugador.getName() + " &6Skeleton &e&lLORD"));
		rich2012.setCustomNameVisible(true);
		
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta sm = (SkullMeta) item.getItemMeta();
		sm.setOwningPlayer(jugador);
		item.setItemMeta(sm);
		rich2012.getEquipment().setHelmet(item);
		rich2012.getEquipment().setHelmetDropChance(0.08f);
		
		stack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.BLACK);
		stack.setItemMeta(meta);
		rich2012.getEquipment().setChestplate(stack);
		rich2012.getEquipment().setChestplateDropChance(0);
		
		stack = new ItemStack(Material.LEATHER_LEGGINGS);
		stack.setItemMeta(meta);
		rich2012.getEquipment().setLeggings(stack);
		rich2012.getEquipment().setLeggingsDropChance(0);
		
		stack = new ItemStack(Material.LEATHER_BOOTS);
		stack.addEnchantment(Enchantment.DEPTH_STRIDER, 2);
		stack.setItemMeta(meta);
		rich2012.getEquipment().setBoots(stack);
		rich2012.getEquipment().setBootsDropChance(0);
		
		rich2012.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,0));
		rich2012.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,2));
		rich2012.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,Integer.MAX_VALUE,0));
		
		rich2012.addScoreboardTag("Rich2012");
    }
	


}
