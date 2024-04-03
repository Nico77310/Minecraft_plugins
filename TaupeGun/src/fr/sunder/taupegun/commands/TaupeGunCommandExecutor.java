package fr.sunder.taupegun.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.sunder.taupegun.config.ConfigManager;
import fr.sunder.taupegun.game.GameManager;
import fr.sunder.taupegun.players.PlayerManager;
import fr.sunder.taupegun.teams.TeamManager;

/* Classe gérant les commandes du plugin Taupe Gun. */
public class TaupeGunCommandExecutor implements CommandExecutor {
	
	private boolean gameRunning; /* Etat du jeu. */
	private ScoreboardManager manager; /* Gestionnaire de Scoreboard. */
	private Scoreboard board; /* Scoreboard du plugin. */
	private ConfigManager config; /* Configuration du plugin. */
	private GameManager gm; /* Gestionnaire du jeu. */
	private TeamManager tmanager; /* Gestionnaire des équipes. */
	private PlayerManager pmanager; /* Gestionnaire des joueurs. */
	private JavaPlugin plugin; /* Plugin. */
	
	/* Constructeur de la classe. 
	 * @param manager Gestionnaire de Scoreboard.
	 * @param board Scoreboard du plugin.
	 * @param config Configuration du plugin.
	 * @param tmanager Gestionnaire des équipes.
	 * @param pmanager Gestionnaire des joueurs.
	 * @param plugin Plugin.
	 * @param gameRunning Etat du jeu.
	*/
	public TaupeGunCommandExecutor(ScoreboardManager manager, Scoreboard board, ConfigManager config, TeamManager tmanager, PlayerManager pmanager, JavaPlugin plugin, boolean gameRunning) {
		this.gameRunning = gameRunning;
		this.manager = manager;
		this.board = board;
		this.config = config;
		this.tmanager = tmanager;
		this.pmanager = pmanager;
		this.plugin = plugin;
	}

