package com.emonadeo.jnr.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.emonadeo.jnr.Main;

public class JoinRunnable implements Runnable
{
	Main main;
	Player p;
	int id, minutes, seconds;
	
	public JoinRunnable(Main main, Player p, int minutes, int seconds)
	{
		this.main = main;
		this.p = p;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	@Override
	public void run() {
		//Time
		main.obj.setDisplayName("§e" + (minutes > 9 ? minutes : "0" + minutes) + (seconds > 9 ? ":" + seconds : ":0" + seconds));
		if(minutes == 0 && seconds == 0)
		{
			//End
			if(main.playerPoints.get(p.getName()) >= main.pointsGoal)
				p.kickPlayer("§aHerzlichen Glückwunsch! §fDu hast gewonnen. Bitte zeige einem Leiter diese Nachricht und hole dir deine Belohnung!");
			else
				p.kickPlayer("§cLeider nicht Gewonnen! §fVielleicht beim nächsten Mal!");
			Bukkit.getScheduler().cancelTask(id);
		} else {
			//Countdown
			if(seconds == 0)
			{
				minutes--;
				seconds = 59;
			} else {
				seconds--;
			}
		}
		if(!(p.isOnline()))
		{
			Bukkit.getScheduler().cancelTask(id);
		}
	}
}
