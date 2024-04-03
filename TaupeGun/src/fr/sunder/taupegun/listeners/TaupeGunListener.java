package fr.sunder.taupegun.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.sunder.taupegun.game.GameManager;
import fr.sunder.taupegun.teams.TeamManager;

/* Classe gérant les événements du plugin Taupe Gun. */
public class TaupeGunListener implements Listener {
	
	private boolean gameRunning; /* Etat du jeu. */
	private Scoreboard board; /* Scoreboard du plugin. */
	private TeamManager tmanager; /* Gestionnaire des équipes. */
	private GameManager gm; /* Gestionnaire du jeu. */
	
	/* Constructeur de la classe.
	 * @param board Scoreboard du plugin.
	 * @param tmanager Gestionnaire des équipes.
	 * @param gm Gestionnaire du jeu.
	 * @param gameRunning Etat du jeu.
	 */
	public TaupeGunListener(Scoreboard board, TeamManager tmanager, GameManager gm, boolean gameRunning) {
		this.board = board;
		this.gameRunning = gameRunning;
		this.tmanager = tmanager;
		this.gm = gm;
	}
	
	/* Méthode appelée lors de la mort d'un joueur.
	 * Elle affiche un message indiquant que le joueur est mort et le met en mode spectateur.
	 * @param event PlayerDeathEvent.
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (gameRunning) {
			event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GOLDEN_APPLE));
			event.getEntity().setGameMode(GameMode.SPECTATOR);
			String playerName = event.getEntity().getName();
			if (tmanager.getBleu().hasPlayer(event.getEntity())){
				playerName = ChatColor.AQUA + event.getEntity().getName();
			} else if (tmanager.getOrange().hasPlayer(event.getEntity())) {
				playerName = ChatColor.GOLD + event.getEntity().getName();
			} else if (tmanager.getVert().hasPlayer(event.getEntity())) {
				playerName = ChatColor.GREEN + event.getEntity().getName();
			} else if (tmanager.getJaune().hasPlayer(event.getEntity())) {
				playerName = ChatColor.YELLOW + event.getEntity().getName();
			} else if (tmanager.getTaupe().hasPlayer(event.getEntity())) {
				playerName = ChatColor.RED + event.getEntity().getName();
			}
			Team team = board.getPlayerTeam(event.getEntity());
			team.removePlayer(event.getEntity());
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
			}
			Bukkit.broadcastMessage(playerName + ChatColor.DARK_RED + " est Mort !");
		}
	}
	
	/* Méthode appelée lors de la connexion d'un joueur.
	 * Elle ajoute le joueur au scoreboard.
	 * @param event PlayerJoinEvent.
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setScoreboard(board);
	}
	
	/* Méthode appelée lorsqu'un joueur envoie un message dans le chat.
	 * Elle ajoute la couleur de l'équipe du joueur à son message.
	 * @param event AsyncPlayerChatEvent.
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (tmanager.getBleu().hasPlayer(event.getPlayer())) {
			String playerName = ChatColor.AQUA + event.getPlayer().getName();
			event.setFormat("<" + playerName + ChatColor.RESET + "> " + event.getMessage());
		} else if (tmanager.getOrange().hasPlayer(event.getPlayer())) {
			String playerName = ChatColor.GOLD + event.getPlayer().getName();
			event.setFormat("<" + playerName + ChatColor.RESET + "> " + event.getMessage());
		} else if (tmanager.getVert().hasPlayer(event.getPlayer())) {
			String playerName = ChatColor.GREEN + event.getPlayer().getName();
			event.setFormat("<" + playerName + ChatColor.RESET + "> " + event.getMessage());
		} else if (tmanager.getJaune().hasPlayer(event.getPlayer())) {
			String playerName = ChatColor.YELLOW + event.getPlayer().getName();
			event.setFormat("<" + playerName + ChatColor.RESET + "> " + event.getMessage());
		} else if (tmanager.getTaupe().hasPlayer(event.getPlayer())) {
			String playerName = ChatColor.RED + event.getPlayer().getName();
			event.setFormat("<" + playerName + ChatColor.RESET + "> " + event.getMessage());
		} else {
			event.setFormat("<" + event.getPlayer().getName() + "> " + event.getMessage());
		}
	}
	
	/* Méthode appelée lorsqu'une entité subit des dégâts.
	 * Elle annule les dégâts si le mode invincible est activé à savoir pendant les 30 premières secondes de jeu.
	 * @param event EntityDamageEvent.
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (gm.getInvincible()) {
			if (event.getEntity() instanceof Player) {
				event.setCancelled(true);
			}
		}
	}

}
