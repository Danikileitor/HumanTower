package ml.dnk.humantower;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
		htswordmeta.setDisplayName("�1�OLa Apiladora");
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
//Si te molesta el ruido				e.getEntity().setSilent(true);
					if(p.getPassengers().isEmpty()){
						p.addPassenger(p.getWorld().spawnEntity(p.getLocation(), EntityType.SPECTRAL_ARROW));
					}
					Entity p1;
					int i = 0;
					for(p1 = (Entity) e.getDamager();!p1.getPassengers().get(0).isEmpty();){
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
						p1 = p1.getPassengers().get(0); i++;
						}p1.getPassengers().get(0).addPassenger(e.getEntity()); i++;
					getServer().broadcastMessage(e.getDamager().getName()+" > "+e.getEntity().getName()+" ("+i+")");
	        	}
			else {e.setCancelled(false);}	
			}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		Player p = (Player) e.getPlayer();
		Action action = e.getAction();
		if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
			if(!p.isEmpty()){
				if(!p.getPassengers().get(0).isEmpty()){
					if(!p.getPassengers().get(0).getPassengers().get(0).isEmpty()){
						Entity pafuera = p.getPassengers().get(0).getPassengers().get(0).getPassengers().get(0);
						p.getPassengers().get(0).eject();
						p.getPassengers().get(0).addPassenger(pafuera);
					}else{p.getPassengers().get(0).eject();}
				}else{p.eject();}
			}
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
            p.addPassenger(p.getWorld().spawnEntity(p.getLocation(), EntityType.SPECTRAL_ARROW));
            return true;
        }
        return false;
    }
}
