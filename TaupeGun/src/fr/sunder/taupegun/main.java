package fr.sunder.taupegun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.sunder.taupegun.commands.TaupeGunCommandExecutor;
import fr.sunder.taupegun.config.ConfigManager;
import fr.sunder.taupegun.players.PlayerManager;
import fr.sunder.taupegun.teams.TeamManager;

/* Classe principale du plugin Minecraft Taupe Gun. */
public class main extends JavaPlugin {
	
	private TaupeGunCommandExecutor command;  /* Commandes du plugin. */
	private ScoreboardManager manager; /* Gestionnaire de Scoreboard. */
	private Scoreboard board; /* Scoreboard du plugin. */
	private ConfigManager config; /* Configuration du plugin. */
	private TeamManager tmanager; /* Gestionnaire des équipes. */
	private PlayerManager pmanager; /* Gestionnaire des joueurs. */
	
	/* Méthode appelée lors de l'activation du plugin. */
	@Override 
	public void onEnable() {
		getLogger().info("Le plugin Taupe Gun est activé !");
		manager =  Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		config = new ConfigManager();
		tmanager = new TeamManager(board);
		tmanager.createTeams();
		pmanager = new PlayerManager(tmanager);
		command = new TaupeGunCommandExecutor(manager, board, config, tmanager, pmanager, this, false);
		getCommand("taupegun").setExecutor(command);
		getCommand("t").setExecutor(command);
		getCommand("reveal").setExecutor(command);
	}
	
	/* Méthode appelée lors de la désactivation du plugin. */
	@Override
	public void onDisable() {
		getLogger().info("Le plugin Taupe Gun est désactivé !");
	}

}
