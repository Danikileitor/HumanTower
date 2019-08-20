package ml.dnk.humantower;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	Logger log = Bukkit.getLogger();
	ItemStack htsword = getEspada();
	ItemStack aire = new ItemStack(Material.AIR);
	
	public ItemStack getEspada(){
		ItemStack htsword = new ItemStack(Material.GOLDEN_SWORD, 1);
		ItemMeta htswordmeta = htsword.getItemMeta();
		ArrayList<String> lores = new ArrayList<>();
		lores.add("Golpea y apila a tus enemigos");
		htswordmeta.setLore(lores);
		htswordmeta.addEnchant(Enchantment.DURABILITY, Enchantment.DURABILITY.getMaxLevel(), true);
		htswordmeta.setDisplayName("§1§OLa Apiladora");
		htsword.setItemMeta(htswordmeta);

		return htsword;
	}
	
	public boolean mismoItem(ItemStack a, ItemStack b){
		String n1, n2;
		if (a.hasItemMeta())
			n1= a.getItemMeta().getDisplayName();
		else
			n1 = a.getType().name();
		if (b.hasItemMeta())
			n2=b.getItemMeta().getDisplayName();
		else
			n2 = b.getType().name();
		return n1.equals(n2);
	}
	
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent e){
		if (e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			ItemStack mano = p.getInventory().getItemInMainHand();
			if (mismoItem(mano, htsword)){
				e.setCancelled(true);
				getServer().broadcastMessage(e.getDamager().getName()+" > "+e.getEntity().getName());
//Si te molesta el ruido				e.getEntity().setSilent(true);
					if(p.getPassengers().isEmpty()){p.addPassenger(p.getWorld().spawnEntity(p.getLocation(), EntityType.ARROW));}
/*
*	if(p.getPassengers().get(0).getPassengers().isEmpty()){p.getPassengers().get(0).addPassenger(e.getEntity());}
*					else {p.getPassengers().get(0).getPassengers().get(0).addPassenger(e.getEntity());}
*/
				for(p1 = (Entity) e.getDamager();!p1.getPassengers().get(0).isEmpty()){
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
				p1 = p1.getPassengers().get(0);
				}p1.addPassenger(e.getEntity);
	        	}
			else {e.setCancelled(false);}		
			}
	}
	
    @Override
    public void onEnable() {
    	log.info("[HT] Enjoy lifting humans!");
    	
    	getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
    	log.info("[HT] No more lifting :(");
    }
    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
    	Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("ht1") && sender.hasPermission("ht.1")) {
            sender.sendMessage("Espada pa ti!");
            p.getInventory().addItem(htsword);
            p.addPassenger(p.getWorld().spawnEntity(p.getLocation(), EntityType.ARROW));
            return true;
        }
        return false;
    }
}
