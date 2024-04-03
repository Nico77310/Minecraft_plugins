package fr.sunder.taupegun.game;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.sunder.taupegun.config.ConfigManager;
import fr.sunder.taupegun.listeners.TaupeGunListener;
import fr.sunder.taupegun.players.PlayerManager;
import fr.sunder.taupegun.teams.TeamManager;

/* Classe gérant le jeu du plugin Taupe Gun. */
public class GameManager {
	
	private WorldBorder border; /* Bordure du monde. */
	private int time; /* Temps écoulé. */
	private int day; /* Jour actuel. */
	private int Nplayer; /* Nombre de joueurs. */
	@SuppressWarnings("unused")
	private ScoreboardManager manager; /* Gestionnaire de Scoreboard. */
	private Scoreboard board; /* Scoreboard du plugin. */
	private Objective objective; /* Objectif propre au timer. */
	private Objective health; /* Objectif de la santé affiché en liste. */
	private Objective hp; /* Objectif de la santé affiché en indice du nom de joueur. */
	private TeamManager tmanager; /* Gestionnaire des équipes. */
	private ConfigManager config; /* Configuration du plugin. */
	private boolean gameRunning; /* Etat du jeu. */
	private boolean invincible; /* Etat d'invincibilité pour les joueurs. */
	private PlayerManager pmanager; /* Gestionnaire des joueurs. */
	private JavaPlugin plugin; /* Plugin. */
	
	/* Constructeur de la classe.
	 * @param config Configuration du plugin.
	 * @param pmanager Gestionnaire des joueurs.
	 * @param tmanager Gestionnaire des équipes.
	 * @param manager Gestionnaire de Scoreboard.
	 * @param board Scoreboard du plugin.
	 * @param plugin Plugin.
	 * @param gameRunning Etat du jeu.
	 */
	public GameManager(ConfigManager config, PlayerManager pmanager, TeamManager tmanager, ScoreboardManager manager, Scoreboard board, JavaPlugin plugin, boolean gameRunning) {
		this.config = config;
		this.gameRunning = gameRunning;
		this.pmanager = pmanager;
		this.tmanager = tmanager;
		this.manager = manager;
		this.board = board;
		this.plugin = plugin;
	}
	
	/* Méthode permettant de définir si le jeu est en cours ou non.
	 * @param gameRunning Etat du jeu.
	 * @return void
	 */
	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
	
	/* Méthode permettant de récupérer l'état du jeu.
	 * @return boolean Etat du jeu.
	 */
	public boolean getGameRunning() {
		return this.gameRunning;
	}
	
	/* Méthode permettant de récupérer l'état d'invicibilité des joueurs.
	 * @return boolean Etat d'invicibilité des joueurs.
	 */
	public boolean getInvincible() {
		return this.invincible;
	}
	
	/* Méthode initialisant les paramètres nécéssaire au jeu. */
	@SuppressWarnings("deprecation")
	public void setUp() {
		objective = board.registerNewObjective("TaupeGun", "dummy", ChatColor.GOLD + "Taupe Gun");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		health = board.registerNewObjective("health", "health", "Health");
		health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		hp = board.registerNewObjective("hp", "health", ChatColor.DARK_RED + "♥");
		hp.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}
	
	/* Méthode permettant de démarrer le jeu. */
	public void start() {
		invincible = true;
		border = Bukkit.getWorlds().get(0).getWorldBorder();
		border.setSize(config.getBorderSize());
		border.setDamageAmount(0);
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.NATURAL_REGENERATION, false);
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
			world.setDifficulty(Difficulty.HARD);
		}
		Team[] teams = {tmanager.getBleu(), tmanager.getOrange(), tmanager.getVert(), tmanager.getJaune()};
		for (Team team : teams) {
			double x = border.getCenter().getX() + (Math.random() - 0.5) * border.getSize();
			double z = border.getCenter().getZ() + (Math.random() - 0.5) * border.getSize();
			for (String playerName : team.getEntries()) {
				Player player = Bukkit.getPlayer(playerName);
				if (player != null) {
					player.teleport(new Location(Bukkit.getWorlds().get(0), x, 200, z));
                    player.setHealthScale(20);
                    player.setHealthScaled(true);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setScoreboard(board);
                    player.setGameMode(GameMode.SURVIVAL);
				}
			}
		}
		Bukkit.getPluginManager().registerEvents(new TaupeGunListener(board, tmanager, this, gameRunning), plugin);
		Bukkit.broadcastMessage(ChatColor.AQUA + "-------- GO ! --------");
		Bukkit.getWorlds().get(0).setTime(6000);
		time = 0;
		day = 1;
		Nplayer = tmanager.getBleu().getEntries().size() + tmanager.getOrange().getEntries().size() + tmanager.getVert().getEntries().size() + tmanager.getJaune().getEntries().size() + tmanager.getTaupe().getEntries().size();
		AtomicReference<String> timeString = new AtomicReference<>(String.format("Chrono: §a%02d:%02d", time / 60, time % 60));
		AtomicReference<String> dayString = new AtomicReference<>(String.format("Jour: §a%d", day));
		AtomicReference<String> playerString = new AtomicReference<>(String.format("Joueurs: §a%d", Nplayer));
		objective.getScore(timeString.get()).setScore(0);
		objective.getScore("").setScore(1);
		objective.getScore(playerString.get()).setScore(2);
		objective.getScore(dayString.get()).setScore(3);
		new BukkitRunnable() {
			/* Méthode permettant de gérer le timer du jeu. */
			@Override
			public void run() {
				if (!gameRunning) {
					return;
				}
				Bukkit.getWorlds().get(0).setStorm(false);
				Nplayer = tmanager.getBleu().getEntries().size() + tmanager.getOrange().getEntries().size() + tmanager.getVert().getEntries().size() + tmanager.getJaune().getEntries().size() + tmanager.getTaupe().getEntries().size();
				board.resetScores(timeString.get());
				board.resetScores(playerString.get());
				time++;
				/* Retirer l'état d'invicibilité au bout de 30 secondes. */
				if (time == 30 && day == 1) {
					invincible = false;
				}
				/* Incrémentation du jour de 1 au bout de 20 minutes */
				if (time >= 1200) {
					board.resetScores(dayString.get());
					time = 0;
					day++;
					dayString.set(String.format("Jour: §a%d", day));
					objective.getScore(dayString.get()).setScore(3);
					Bukkit.broadcastMessage(ChatColor.AQUA + "-------- Jour Suivant --------");
				}
				/* Initialisation des taupes au bout de 50 minutes de jeu. */
				if (time == 600 && day == 3) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (pmanager.getIsTaupe().get(player.getName())) {
							player.sendMessage(ChatColor.RED + "----------");
							player.sendMessage(ChatColor.GOLD + "Vous êtes une taupe ! Roulez votre équipe et rejoignez les autres taupes.");
							player.sendMessage(ChatColor.GOLD + "Tapez '/reveal' pour vous révéler à tout moment.");
							player.sendMessage(ChatColor.GOLD + "Tapez '/t <message>' pour envoyer un message aux autres taupes.");
							player.sendMessage(ChatColor.RED + "----------");
						}
					}
				}
				timeString.set(String.format("Chrono: §a%02d:%02d", time / 60, time % 60));
				objective.getScore(timeString.get()).setScore(0);
				playerString.set(String.format("Joueurs: §a%d", Nplayer));
				objective.getScore(playerString.get()).setScore(2);
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20).getTaskId(); // 20 ticks = 1 seconde
	}

}
