package com.emonadeo.jnr.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

import com.emonadeo.jnr.Main;
import com.emonadeo.jnr.lib.JoinRunnable;

public class TimeHandler implements Listener
{
	Main main;
	
	private int time;
	public int pointsGoal;
	
	public TimeHandler(Main main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		main.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(!(e.getPlayer().isOp()))
		{   
			time = main.getConfig().getInt("Minutes") * 60 + main.getConfig().getInt("Seconds");
			double d = time / 60;
			long m = (long) d;
			pointsGoal = main.getConfig().getInt("Goal");
			Player p = e.getPlayer();
			p.setDisplayName("§2" + p.getName() + "§r");
			p.setGameMode(GameMode.ADVENTURE);
			p.getInventory().clear();
			main.playerPoints.put(p.getName(), 0);
			p.sendMessage("§aDu hast " + m + ":" + ((time % 60) > 9 ? "" : "0") + time % 60 + " Zeit, so viele Punkte wie möglich zu sammeln!");
			main.obj.setDisplayName("§e" + m + ":" + ((time % 60) > 9 ? "" : "0") + time % 60);
			main.scorePoints.setScore(0);
			p.setScoreboard(main.board);
			main.playerTimes.put(p.getName(), time);
			JoinRunnable task = new JoinRunnable(main, p);
			task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(main, task, 20L, 20L));
		} else {
			e.getPlayer().setDisplayName("§4" + e.getPlayer().getName() + "§r");
			e.getPlayer().setGameMode(GameMode.CREATIVE);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		if(!e.getPlayer().isOp())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		String format = e.getFormat();
		format = "%1$s §7- §r%2$s";
		e.setFormat(format);
	}
}
