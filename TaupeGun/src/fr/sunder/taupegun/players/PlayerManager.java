package fr.sunder.taupegun.players;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.sunder.taupegun.teams.TeamManager;

/* Classe gérant les joueurs du plugin Taupe Gun. */
public class PlayerManager {
	
	private HashMap<String, Boolean> isTaupe; /* Dictionnaire contenant l'information pour chaque joueurs si c'est une taupe ou non */
	private TeamManager tmanager; /* Gestionnaire des équipes. */
	
	/* Constructeur de la classe.
	 * @param tmanager Gestionnaire des équipes.
	 */
	public PlayerManager(TeamManager tmanager) {
		this.isTaupe = new HashMap<String, Boolean>();
		this.tmanager = tmanager;
	}
	
	/* Méthode permettant de définir si un joueur est une taupe ou non.
	 * @param isTaupe Dictionnaire contenant l'information pour chaque joueurs si c'est une taupe ou non.
	 * @return void
	 */
	public void setIsTaupe(HashMap<String, Boolean> isTaupe) {
		this.isTaupe = isTaupe;
	}
	
	/* Méthode permettant de récupérer si un joueur est une taupe ou non. 
	 * @return HashMap<String, Boolean> Dictionnaire contenant l'information pour chaque joueurs si c'est une taupe ou non.
	*/
	public HashMap<String, Boolean> getIsTaupe() {
		return this.isTaupe;
	}
	
	/* Méthode permettant de mettre l'information "isTaupe" à false pour chaque joueurs */
	public void setPlayersIsTaupe() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			String playerName = player.getName();
			this.isTaupe.put(playerName, false);
		}
	}
	
	/* Méthode permettant de définir les taupes aléatoirement dans chaque équipes. */
	public void setTaupes() {
		Team[] teams = {tmanager.getBleu(), tmanager.getOrange(), tmanager.getVert(), tmanager.getJaune()};
		for (Team team : teams) {
			Set<String> playersInTeam = team.getEntries();
			if (!playersInTeam.isEmpty()) {
				Random random = new Random();
				int index = random.nextInt(playersInTeam.size());
				this.isTaupe.put((String) playersInTeam.toArray()[index], true);
			}
		}
	}

}
