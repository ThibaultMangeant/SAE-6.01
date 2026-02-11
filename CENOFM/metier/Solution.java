package CENOFM.metier;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une solution complète du CVRP.
 * 
 * Contient : - la liste des tournées - la charge associée à chaque tournée - la
 * distance totale de la solution
 */
public class Solution
{

	/** Liste des tournées (une tournée = liste de clients) */
	private List<List<Noeud>> tournees;

	/** Charge actuelle de chaque tournée */
	private List<Integer> charges;

	/** Distance totale de la solution */
	private double distanceTotale;

	/**
	 * Constructeur vide.
	 */
	public Solution()
	{
		this.tournees = new ArrayList<>();
		this.charges = new ArrayList<>();
		this.distanceTotale = 0;
	}

	/**
	 * Crée une copie profonde de la solution.
	 * 
	 * @return nouvelle instance indépendante
	 */
	public Solution copie()
	{
		Solution clone = new Solution();

		for (List<Noeud> t : this.tournees) { clone.tournees.add(new ArrayList<>(t)); }

		clone.charges = new ArrayList<>(this.charges);
		clone.distanceTotale = this.distanceTotale;

		return clone;
	}

	public List<List<Noeud>> getTournees() { return tournees; }
	public List<Integer> getCharges() { return charges; }
	public double getDistanceTotale() { return distanceTotale; }
	public void setDistanceTotale(double d) { this.distanceTotale = d; }
}
