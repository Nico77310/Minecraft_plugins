package fr.sunder.taupegun.teams;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/* Classe gérant les équipes du plugin Taupe Gun. */
public class TeamManager {
	
	private Scoreboard board; /* Scoreboard du plugin. */
	private Team teamBleu; /* Equipe bleue. */
	private Team teamOrange; /* Equipe orange. */
	private Team teamVert; /* Equipe verte. */
	private Team teamJaune; /* Equipe jaune. */
	private Team teamTaupe; /* Equipe taupe. */
	
	/* Constructeur de la classe.
	 * @param board Scoreboard du plugin.
	 */
	public TeamManager(Scoreboard board) {
		this.board = board;
	}
	
	/* Méthode permettant de créer les équipes du plugin. */
	public void createTeams() {
		
		teamBleu = board.registerNewTeam("Bleu");
		teamBleu.setColor(ChatColor.AQUA);
		teamOrange = board.registerNewTeam("Orange");
		teamOrange.setColor(ChatColor.GOLD);
		teamVert = board.registerNewTeam("Vert");
		teamVert.setColor(ChatColor.GREEN);
		teamJaune = board.registerNewTeam("Jaune");
		teamJaune.setColor(ChatColor.YELLOW);
		teamTaupe = board.registerNewTeam("Taupe");
		teamTaupe.setColor(ChatColor.RED);
		
	}
		
	/* Méthode permettant de récupérer l'équipe bleue.
	 * @return Team Equipe bleue.
	 */
	public Team getBleu() {
		return this.teamBleu;
	}
	
	/* Méthode permettant de récupérer l'équipe orange.
	 * @return Team Equipe orange.
	 */
	public Team getOrange() {
		return this.teamOrange;
	}
	
	/* Méthode permettant de récupérer l'équipe verte.
	 * @return Team Equipe verte.
	 */
	public Team getVert() {
		return this.teamVert;
	}
	
	/* Méthode permettant de récupérer l'équipe jaune.
	 * @return Team Equipe jaune.
	 */
	public Team getJaune() {
		return this.teamJaune;
	}
	
	/* Méthode permettant de récupérer l'équipe taupe.
	 * @return Team Equipe taupe.
	 */
	public Team getTaupe() {
		return this.teamTaupe;
	}

}
