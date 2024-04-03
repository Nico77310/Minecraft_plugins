package fr.sunder.taupegun.config;

/* Classe gérant la configuration du plugin Taupe Gun. */
public class ConfigManager {
	
	private double bordersize = 1000; /* Taille de la bordure. */
	
	/* Setter de taille pour la bordure. 
	 * @param bordersize Taille de la bordure.
	*/
	public void setBorderSize(double bordersize) {
		this.bordersize = bordersize;
	}
	
	/* Getter de taille pour la bordure. 
	 * @return double Taille de la bordure.
	*/
	public double getBorderSize() {
		return this.bordersize;
	}

}
