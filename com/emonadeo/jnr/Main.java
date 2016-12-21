package com.emonadeo.jnr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.emonadeo.jnr.events.Creation;
import com.emonadeo.jnr.events.Gameplay;
import com.emonadeo.jnr.events.TimeHandler;

public class Main extends JavaPlugin
{
	public File f0 = new File("plugins/Jump", "parkours.yml");
	public FileConfiguration prks = YamlConfiguration.loadConfiguration(f0);
	public Map<String, String> playerInParkour = new HashMap<String, String>();
	public Map<String, Integer> playerPoints = new HashMap<String, Integer>();
	public Map<String, Integer> playerTimes = new HashMap<String, Integer>();
	public int pointsGoal = 8;
	
	public Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	public Objective obj = board.registerNewObjective("string1", "string2");
	public Score scorePoints = obj.getScore("Punkte");
	
	@Override
	public void onEnable()
	{
		//Config
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		//Commands
		this.getCommand("course").setExecutor(new Commands(this));
		this.getCommand("cfg-reload").setExecutor(new Commands(this));
		
		//Events
		this.getServer().getPluginManager().registerEvents(new Creation(this), this);
		this.getServer().getPluginManager().registerEvents(new Gameplay(this), this);
		this.getServer().getPluginManager().registerEvents(new TimeHandler(this), this);
	}
	
	@Override
	public void onDisable()
	{
		
	}
}