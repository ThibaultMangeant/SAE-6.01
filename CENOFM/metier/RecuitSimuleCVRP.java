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

		Solution actuelle = genererSolutionInitiale();
		calculerDistanceTotale(actuelle);

		Solution meilleure = actuelle.copie();

		List<Solution> snapshots = new ArrayList<>();
		snapshots.add(actuelle.copie());

		double temperature = tempInit;
		int iteration = 0;
		int iterationsSansAmelioration = 0;

		int nbIterationsParPalier = 1000 + donnees.getNbClients() * 20;

		while (temperature > seuilArret && iterationsSansAmelioration < nbIttSansAmelioration)
		{
			// ===== PALIER =====
			for (int i = 0; i < nbIterationsParPalier; i++)
			{
				Solution voisin = genererVoisin(actuelle);
				double delta = voisin.getDistanceTotale() - actuelle.getDistanceTotale();

				if (delta <= 0 || Math.exp(-delta / temperature) > random.nextDouble())
				{
					actuelle = voisin;

					if (actuelle.getDistanceTotale() < meilleure.getDistanceTotale())
					{
						meilleure = actuelle.copie();
						iterationsSansAmelioration = 0;
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

				if (iteration % intervalObservation == 0)
					snapshots.add(actuelle.copie());
			}

			// 🔥 Refroidissement seulement après le palier
			temperature = alpha * temperature;
		}

		long fin = System.currentTimeMillis();
		double temps = (fin - debut) / 1000.0;

		return new ResultatRecuit(meilleure, snapshots, temps, iteration);
	}

	// Génère un voisin aléatoire
	private Solution genererVoisin(Solution sol)
	{
		Solution voisin = sol.copie();

		int choix = random.nextInt(5);

		switch (choix)
		{
		case 0 -> mouvementRelocate(voisin);
		case 1 -> mouvementSwap(voisin);
		case 2 -> mouvement2OptIntra(voisin);
		case 3 -> mouvement2OptInter(voisin);
		case 4 -> mouvementOrOpt(voisin);
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

	private void mouvement2OptInter(Solution sol)
	{
		if (sol.getTournees().size() < 2)
			return;

		int r1 = random.nextInt(sol.getTournees().size());
		int r2 = random.nextInt(sol.getTournees().size());

		if (r1 == r2)
			return;

		List<Noeud> route1 = sol.getTournees().get(r1);
		List<Noeud> route2 = sol.getTournees().get(r2);

		if (route1.size() < 2 || route2.size() < 2)
			return;

		int cut1 = random.nextInt(route1.size());
		int cut2 = random.nextInt(route2.size());

		List<Noeud> new1 = new ArrayList<>(route1.subList(0, cut1));
		new1.addAll(route2.subList(cut2, route2.size()));

		List<Noeud> new2 = new ArrayList<>(route2.subList(0, cut2));
		new2.addAll(route1.subList(cut1, route1.size()));

		int charge1 = new1.stream().mapToInt(n -> n.demande).sum();
		int charge2 = new2.stream().mapToInt(n -> n.demande).sum();

		if (charge1 <= donnees.getqMax() && charge2 <= donnees.getqMax())
		{
			sol.getTournees().set(r1, new1);
			sol.getTournees().set(r2, new2);
			sol.getCharges().set(r1, charge1);
			sol.getCharges().set(r2, charge2);
		}
	}

	private void mouvementOrOpt(Solution sol)
	{
		int r = random.nextInt(sol.getTournees().size());
		List<Noeud> route = sol.getTournees().get(r);

		if (route.size() < 3)
			return;

		int i = random.nextInt(route.size() - 2);
		int j = i + 1 + random.nextInt(2);

		List<Noeud> segment = new ArrayList<>(route.subList(i, j));
		route.subList(i, j).clear();

		int insertPos = random.nextInt(route.size());
		route.addAll(insertPos, segment);
	}

	// Génère une solution initiale aléatoire respectant la capacité max
	private Solution genererSolutionInitiale()
	{
		Solution sol = new Solution();

		// 1️⃣ Une route par client
		for (Noeud client : donnees.getClients())
		{
			List<Noeud> route = new ArrayList<>();
			route.add(client);

			sol.getTournees().add(route);
			sol.getCharges().add(client.demande);
		}

		calculerDistanceTotale(sol);

		// 2️⃣ Calcul des savings
		class Saving
		{
			Noeud i, j;
			double value;

			Saving(Noeud i, Noeud j, double v)
			{
				this.i = i;
				this.j = j;
				this.value = v;
			}
		}

		List<Saving> savings = new ArrayList<>();

		for (Noeud i : donnees.getClients())
		{
			for (Noeud j : donnees.getClients())
			{
				if (i.id >= j.id)
					continue;

				double s = distance(0, i.id) + distance(0, j.id) - distance(i.id, j.id);

				savings.add(new Saving(i, j, s));
			}
		}

		savings.sort((a, b) -> Double.compare(b.value, a.value));

		// 3️⃣ Fusion des routes
		for (Saving s : savings)
		{
			List<Noeud> routeI = trouverRoute(sol, s.i);
			List<Noeud> routeJ = trouverRoute(sol, s.j);

			if (routeI == null || routeJ == null)
				continue;
			if (routeI == routeJ)
				continue;

			int chargeI = chargeRoute(sol, routeI);
			int chargeJ = chargeRoute(sol, routeJ);

			if (chargeI + chargeJ > donnees.getqMax())
				continue;

			if (routeI.get(routeI.size() - 1) == s.i && routeJ.get(0) == s.j)
			{
				routeI.addAll(routeJ);
				sol.getTournees().remove(routeJ);
			}
		}

		sol.getCharges().clear();
		for (List<Noeud> r : sol.getTournees())
		{
			int c = 0;
			for (Noeud n : r)
				c += n.demande;
			sol.getCharges().add(c);
		}

		calculerDistanceTotale(sol);
		return sol;
	}

	private List<Noeud> trouverRoute(Solution sol, Noeud client)
	{
		for (List<Noeud> r : sol.getTournees())
			if (r.contains(client))
				return r;
		return null;
	}

	private int chargeRoute(Solution sol, List<Noeud> r)
	{
		int sum = 0;
		for (Noeud n : r)
			sum += n.demande;
		return sum;
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
