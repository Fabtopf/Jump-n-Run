package com.emonadeo.jnr.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import com.emonadeo.jnr.Main;

public class Gameplay implements Listener
{
	Main main;
	
	public Gameplay(Main main) {
		this.main = main;
	}
	
	//Join Parkour
	@EventHandler
	public void onEnterJump(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(e.getClickedBlock().getState() instanceof Sign)
			{
				if(!(main.playerInParkour.containsKey(e.getPlayer().getName())))
				{
					Sign sign = (Sign)e.getClickedBlock().getState();
					if(main.prks.contains(ChatColor.stripColor(sign.getLine(1))))
					{
						Player p = e.getPlayer();
						String name = ChatColor.stripColor(sign.getLine(1));
						enterParkour(p, name);
					}
				} else {
					e.getPlayer().sendMessage("§cDu bist bereits in einem Parkour");
				}
			}
		}
	}
	
	//Exit Parkour
	@EventHandler
	public void onReachGoal(PlayerMoveEvent e)
	{
		if(main.playerInParkour.containsKey(e.getPlayer().getName()))
		{
			Player p = e.getPlayer();
			String name = main.playerInParkour.get(p.getName());
			World w = Bukkit.getWorld(main.prks.getString(name + ".end.world"));
			int x = main.prks.getInt(name + ".end.x");
			int y = main.prks.getInt(name + ".end.y");
			int z = main.prks.getInt(name + ".end.z");
			
			if(w.getName() == p.getLocation().getWorld().getName() && x == p.getLocation().getBlockX() && y == p.getLocation().getBlockY() && z == p.getLocation().getBlockZ())
			{
				int points = main.playerPoints.get(p.getName());
				points += main.prks.getInt(name + ".points");
				main.playerPoints.put(p.getName(), points);
				main.scorePoints.setScore(points);
				exitParkour(e.getPlayer());
				p.teleport(p.getLocation().getWorld().getSpawnLocation());
				p.sendMessage("§a+ " + main.prks.getInt(name + ".points") + " Punkte!");
			}
		}
	}
	
	@EventHandler
	public void onManualActions(PlayerInteractEvent e)
	{
		if((e.getAction() == Action.RIGHT_CLICK_BLOCK && !(e.getClickedBlock().getState() instanceof Sign)) || e.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if(main.playerInParkour.containsKey(e.getPlayer().getName()))
			{
				Player p = e.getPlayer();
				//Exit
				if(p.getItemInHand().getType() == Material.PRISMARINE_SHARD)
				{
					exitParkour(p);
					p.teleport(p.getWorld().getSpawnLocation());
				}
				//Restart
				if(p.getItemInHand().getType() == Material.INK_SACK)
				{
					restartParkour(p);
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		exitParkour(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e)
	{
		exitParkour(e.getPlayer());
	}
	
	//Method Lib
	public void restartParkour(Player p)
	{
		String name = main.playerInParkour.get(p.getName());
		enterParkour(p, name);
	}
	
	public void exitParkour(Player p)
	{
		main.playerInParkour.remove(p.getName());
		p.getInventory().clear();
	}
	
	public void enterParkour(Player p, String name)
	{		
		//Var
		World w = Bukkit.getWorld(main.prks.getString(name + ".start.world"));
		double x = main.prks.getDouble(name + ".start.x");
		double y = main.prks.getDouble(name + ".start.y");
		double z = main.prks.getDouble(name + ".start.z");
		float yaw = (float)main.prks.getDouble(name + ".start.yaw");
		float pitch = (float)main.prks.getDouble(name + ".start.pitch");
		
		//Teleport
		Location start = new Location(w, x, y, z, yaw, pitch);
		p.teleport(start);		
		
		if(!(main.playerInParkour.containsKey(p.getName())))
		{
			//Items
			Dye dye = new Dye();
			
			dye.setColor(DyeColor.RED);
			ItemStack stackRestart = dye.toItemStack(1);
			ItemMeta metaRestart = stackRestart.getItemMeta();
			metaRestart.setDisplayName("§cRespawn");
			stackRestart.setItemMeta(metaRestart);
			
			ItemStack stackExit = new ItemStack(Material.PRISMARINE_SHARD, 1);
			ItemMeta metaExit = stackRestart.getItemMeta();
			metaExit.setDisplayName("§3§lHub");
			stackExit.setItemMeta(metaExit);
			
			p.getInventory().setItem(4, stackRestart);
			p.getInventory().setItem(0, stackExit);
			
			//Formals
			main.playerInParkour.put(p.getName(), name);
		}
	}
}
