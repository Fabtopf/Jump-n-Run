package com.emonadeo.jnr.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.emonadeo.jnr.Main;

public class JoinRunnable implements Runnable
{
	Main main;
	Player p;
	int id;
	
	public JoinRunnable(Main main, Player p)
	{
		this.main = main;
		this.p = p;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	@Override
	public void run() {
		//Time
		int time = main.playerTimes.get(p.getName());
		if(time == 0)
		{
			//End
			if(main.playerPoints.get(p.getName()) >= main.pointsGoal)
				p.kickPlayer("§aHerzlichen Glückwunsch! §fDu hast gewonnen. Bitte zeige einem Leiter diese Nachricht und hole dir deine Belohnung!");
			else
				p.kickPlayer("§cLeider nicht Gewonnen! §fVielleicht beim nächsten Mal!");
			Bukkit.getScheduler().cancelTask(id);
		} else {
			//Countdown
			double d = time / 60;
			long m = (long) d;
			main.playerTimes.put(p.getName(), time - 1);
			main.obj.setDisplayName("§e" + m + ":" + ((time % 60) > 9 ? "" : "0") + time % 60);
		}
		if(!(p.isOnline()))
		{
			Bukkit.getScheduler().cancelTask(id);
		}
	}
}