	/* Méthode appelée lors de l'exécution d'une commande.
	 * @param sender CommandSender.
	 * @param command Commande.
	 * @param label Label.
	 * @param args Arguments.
	 * @return boolean.
	*/
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/* Commande "taupegun". */
		if (command.getName().equalsIgnoreCase("taupegun")) {
			/* Argument "start" :
			 * - Si le jeu n'est pas en cours, on le démarre.
			 */
			if (args[0].equals("start")) {
				if (!gameRunning) {
					gameRunning = true;
					gm = new GameManager(config, pmanager, tmanager, manager, board, plugin, gameRunning);
					gm.setUp();
					gm.start();
					pmanager.setPlayersIsTaupe();
					pmanager.setTaupes();
				}
			/* Argument "stop" :
			 * - Si le jeu est en cours, on le met en pause.
			 */
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (gm.getGameRunning()) {
					gm.setGameRunning(false);
				}
			/* Argument "reset" :
			 * - Si le jeu est en pause, on le réinitialise.
			 */
			} else if (args[0].equals("reset")) {
				if (!gm.getGameRunning()) {
					gm = null;
				}
			/* Argument "resume" :
			 * - Si le jeu est pas en pause, on le reprend.
			 */
			} else if (args[0].equals("resume")) {
				if (!gm.getGameRunning()) {
					gm.setGameRunning(true);
				}
			/* Argument "config" :
			 * - Si le jeu n'a pas débuter, on le configure.
			 */
			} else if (args[0].equals("config")) {
				if (!gameRunning) {
					/* Argument "border" :
					 * - Si l'argument "border" est présent, on configure la bordure du jeu.
					 */
					if (args[1].equals("border")) {
						if (args.length == 3) {
							String size = args[2];
							double bordersize = Double.parseDouble(size);
							this.config.setBorderSize(bordersize);
							Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "La bordure du jeu a été définie à " + size);
						}
					} else {
						if (sender instanceof Player) {
							Player player = (Player) sender;
							player.sendMessage(ChatColor.RED + "/taupegun config [border <size>]");
						}
					}
				}
			/* Argument "team" :
			 * - Si le jeu n'a pas débuter, on configure les équipes.
			 */
			} else if (args[0].equals("team")) {
				if (!gameRunning) {
					/* Argument "add" :
					 * - Si l'argument "add" est présent, on ajoute un joueur à une équipe.
					 */
					if (args[1].equals("add")) {
						if (args.length == 4) {
							/* Ajout du joueur à l'équipe Bleu. */
							if (args[2].equals("Bleu")) {
								tmanager.getBleu().addEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.AQUA + "Le joueur " + args[3] + " a été ajouté à l'équipe Bleu");
							/* Ajout du joueur à l'équipe Orange. */
							} else if (args[2].equals("Orange")) {
								tmanager.getOrange().addEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.GOLD + "Le joueur " + args[3] + " a été ajouté à l'équipe Orange");
							/* Ajout du joueur à l'équipe Vert. */
							} else if (args[2].equals("Vert")) {
								tmanager.getVert().addEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.GREEN + "Le joueur " + args[3] + " a été ajouté à l'équipe Vert");
							/* Ajout du joueur à l'équipe Jaune. */
							} else if (args[2].equals("Jaune")) {
								tmanager.getJaune().addEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.YELLOW + "Le joueur " + args[3] + " a été ajouté à l'équipe Jaune");
							}
						}
					/* Argument "remove" :
					 * - Si l'argument "remove" est présent, on retire un joueur d'une équipe.
					 */
					} else if (args[1].equals("remove")) {
						if (args.length == 4) {
							/* Retrait du joueur de l'équipe Bleu. */
							if (args[2].equals("Bleu")) {
								tmanager.getBleu().removeEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.AQUA + "Le joueur " + args[3] + " a été ajouté à l'équipe Bleu");
							/* Retrait du joueur de l'équipe Orange. */
							} else if (args[2].equals("Orange")) {
								tmanager.getOrange().removeEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.GOLD + "Le joueur " + args[3] + " a été ajouté à l'équipe Orange");
							/* Retrait du joueur de l'équipe Vert. */
							} else if (args[2].equals("Vert")) {
								tmanager.getVert().removeEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.GREEN + "Le joueur " + args[3] + " a été ajouté à l'équipe Vert");
							/* Retrait du joueur de l'équipe Jaune. */
							} else if (args[2].equals("Jaune")) {
								tmanager.getJaune().removeEntry(args[3]);
								Bukkit.broadcastMessage(ChatColor.YELLOW + "Le joueur " + args[3] + " a été ajouté à l'équipe Jaune");
							}
						}
					} else {
						if (sender instanceof Player) {
							Player player = (Player) sender;
							player.sendMessage(ChatColor.RED + "/taupegun team [add|remove] [Bleu|Orange|Vert|Jaune] <joueur>");
						}
					}
				}
			} else {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.sendMessage(ChatColor.RED + "/taupegun [start|reset|resume|config|team]");
				}
			}
		/* Commande "t". 
		 * - Si le joueur est une taupe, on envoie un message aux autres taupes.
		*/
		} else if (command.getName().equalsIgnoreCase("t")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String executorName = player.getName();
				if (pmanager.getIsTaupe().get(executorName)) {
					String message = "";
					for (String arg : args) {
						message = message + arg + " ";
					}
					List<String> taupes = new ArrayList<String>();
					for (Player joueur : Bukkit.getServer().getOnlinePlayers()) {
						if (pmanager.getIsTaupe().get(joueur.getName())) {
							taupes.add(joueur.getName());
						}
					}
					for (String taupeName : taupes) {
						Player taupe = Bukkit.getPlayer(taupeName);
						taupe.sendMessage("§6[Taupes] §c<" + executorName + "§c> " + message);
					}
				} else {
					player.sendMessage(ChatColor.RED + "La commande '/t' est reservé aux taupes");
				}
			}
		/* Commande "reveal".
		 * - Si le joueur est une taupe, on révèle son identité à tous les joueurs.
		*/
		} else if (command.getName().equalsIgnoreCase("reveal")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String executorName = player.getName();
				if (pmanager.getIsTaupe().get(executorName)) {
					Bukkit.broadcastMessage(ChatColor.GOLD + "--- " + executorName + " se révèle être une taupe ! ---");
					for (Player joueur : Bukkit.getServer().getOnlinePlayers()) {
						joueur.playSound(joueur.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0F, 1.0F);
					}
					player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE));
					tmanager.getTaupe().addEntry(executorName);
				} else {
					player.sendMessage(ChatColor.RED + "La commande '/reveal' est reservé aux taupes");
				}
			}
		}
		return true;
	}

}
