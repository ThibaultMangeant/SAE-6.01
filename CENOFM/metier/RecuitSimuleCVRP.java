package CENOFM.metier;

import java.util.*;

/**
 * Recuit simulé pour le CVRP.
 * 
 * Fonctionnalités : - Génère solution initiale respectant les capacités -
 * Explore le voisinage via Relocate, Swap, 2-opt intra - Critère d'arrêt :
 * température minimale ou N itérations sans amélioration - Stocke snapshots
 * toutes les X itérations pour affichage
 */
public class RecuitSimuleCVRP
{

	private final DonneesVrp donnees;
	private final Random random = new Random();
	private double[][] matriceDistance;

	public RecuitSimuleCVRP(DonneesVrp donnees)
	{
		this.donnees = donnees;
		initialiserMatriceDistance();
	}

	// Pré-calcul de toutes les distances pour accélérer les calculs
	private void initialiserMatriceDistance()
	{
		Noeud[] noeuds = donnees.getTableauNoeudsComplet();
		int n = noeuds.length;
		matriceDistance = new double[n][n];
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				double dx = noeuds[i].x - noeuds[j].x;
				double dy = noeuds[i].y - noeuds[j].y;
				matriceDistance[i][j] = Math.sqrt(dx * dx + dy * dy);
			}
		}
	}

	private double distance(int id1, int id2)
	{
		return matriceDistance[id1][id2];
	}

	/**
	 * Résout le CVRP par recuit simulé.
	 * 
	 * @param tempInit
	 *            Initial temperature
	 * @param seuilArret
	 *            Minimum temperature
	 * @param alpha
	 *            Coefficient de refroidissement
	 * @param nbIttSansAmelioration
	 *            Nombre d'itérations sans amélioration pour arrêter
	 * @param intervalObservation
	 *            Intervalle pour snapshots
	 * @return ResultatRecuit
	 */
	public ResultatRecuit resoudre(double tempInit, double seuilArret, double alpha, int nbIttSansAmelioration,
			int intervalObservation)
	{

		long debut = System.currentTimeMillis();

		// Génération de la solution initiale
		Solution actuelle = genererSolutionInitiale();

		// Vérification des capacités
		for (int i = 0; i < actuelle.getCharges().size(); i++)
		{
			if (actuelle.getCharges().get(i) > donnees.getqMax())
			{
				String msg = "Erreur : la solution initiale dépasse la capacité du véhicule " + (i + 1);
				System.err.println(msg);

				// Renvoi d'une solution "vide" pour le contrôleur
				Solution erreurSol = new Solution();
				erreurSol.setDistanceTotale(Double.POSITIVE_INFINITY);
				return new ResultatRecuit(erreurSol, new ArrayList<>(), 0, 0);
			}
		}

		calculerDistanceTotale(actuelle);
		Solution meilleure = actuelle.copie();
		List<Solution> snapshots = new ArrayList<>();

		double temperature = tempInit;
		int iteration = 0;
		int iterationsSansAmelioration = 0;

		// Boucle principale
		while (temperature > seuilArret && iterationsSansAmelioration < nbIttSansAmelioration)
		{

			Solution voisin = genererVoisin(actuelle);
			double delta = voisin.getDistanceTotale() - actuelle.getDistanceTotale();

			if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble())
			{
				actuelle = voisin;

				if (actuelle.getDistanceTotale() < meilleure.getDistanceTotale())
				{
					meilleure = actuelle.copie();
					iterationsSansAmelioration = 0; // reset compteur
				}
				else
				{
					iterationsSansAmelioration++;
				}
			}
			else
			{
				iterationsSansAmelioration++;
			}

			iteration++;

			// Snapshot toutes les X itérations
			if (iteration % intervalObservation == 0) { snapshots.add(actuelle.copie()); }

			// Refroidissement
			temperature *= alpha;
		}

		long fin = System.currentTimeMillis();
		double temps = (fin - debut) / 1000.0;

		// Optionnel : afficher graphiquement
		// new FrameGraphique(meilleure.getTournees(), donnees.getDepot());

		return new ResultatRecuit(meilleure, snapshots, temps, iteration);
	}

	// Génère un voisin aléatoire
	private Solution genererVoisin(Solution sol)
	{
		Solution voisin = sol.copie();
		int choix = random.nextInt(3);
		switch (choix)
		{
		case 0 -> mouvementRelocate(voisin);
		case 1 -> mouvementSwap(voisin);
		case 2 -> mouvement2OptIntra(voisin);
		}
		calculerDistanceTotale(voisin);
		return voisin;
	}

	// Déplace un client d'une tournée à une autre
	private void mouvementRelocate(Solution sol)
	{
		if (sol.getTournees().isEmpty())
			return;

		int t1 = random.nextInt(sol.getTournees().size());
		int t2 = random.nextInt(sol.getTournees().size());
		if (sol.getTournees().get(t1).isEmpty())
			return;

		int index = random.nextInt(sol.getTournees().get(t1).size());
		Noeud client = sol.getTournees().get(t1).remove(index);
		sol.getCharges().set(t1, sol.getCharges().get(t1) - client.demande);

		if (sol.getCharges().get(t2) + client.demande <= donnees.getqMax())
		{
			sol.getTournees().get(t2).add(client);
			sol.getCharges().set(t2, sol.getCharges().get(t2) + client.demande);
		}
		else
		{
			sol.getTournees().get(t1).add(client);
			sol.getCharges().set(t1, sol.getCharges().get(t1) + client.demande);
		}
	}

	// Échange deux clients entre tournées
	private void mouvementSwap(Solution sol)
	{
		if (sol.getTournees().size() < 2)
			return;

		int t1 = random.nextInt(sol.getTournees().size());
		int t2 = random.nextInt(sol.getTournees().size());
		if (t1 == t2)
			return;

		List<Noeud> r1 = sol.getTournees().get(t1);
		List<Noeud> r2 = sol.getTournees().get(t2);
		if (r1.isEmpty() || r2.isEmpty())
			return;

		int i1 = random.nextInt(r1.size());
		int i2 = random.nextInt(r2.size());
		Noeud c1 = r1.get(i1);
		Noeud c2 = r2.get(i2);

		int charge1 = sol.getCharges().get(t1) - c1.demande + c2.demande;
		int charge2 = sol.getCharges().get(t2) - c2.demande + c1.demande;

		if (charge1 <= donnees.getqMax() && charge2 <= donnees.getqMax())
		{
			r1.set(i1, c2);
			r2.set(i2, c1);
			sol.getCharges().set(t1, charge1);
			sol.getCharges().set(t2, charge2);
		}
	}

	// 2-opt intra pour une tournée
	private void mouvement2OptIntra(Solution sol)
	{
		int t = random.nextInt(sol.getTournees().size());
		List<Noeud> route = sol.getTournees().get(t);
		if (route.size() < 3)
			return;

		int i = random.nextInt(route.size() - 1);
		int j = random.nextInt(route.size() - i - 1) + i + 1;
		Collections.reverse(route.subList(i, j));
	}

	// Génère une solution initiale aléatoire respectant la capacité max
	private Solution genererSolutionInitiale()
	{
		Solution sol = new Solution();

		for (int i = 0; i < donnees.getNbVehicules(); i++)
		{
			sol.getTournees().add(new ArrayList<>());
			sol.getCharges().add(0);
		}

		for (Noeud client : donnees.getClients())
		{
			if (client.demande > donnees.getqMax())
			{
				System.err.println("Erreur : le client " + client.id + " dépasse la capacité maximale du véhicule !");
				continue; // on ignore ce client ou on pourrait arrêter
							// l'algorithme
			}
			boolean ajoute = false;
			// Chercher un véhicule avec assez de capacité
			for (int v = 0; v < donnees.getNbVehicules(); v++)
			{
				if (sol.getCharges().get(v) + client.demande <= donnees.getqMax())
				{
					sol.getTournees().get(v).add(client);
					sol.getCharges().set(v, sol.getCharges().get(v) + client.demande);
					ajoute = true;
					break;
				}
			}
			if (!ajoute)
			{
				System.err.println(
						"Erreur : impossible d’affecter le client " + client.id + " sans dépasser la capacité !");
				// Ici on pourrait soit :
				// - Créer un nouveau véhicule temporaire
				// - Ou retourner null / solution invalide
			}
		}
		return sol;
	}

	// Calcule la distance totale d'une solution
	private void calculerDistanceTotale(Solution sol)
	{
		double total = 0;
		for (List<Noeud> route : sol.getTournees())
		{
			if (route.isEmpty())
				continue;
			total += distance(0, route.get(0).id); // dépôt -> premier client
			for (int i = 0; i < route.size() - 1; i++)
			{
				total += distance(route.get(i).id, route.get(i + 1).id);
			}
			total += distance(route.get(route.size() - 1).id, 0); // dernier
																	// client ->
																	// dépôt
		}
		sol.setDistanceTotale(total);
	}

	// Formate une solution pour affichage
	public String formatterSolution(Solution s)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Distance totale : ").append(String.format("%.2f", s.getDistanceTotale())).append("\n");
		sb.append("Nombre de véhicules : ").append(s.getTournees().size()).append("\n");
		for (int i = 0; i < s.getTournees().size(); i++)
		{
			sb.append("Véhicule ").append(i + 1).append(" : Dépôt");
			for (Noeud c : s.getTournees().get(i))
				sb.append(" -> ").append(c.id);
			sb.append(" -> Dépôt\n");
		}
		return sb.toString();
	}

}
