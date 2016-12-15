package com.emonadeo.jnr;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String label, String[] args)
	{
		if(sen instanceof Player)
		{
			if(label.equalsIgnoreCase("cfg-reload"))
			{
				if(sen.isOp())
				{
					main.reloadConfig();
					sen.sendMessage("§aReloaded Config");
				}
				return true;
			}
			
			if(label.equalsIgnoreCase("course"))
			{
				Player p = (Player)sen;
				if(args.length > 1)
				{
					String name = args[0];
					if(main.prks.contains(name))
					{
						//Icon
						// - course name icon [material]
						if(args[1].equalsIgnoreCase("icon"))
						{
							Material icon;
							if(args.length > 2)
							{
								icon = Material.getMaterial(args[2]);
							} else {
								icon = p.getItemInHand().getType();
							}
							main.prks.set(name + ".icon", icon.toString());
							try {
								main.prks.save(main.f0);
								p.sendMessage("§f" + name + "'s §aIcon: §f" + icon.toString());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						//Points
						// - course name points [int]
						if(args[1].equalsIgnoreCase("points"))
						{
							if(args.length > 2)
							{
								try {
									int ps = Integer.parseInt(args[2]);
									main.prks.set(name + ".points", ps);
								} catch(NumberFormatException e) {
									p.sendMessage("§cNot a number: §f" + args[2]);
									return true;
								}
								try {
									main.prks.save(main.f0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						
						//Start
						// - course name start
						if(args[1].equalsIgnoreCase("start"))
						{
							double x, y, z;
							float yaw, pitch;
							
							x = p.getLocation().getX();
							y = p.getLocation().getY();
							z = p.getLocation().getZ();
							yaw = p.getLocation().getYaw();
							pitch = p.getLocation().getPitch();
							
							main.prks.set(name + ".start.world", p.getWorld().getName());
							main.prks.set(name + ".start.x", x);
							main.prks.set(name + ".start.y", y);
							main.prks.set(name + ".start.z", z);
							main.prks.set(name + ".start.yaw", yaw);
							main.prks.set(name + ".start.pitch", pitch);
							try {
								main.prks.save(main.f0);
								p.sendMessage("§f" + name + "'s §aStart: §7[" + (int)x + "," + (int)y + "," + (int)z + "]");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						//Goal
						// - course name end
						if(args[1].equalsIgnoreCase("end"))
						{
							int x, y, z;
							
							x = p.getLocation().getBlockX();
							y = p.getLocation().getBlockY();
							z = p.getLocation().getBlockZ();
							
							main.prks.set(name + ".end.world", p.getWorld().getName());
							main.prks.set(name + ".end.x", x);
							main.prks.set(name + ".end.y", y);
							main.prks.set(name + ".end.z", z);
							try {
								main.prks.save(main.f0);
								p.sendMessage("§f" + name + "'s §aEnd: §7[" + (int)x + "," + (int)y + "," + (int)z + "]");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						p.sendMessage("§f" + name + " §cdoesn't exist. §7(Names are Case sensitive)");
					}
					return true;
				}
			}
		}
		return false;
	}

}
