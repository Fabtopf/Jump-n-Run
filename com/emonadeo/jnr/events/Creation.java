package com.emonadeo.jnr.events;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.emonadeo.jnr.Main;

public class Creation implements Listener
{
	Main main;
	
	public Creation(Main main) {
		this.main = main;
	}
	
	//==============
	//Name
	//Difficulty
	//==============
	@EventHandler
	public void onCreateSign(SignChangeEvent e)
	{
		//Colors
		int i = 0;
		for(String line : e.getLines())
		{
			line = line.replace('&', '§');
			e.setLine(i, line);
			i++;
		}
		
		//Generate
		if(e.getLine(0).contains("[JUMP]"))
		{
			String name = ChatColor.stripColor(e.getLine(1));
			String difficulty = ChatColor.stripColor(e.getLine(2));
			String color = e.getLine(0).substring(6);
			e.setLine(0, color.replace('&', '§') + "==============");
			e.setLine(3, color.replace('&', '§') + "==============");
			e.getPlayer().sendMessage("§f" + name + " §acreated.");
			
			//Save
			main.prks.set(name + ".difficulty", difficulty);
			main.prks.set(name + ".sign.world", e.getBlock().getWorld().getName());
			main.prks.set(name + ".sign.x", e.getBlock().getX());
			main.prks.set(name + ".sign.y", e.getBlock().getY());
			main.prks.set(name + ".sign.z", e.getBlock().getZ());
			
			try {
				main.prks.save(main.f0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
