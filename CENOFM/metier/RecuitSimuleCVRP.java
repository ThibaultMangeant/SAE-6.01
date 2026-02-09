package CENOFM.metier;

import java.util.*;

class Solution {
	List<List<Noeud>> tournees;
	double distanceTotale;

	public Solution() {
		this.tournees = new ArrayList<>();
		this.distanceTotale = 0;
	}

	public Solution copie() {
		Solution nouvelle = new Solution();
		for (List<Noeud> tournee : this.tournees) {
			nouvelle.tournees.add(new ArrayList<>(tournee));
		}
		nouvelle.distanceTotale = this.distanceTotale;
		return nouvelle;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Distance totale : ").append(String.format("%.2f", distanceTotale)).append("\n");
		for (int i = 0; i < tournees.size(); i++) {
			sb.append("Véhicule ").append(i + 1).append(" : Dépôt");
			for (Noeud c : tournees.get(i)) {
				sb.append(" -> ").append(c.id);
			}
			sb.append(" -> Dépôt\n");
		}
		return sb.toString();
	}
}

public class RecuitSimuleCVRP {
	private List<Noeud> Noeuds;
	private Noeud depot;
	private int qMax;
	private Random random = new Random();

	public RecuitSimuleCVRP(List<Noeud> Noeuds, Noeud depot, int qMax) {
		this.Noeuds = Noeuds;
		this.depot = depot;
		this.qMax = qMax;
	}

	public String resoudre() {

		double temperature = 1000.0;
		double temperatureMin = 0.1;
		double alpha = 0.999;
		int iterationsParPalier = 100;

		long tempsDebut = System.currentTimeMillis();

		Solution actuelle = genererSolutionInitiale();
		calculerDistanceTotale(actuelle);
		Solution meilleure = actuelle.copie();

		//System.out.println("Distance initiale : " + actuelle.distanceTotale);

		while (temperature > temperatureMin) {
			for (int i = 0; i < iterationsParPalier; i++) {
				Solution voisin = genererVoisin(actuelle);
				double delta = voisin.distanceTotale - actuelle.distanceTotale;

				if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble()) {
					actuelle = voisin;
					if (actuelle.distanceTotale < meilleure.distanceTotale) {
						meilleure = actuelle.copie();
					}
				}
			}
			temperature *= alpha;
		}

		long tempsFin = System.currentTimeMillis();
		double tempsTotal = (tempsFin - tempsDebut) / 1000.0; // Conversion en secondes

		System.out.println(afficherResultats(meilleure, 0));
		System.out.println("Temps de résolution : " + tempsTotal + " secondes");
	}

	private Solution genererVoisin(Solution actuelle) {
		Solution voisin = actuelle.copie();

		int choix = random.nextInt(3);
		switch (choix) {
			case 0:
				mouvementEchange(voisin);
				break;
			case 1:
				mouvementDeplacement(voisin);
				break;
			case 2:
				mouvement2Opt(voisin);
				break;
		}

		calculerDistanceTotale(voisin);
		return voisin;
	}

	private void mouvement2Opt(Solution sol) {
		if (sol.tournees.isEmpty())
			return;
		int tIdx = random.nextInt(sol.tournees.size());
		List<Noeud> tournee = sol.tournees.get(tIdx);

		if (tournee.size() < 2)
			return;

		int i = random.nextInt(tournee.size());
		int j = random.nextInt(tournee.size());

		if (i > j) {
			int temp = i;
			i = j;
			j = temp;
		}

		Collections.reverse(tournee.subList(i, j + 1));
	}

	private void mouvementEchange(Solution sol) {
		int t1 = random.nextInt(sol.tournees.size());
		int t2 = random.nextInt(sol.tournees.size());

		List<Noeud> r1 = sol.tournees.get(t1);
		List<Noeud> r2 = sol.tournees.get(t2);

		if (r1.isEmpty() || r2.isEmpty())
			return;

		int i1 = random.nextInt(r1.size());
		int i2 = random.nextInt(r2.size());

		Noeud c1 = r1.get(i1);
		Noeud c2 = r2.get(i2);

		if (calculerDemande(r1) - c1.demande + c2.demande <= qMax &&
				calculerDemande(r2) - c2.demande + c1.demande <= qMax) {
			r1.set(i1, c2);
			r2.set(i2, c1);
		}
	}

	private void mouvementDeplacement(Solution sol) {
		int t1 = random.nextInt(sol.tournees.size());
		int t2 = random.nextInt(sol.tournees.size());

		List<Noeud> depart = sol.tournees.get(t1);
		List<Noeud> arrivee = sol.tournees.get(t2);

		if (depart.isEmpty())
			return;

		int index = random.nextInt(depart.size());
		Noeud c = depart.get(index);

		if (calculerDemande(arrivee) + c.demande <= qMax) {
			depart.remove(index);
			arrivee.add(c);
			if (depart.isEmpty() && sol.tournees.size() > 1) {
				sol.tournees.remove(t1);
			}
		}
	}

	private Solution genererSolutionInitiale() {
		Solution sol = new Solution();
		for (Noeud c : Noeuds) {
			List<Noeud> nouvelleTournee = new ArrayList<>();
			nouvelleTournee.add(c);
			sol.tournees.add(nouvelleTournee);
		}
		return sol;
	}

	private void calculerDistanceTotale(Solution sol) {
		double d = 0;
		for (List<Noeud> t : sol.tournees) {
			if (t.isEmpty())
				continue;
			d += depot.distance(t.get(0));
			for (int i = 0; i < t.size() - 1; i++) {
				d += t.get(i).distance(t.get(i + 1));
			}
			d += t.get(t.size() - 1).distance(depot);
		}
		sol.distanceTotale = d;
	}

	private int calculerDemande(List<Noeud> tournee) {
		int total = 0;
		for (Noeud c : tournee)
			total += c.demande;
		return total;
	}

	public String afficherResultats(Solution s, int iteration) {

		String resultat = (iteration > 0 ? "Itération : " + iteration + "\n" : "Meilleure solution : \n");

		resultat += "Distance totale : " + String.format("%.2f", s.distanceTotale) + "\n";
		resultat += "Nombre de véhicules : " + s.tournees.size() + "\n";

		for (int i = 0; i < s.tournees.size(); i++) {
			resultat += "Véhicule " + (i + 1) + " : Dépôt";
			for (Noeud c : s.tournees.get(i))
				resultat += " -> " + c.id;
			resultat += " -> Dépôt\n";
		}
		return resultat;
	}
}
